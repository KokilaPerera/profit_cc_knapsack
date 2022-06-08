package kokila.gecco22.com.run;

import jmetal.core.SolutionSet;
import jmetal.util.JMException;
import kokila.gecco22.com.metaheuristics.GSEMO_Setting;
import kokila.gecco22.com.metaheuristics.mu_var.MuVarGSEMO_Setting;
import kokila.gecco22.com.metaheuristics.mu_var.NSGAII_Setting;
import kokila.gecco22.com.problems.knapsack.Knapsack;
import kokila.gecco22.com.problems.knapsack.mu_var.MuVar_KnapsackMO;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Scanner;

public class NewUI {

    private JFrame frame;
    Knapsack problem;

    private boolean isAdjusted = false;

    JTextPane logTextPane;

    private JTextField datasetName;
    private JMenuItem openMenuItem;

    private JButton loadProblemButton;
    private File inputDataFile;

    private JButton adjustProblemButton;
    private JComboBox<Integer> gammaValue;

    private JTextField alphaTextField;
    private JComboBox<Double> deltaComboBox;
    private JComboBox<Integer> iterationsComboBox;
    private JComboBox<Integer> evaluationsComboBox;
    private JComboBox<String> algorithmsComboBox;

    private JButton runAlgorithmButton;
    private JTextField noItemsTextField;
    private JTextField capacityTextField;

    public NewUI() {

        //Creating the Frame
        frame = new JFrame("Experiments");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        //Creating the MenuBar and adding components
        JMenuBar mb = new JMenuBar();
        JMenu m1 = new JMenu("FILE");
        //JMenu m2 = new JMenu("Help");
        mb.add(m1);
        openMenuItem = new JMenuItem("Open");

        JMenuItem saveAsMenuItem = new JMenuItem("Save as");
        m1.add(openMenuItem);
        m1.add(saveAsMenuItem);

        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = 1;
        gbc.insets = new Insets(4,2,4,2);
        gbc.gridx = 0;
        gbc.gridy = -1;

        addSpannedComponent(panel, new JLabel("Dataset Name"), gbc);
        datasetName = new JTextField(20);
        addSpannedComponent(panel, datasetName, gbc);
        //addLabeledComponent(panel, "Dataset", datasetName, gbc);
        loadProblemButton = new JButton("Load Data");
        addSpannedComponent(panel, loadProblemButton, gbc);


        noItemsTextField = new JTextField(5);
        addLabeledComponent(panel, "No of Items", noItemsTextField, gbc);

        capacityTextField = new JTextField(8);
        addLabeledComponent(panel, "Weight Bounds", capacityTextField, gbc);

        gammaValue = new JComboBox<>(new Integer[]{0, 100});
        addLabeledComponent(panel, "Gamma", gammaValue, gbc);

        adjustProblemButton = new JButton("Adjust Weights");
        addSpannedComponent(panel, adjustProblemButton, gbc);

        alphaTextField = new JTextField("{0.001, 0.01, 0.1}");
        alphaTextField.setEnabled(false);
        addLabeledComponent(panel, "Alpha", alphaTextField, gbc);
        deltaComboBox = new JComboBox<>(new Double[]{25.0, 50.0});
        addLabeledComponent(panel, "Delta", deltaComboBox, gbc);
        iterationsComboBox = new JComboBox<>(new Integer[]{30, 20, 10, 5, 1});
        addLabeledComponent(panel, "No of iterations", iterationsComboBox, gbc);
        evaluationsComboBox = new JComboBox<>(new Integer[]{10000000, 1000000, 100000, 10000});
        addLabeledComponent(panel, "No of evaluations", evaluationsComboBox, gbc);
        algorithmsComboBox = new JComboBox<>(new String[]{ "GSEMO - Pareto Opti.", "NSGAII - Pareto Opti.", "GSEMO"});
        addLabeledComponent(panel, "Algorithm", algorithmsComboBox, gbc);

        runAlgorithmButton = new JButton("Run Algorithm");
        addSpannedComponent(panel, runAlgorithmButton, gbc);

        initFileOpeningComponents();
        initDataLoadingComponents();
        initDataAdjustingComponents();
        initExecutingComponents();

        // Text Area at the Center
        JTextArea ta = new JTextArea();
        logTextPane = new JTextPane();
        logTextPane.setForeground(Color.LIGHT_GRAY);
        //Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.WEST, panel);
        frame.getContentPane().add(BorderLayout.NORTH, mb);
        frame.getContentPane().add(BorderLayout.CENTER, ta);
        frame.getContentPane().add(BorderLayout.SOUTH, logTextPane);
        frame.setVisible(true);
    }

    private void addLabeledComponent(JPanel panel, String label, JComponent component, GridBagConstraints gbc){
        gbc.gridx = 0;
        gbc.gridy ++;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(component, gbc);
    }
    private void addSpannedComponent(JPanel panel, JComponent component, GridBagConstraints gbc){
        gbc.gridx = 0;
        gbc.gridy ++;
        gbc.gridwidth = 2;
        panel.add(component, gbc);
        gbc.gridwidth = 1;
    }

    private void initDataLoadingComponents() {
        noItemsTextField.setEnabled(false);
        capacityTextField.setEnabled(false);
        loadProblemButton.setEnabled(false);
        loadProblemButton.addActionListener(e -> {
             if(inputDataFile == null)
             {
                 JOptionPane.showMessageDialog(frame,
                         "Please select the file first. Goto FILE > Open",
                         "ERROR: File not selected",
                         JOptionPane.ERROR_MESSAGE);
             }
            try {
                problem = loadProblemData(inputDataFile);
                adjustProblemButton.setEnabled(true);
                isAdjusted = false;
            } catch (IOException ex) {
                System.out.println("Error in loading file");
                ex.printStackTrace();
            }
        });
    }

    private void initDataAdjustingComponents()
    {
        gammaValue.setSelectedIndex(0);
        adjustProblemButton.setEnabled(false);
        adjustProblemButton.addActionListener(e -> {
            if( isAdjusted ) {
                if( JOptionPane.showConfirmDialog(frame,
                        "The problem weights are already adjusted. Are you sure",
                        "WARNING: Already adjusted.",JOptionPane.YES_NO_OPTION)
                        == JOptionPane.NO_OPTION)
                    return;
            }

            if(problem == null)
            {
                JOptionPane.showMessageDialog(frame,
                        "Please load the data first.",
                        "ERROR: Data is not available.",
                        JOptionPane.ERROR_MESSAGE);
            }
            else if (gammaValue.getSelectedIndex() < 0)
            {
                JOptionPane.showMessageDialog(frame,
                        "Please select gamma value first.",
                        "ERROR: Parameter(s) is/are not available.",
                        JOptionPane.ERROR_MESSAGE);
            }
            runAlgorithmButton.setEnabled(true);
            problem = adjustWeights(problem, (Integer)gammaValue.getSelectedItem());
            isAdjusted = true;
            System.out.println("Problem weights are adjusted for gamma value : " + gammaValue.getSelectedItem());
        });
    }

    private void initExecutingComponents(){
        runAlgorithmButton.setEnabled(false);
        runAlgorithmButton.addActionListener(e -> {
            int algorithm = algorithmsComboBox.getSelectedIndex();
            if (algorithm == 0) {
                runGSEMO();
            } else if (algorithm == 1) {
                runNSGA2();
            } else if (algorithm == 2)
            {
                runGeneralGSEMO();
            }
        });
    }

    private void runGeneralGSEMO() {
        int iterations = (int) evaluationsComboBox.getSelectedItem();
        int testRounds = (int) iterationsComboBox.getSelectedItem();
        String filePath = new Timestamp(System.currentTimeMillis()).toString();
        filePath = "/home/a1846115/Projects/test_results/test_"+filePath;//"Users\\kokila\\Downloads\\Projects\\test_results\\test_"+filePath;
        filePath = filePath.replaceAll(":","-");
        System.out.println("\tDataset\t"+datasetName.getText()+
                "\tOutput:\t"+filePath+"\tRunning\tGSEMO");
        System.out.println("\tEvaluations\t"+iterations+"\tCapacity\t"+
                capacityTextField.getText()+"\tDelta\t"+
                deltaComboBox.getSelectedItem());

        new File(filePath).mkdirs();

        try {
            for(int i=0; i<testRounds;){
                i++;
                System.out.print(i+"\t");

                GSEMO_Setting experimentSetting = new GSEMO_Setting();
                experimentSetting.init(this.problem, (double)deltaComboBox.getSelectedItem(), iterations);
                SolutionSet results = experimentSetting.run();

                results.printObjectivesToFile(filePath+"\\FUN_i_" + i);
                results.printVariablesToFile(filePath+"\\VAR_i_" + i);
            }
            System.out.println("**** end of execution ****");

        } catch (JMException|ClassNotFoundException e) {
            logTextPane.setText(e.getMessage());
            e.printStackTrace();
        }

    }

    private void runGSEMO() {
        int iterations = (int) evaluationsComboBox.getSelectedItem();

        int testRounds = (int) iterationsComboBox.getSelectedItem();
        String filePath = new Timestamp(System.currentTimeMillis()).toString();
        //filePath = ".\\test_results\\test_"+filePath; // uncomment for windows
        filePath = "/home/a1846115/Projects/test_results/test_"+filePath; // for mac path
        filePath = filePath.replaceAll(":","-");
        System.out.println("\tDataset\t"+datasetName.getText()+
                "\tOutput:\t"+filePath+"\tRunning\tGSEMO");
        System.out.println("\tEvaluations\t"+iterations+"\tCapacity\t"+
                capacityTextField.getText()+"\tDelta\t"+
                deltaComboBox.getSelectedItem());

        new File(filePath).mkdirs();

        try {
            for(int i=0; i<testRounds;){
                i++;
                System.out.print(i+"\t");

                MuVarGSEMO_Setting experimentSetting = new MuVarGSEMO_Setting();
                experimentSetting.init(this.problem, (double)deltaComboBox.getSelectedItem(), iterations);
                SolutionSet results = experimentSetting.run();

                results.printObjectivesToFile(filePath+"\\FUN_i_" + i);
                results.printVariablesToFile(filePath+"\\VAR_i_" + i);
            }
            System.out.println("**** end of execution ****");

        } catch (JMException|ClassNotFoundException e) {
            logTextPane.setText(e.getMessage());
            e.printStackTrace();
        }

    }
    private void runNSGA2() {
        int iterations = (int) evaluationsComboBox.getSelectedItem();
        int testRounds = (int) iterationsComboBox.getSelectedItem();
        String filePath = new Timestamp(System.currentTimeMillis()).toString();

        //filePath = ".\\test_results\\test_"+filePath; //uncomment for windows
        filePath = "/home/a1846115/test_results/test_"+filePath; //uncomment for windows
        filePath = filePath.replaceAll(":","-");
        System.out.println("\tDataset\t"+datasetName.getText()+
                "\tOutput:\t"+filePath+"\tRunning\tNSGA2");
        System.out.println("\tEvaluations\t"+iterations+"\tCapacity\t"+
                capacityTextField.getText()+"\tDelta\t"+
                deltaComboBox.getSelectedItem());

        new File(filePath).mkdirs();
        try {
            for(int i=0; i<testRounds;){
                System.out.print(++i+"\t");
                NSGAII_Setting experimentSetting = new NSGAII_Setting();
                experimentSetting.init(this.problem, (double)deltaComboBox.getSelectedItem(), iterations);
                SolutionSet results = experimentSetting.run();

                results.printObjectivesToFile(filePath+"\\FUN_i_" + i);
                results.printVariablesToFile(filePath+"\\VAR_i_" + i);
            }
            System.out.println("**** end of execution ****");

        } catch (JMException|ClassNotFoundException e) {
            logTextPane.setText(e.getMessage());
            e.printStackTrace();
        }
    }

    private Knapsack adjustWeights(Knapsack problem, int gamma)
    {
        if(gamma == 0)
            return problem;
        int[] newWeights = problem.getWeight().clone();
        int[] newProfits = problem.getProfit().clone();
        int capacity = problem.getWeightBound();

        for(int i = 0 ; i < newWeights.length ; i++ )
            newWeights[i]+= gamma;
        runAlgorithmButton.setEnabled(true);
        capacityTextField.setText(capacity+"");
        logTextPane.setText("Weights are adjusted for gamma : " + gamma + ". New weight bound : " + capacity);
        MuVar_KnapsackMO prob = new MuVar_KnapsackMO(problem.getName() );

        prob.init(newWeights.length, capacity , newProfits, newWeights );
        return prob;
    }

    private Knapsack loadProblemData(File file) throws
            IOException {
        Scanner scanner = new Scanner(file);

        String splitRegex = ":(\\t|\\s)*";
        String problemName = scanner.nextLine().split(splitRegex)[1];
        problemName += scanner.nextLine().split(splitRegex)[1];
        /*scanner.nextLine();
        scanner.nextLine();*/
        String line = scanner.nextLine();
        int skipLines = Integer.parseInt(line.split(splitRegex)[1]);
        line = scanner.nextLine();
        int numberOfItems = Integer.parseInt(line.split(splitRegex)[1]);
        line = scanner.nextLine();
        int capacity = Integer.parseInt(line.split(splitRegex)[1]);
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
        noItemsTextField.setText(numberOfItems+"");
        capacityTextField.setText(capacity+"");
        logTextPane.setText("Problem data is loaded. Number of items in the problem are : " + numberOfItems + "Weight Bound : " + capacity);
        MuVar_KnapsackMO prob = new MuVar_KnapsackMO(problemName);
        prob.init(numberOfItems, capacity, profit, weight);
        return prob;
    }

    private void initFileOpeningComponents()
    {
        datasetName.setText("Goto FILE > Open to select data file.");
        datasetName.setEditable(false);
        openMenuItem.addActionListener( e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("/home/a1846115/Projects/benchmarks"));
            //fileChooser.setCurrentDirectory(new File(".\\benchmarks")); // uncomment for windows
            fileChooser.showDialog(frame, "Select File");
            inputDataFile = fileChooser.getSelectedFile();
            datasetName.setText(inputDataFile.getName());
            loadProblemButton.setEnabled(true);
            logTextPane.setText(inputDataFile.getName() + " file is loaded.");
        });
    }

    public static void main(String[] args) {
        new NewUI();
    }

}
