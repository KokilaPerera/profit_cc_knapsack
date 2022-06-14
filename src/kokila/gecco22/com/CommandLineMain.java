package kokila.gecco22.com;

import jmetal.core.SolutionSet;
import jmetal.util.JMException;
import kokila.gecco22.com.metaheuristics.GSEMO_Setting;
import kokila.gecco22.com.metaheuristics.OnePlusOne_MuVar_setting;
import kokila.gecco22.com.metaheuristics.mu_var.MuVarGSEMO_Setting;
import kokila.gecco22.com.metaheuristics.mu_var.NSGAII_Setting;
import kokila.gecco22.com.problems.knapsack.Knapsack;
import kokila.gecco22.com.problems.knapsack.mu_var.MuVar_KnapsackMO;
import kokila.gecco22.com.run.BestSolutionVisualizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class CommandLineMain {

    //private final double alpha;
    private double delta;
    private int iterations;
    private int evaluations;
    Knapsack problem;
    private File inputDataFile;
    Scanner scanner;
    private String datasetName;
    private int capacity;

    private boolean visualize = false;

    public CommandLineMain(String filename, double delta, int iterations, int evaluations, int algorithm) {



        //scanner = new Scanner(System.in);
        inputDataFile = new File(filename);

        try {
            problem = loadProblemData(inputDataFile);
        } catch (IOException ex) {
            System.out.println("Error in loading file");
            ex.printStackTrace();
        }

        //alpha = getParamSelection("alpha", new double[]{0.001, 0.01, 0.1});
        this.delta = delta;
        this.iterations = iterations;
        this.evaluations = evaluations;

        executeAlgorithm(algorithm);
    }

    public CommandLineMain() {
        scanner = new Scanner(System.in);
        System.out.println("Welcome!\nGive the benchmark file name: ");

        String folder = "benchmarks/";
        //String folder = "/hpfs/users/a1846115/Documents/Projects/benchmarks/";
        String path = scanner.nextLine();
        inputDataFile = new File(folder+path);

        if(inputDataFile == null)
        {
            System.out.println("ERROR: File not selected");
        }
        try {
            problem = loadProblemData(inputDataFile);
        } catch (IOException ex) {
            System.out.println("Error in loading file");
            ex.printStackTrace();
        }

        //alpha = getParamSelection("alpha", new double[]{0.001, 0.01, 0.1});
        delta = getParamSelection("delta", new double[]{25.0, 50.0});
        iterations = getParamSelection("iterations", new int[]{30, 20, 10, 5, 1});
        evaluations = getParamSelection("evaluations", new int[]{10000000, 1000000, 100000, 10000});
        int algorithm = getAlgoSelection(new String[]{ "GSEMO - Pareto Opti.", "NSGAII - Pareto Opti.", "GSEMO"});

        executeAlgorithm(algorithm);
    }

    private int getAlgoSelection( String[] values)
    {
        System.out.println("Please select the algorithm: ");
        int i = 0;
        for(String value: values)
            System.out.print("\t"+(++i)+":"+value +", ");
        System.out.print("\nEnter your selection 1 - " + (i++) + ": ");
        int input  = scanner.nextInt();
        if(input>0 && input<i)
            return input-1;
        else return -1;
    }

    private int getParamSelection(String paramName, int[] values)
    {
        System.out.println("Please select the value for "+paramName);
        int i = 0;

        for (int value : values)
            System.out.print("\t" + (++i) + ":" + value +",");
        System.out.print("\nEnter your selection 1 - " + (i++) + ": ");
        int input  = scanner.nextInt();
        if(input>0 && input<i)
            return values[input-1];
        else return -1;
    }

    private double getParamSelection(String paramName, double[] values)
    {
        System.out.println("Please select the value for "+paramName);
        int i = 0;

        for (double value : values)
            System.out.print("\t" + (++i) + ": " + value +",");
        System.out.print("\nEnter your selection 1 - " + (i++) + ": ");
        int input  = scanner.nextInt();
        if(input>0 && input<i)
            return values[input-1];
        else return -1.0;
    }

    private void executeAlgorithm(int algoIndex){
            if (algoIndex == 0) {
                runGSEMO();
            } else if (algoIndex == 1) {
                runNSGA2();
            } else if (algoIndex == 2)
            {
                runGeneralGSEMO();
            }
            else{
                run1p1();
            }
    }

    private void runGeneralGSEMO() {
        String filePath = getOutputFilePath();
        printSettingParameters(filePath, "GSEMO");

        new File(filePath).mkdirs();

        try {
            for(int i=0; i<iterations;){
                i++;
                System.out.print(i+"\t");

                GSEMO_Setting experimentSetting = new GSEMO_Setting();
                experimentSetting.init(this.problem, delta, evaluations);
                SolutionSet results = experimentSetting.run();

                printAndVisualize(results, filePath, i);

            }
            //System.out.println("**** end of execution ****");

        } catch (JMException|ClassNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    private void printAndVisualize(SolutionSet results, String filePath, int index)
    {
        String functionFilePath = filePath+File.separator+"FUN_i_" + index;
        String varFilePath = filePath+File.separator+"VAR_i_" + index;


        results.printObjectivesToFile(functionFilePath);
        results.printVariablesToFile(varFilePath);

        if(visualize) {
            try {
                new BestSolutionVisualizer(delta).run(functionFilePath, new double[]{0.1, 0.001, 0.0001, 0.00001, 0.000001, 0.0000001});
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void printSettingParameters(String filePath, String algorithm) {
        System.out.println("\tInput\t"+datasetName+
                "\tOutput\t"+ filePath +"\tAlgorithm\t"+algorithm);
        System.out.println("\tEvaluations\t"+evaluations+"\tCapacity\t"+
                capacity+"\tDelta\t"+
                delta);
    }

    private void runGSEMO() {

        String filePath = getOutputFilePath();
        printSettingParameters(filePath, "GSEMO_Var");

        new File(filePath).mkdirs();

        try {
            for(int i=0; i<iterations;){
                i++;
                System.out.print(i+"\t");
                this.problem.setAlpha(0.01);

                //MuCGSEMO_Setting experimentSetting = new MuCGSEMO_Setting();
                MuVarGSEMO_Setting experimentSetting = new MuVarGSEMO_Setting();
                experimentSetting.init(this.problem, delta, evaluations);
                SolutionSet results = experimentSetting.run();

                printAndVisualize(results, filePath, i);
            }
            //System.out.println("**** end of execution ****");

        } catch (JMException|ClassNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }
    private void runNSGA2() {
        String filePath = getOutputFilePath();
        printSettingParameters(filePath, "NSGA2_Var");

        new File(filePath).mkdirs();
        try {
            for(int i=0; i<iterations;){
                System.out.print(++i+"\t");
                NSGAII_Setting experimentSetting = new NSGAII_Setting();
                experimentSetting.init(this.problem, delta, evaluations);
                SolutionSet results = experimentSetting.run();
                printAndVisualize(results, filePath, i);
            }
            System.out.println("**** end of execution ****");

        } catch (JMException|ClassNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }


    private void run1p1() {

        String filePath = getOutputFilePath();
        printSettingParameters(filePath, "OnePlusOne");

        new File(filePath).mkdirs();

        try {
            for(int i=0; i<iterations;){
                i++;
                System.out.print(i+"\t");

                //MuProbGSEMO_Setting experimentSetting = new MuProbGSEMO_Setting();
                OnePlusOne_MuVar_setting experimentSetting = new OnePlusOne_MuVar_setting();
                experimentSetting.init(this.problem, delta, evaluations);
                SolutionSet results = experimentSetting.run();

                printAndVisualize(results, filePath, i);
            }
            //System.out.println("**** end of execution ****");

        } catch (JMException|ClassNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }

    private String getOutputFilePath() {
        String filePath = String.valueOf(ProcessHandle.current().pid());
        filePath = "test_results"+File.separator+filePath;
        return filePath;
    }

    private Knapsack loadProblemData(File file) throws
            IOException {
        Scanner scanner = new Scanner(file);
        String splitRegex = ":(\\t|\\s)*";

        scanner.nextLine();//String problemName = scanner.nextLine().split(splitRegex)[1];
        scanner.nextLine();//problemName += scanner.nextLine().split(splitRegex)[1];

        datasetName = file.getName();


        String line = scanner.nextLine();
        int skipLines = Integer.parseInt(line.split(splitRegex)[1]);
        line = scanner.nextLine();
        int numberOfItems = Integer.parseInt(line.split(splitRegex)[1]);
        line = scanner.nextLine();
        capacity = Integer.parseInt(line.split(splitRegex)[1]);
        capacity = (int) (capacity/2.0);
        int[] weight = new int[numberOfItems];
        int[] profit = new int[numberOfItems];
        for (int i = 0; i < 6 + skipLines; i++)
            scanner.nextLine();

        for (int i = 0; i < numberOfItems; i++) {
            line = scanner.nextLine();
            String[] tokens = line.split("(\\s|\\t)+");
            profit[i] = Integer.parseInt(tokens[1]);
            weight[i] = Integer.parseInt(tokens[2]);
        }
        //System.out.println("Problem data is loaded. Number of items in the problem are : " + numberOfItems + "Weight Bound : " + capacity);
        MuVar_KnapsackMO prob = new MuVar_KnapsackMO(datasetName);
        prob.init(numberOfItems, capacity, profit, weight);
        return prob;
    }

    public static void main(String[] args) {
        if (args != null && args.length >= 5)  {
            new CommandLineMain(args[0], Double.parseDouble(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]));
        } else {
            new CommandLineMain();
        }
    }

}
