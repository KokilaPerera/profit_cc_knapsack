package kokila.gecco22.com.problems.knapsack;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.variable.Binary;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Class representing problem OneMax. The problem consist of maximizing the
 * number of '1's in a binary string.
 */
public class Knapsack extends Problem {

    public static final int OBJECTIVE_P = 0;
    public static final int OBJECTIVE_W = 1;
    public static final int OBJECTIVE_C = 2;
    protected double delta_ = 50;
    protected double alpha_ = 0.1;


    /**
     * Stores the number of variables of the problem
     */
    protected int weightBound_;

    /**
     * Stores the number of items of the problem
     */
    protected int numberOfItems_ ;

    /**
     * Stores the profits of items of the problem
     */
    protected int[] profit ;

    /**
     * Stores the weights of items of the problem
     */
    protected int[] weight ;

    /**
     * Creates a new Knapsack problem instance
     * @param file_data Path of the problem data
     */
    public Knapsack(String solutionType, String file_data) {
        this.numberOfVariables_  = 1;
        numberOfObjectives_ = 5;
        numberOfConstraints_= 1;
        problemName_ = "Knapsack";

        solutionType_ = new BinarySolutionType(this) ;
        try {
            readProblem(file_data);
        } catch (IOException e) {
            e.printStackTrace();
        }



        length_ = new int[numberOfVariables_];
        length_[0] = numberOfItems_;

        if (solutionType.compareTo("Binary") == 0)
            solutionType_ = new BinarySolutionType(this) ;
        else {
            System.out.println("Knapsack Problems: solution type " + solutionType + " invalid") ;
            System.exit(-1) ;
        }

    } // OneMax

    /**
     * Evaluates a solution
     * @param solution The solution to evaluate
     */
    public void evaluate(Solution solution) {
        Binary variable;
        int counter;
        int profit;
        int weight;

        //for knapsack problem, the binary variable will indicate
        // whether a particular item is selected into the solution or  not
        variable = ((Binary) solution.getDecisionVariables()[0]);

        counter = 0;
        profit = 0;
        weight = 0;

        for (int i = 0; i < variable.getNumberOfBits(); i++){
            if (variable.bits_.get(i)) {
                counter++;
                profit += this.profit[i];
                weight += this.weight[i];
            }
        }
        solution.setObjective(OBJECTIVE_C, counter);
        solution.setObjective(OBJECTIVE_W, weight);
        solution.setObjective(OBJECTIVE_P, profit);

    } // evaluate

    // calculate the chance of constraint violation using Chebyshev method
    double chebyshevProbability(int count, int value, int constraint, double alpha, double delta)
    {
        //TODO implement chernoff bound later
        double temp1 = delta * delta * count;
        double temp2 = constraint - value;
        temp2 = 3 * temp2 * temp2;
        double chance = temp1 / (temp1 + temp2);
        chance -= alpha;
        if (  chance > 0 ) {
            return BigDecimal.valueOf(chance).setScale(8, RoundingMode.HALF_EVEN).doubleValue();
        }
        return 0;
    }

    public double getCCViolation(int count, int weight)
    {
        //TODO implement chernoff bound later
        double temp1 = delta_ * delta_ * count;
        double temp2 = weightBound_ - weight;
        temp2 = 3 * temp2 * temp2;
        double chance = temp1 / (temp1 + temp2);
        //chance -= this.alpha_;
        if (  chance > 0 ) {
            return BigDecimal.valueOf(chance).setScale(8, RoundingMode.HALF_EVEN).doubleValue();
        }
        return 0;
    }

    public void readProblem(String file) throws
            IOException {

        Reader inputFile = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

        StreamTokenizer token = new StreamTokenizer(inputFile);
        try {
            boolean found = false ;

            token.nextToken();
            while(!found) {
                if ((token.sval != null) && ((token.sval.compareTo("NUMBER") == 0)))
                    found = true ;
                else
                    token.nextToken() ;
            } // while

            token.nextToken() ;
            token.nextToken() ;
            token.nextToken();
            token.nextToken();
            numberOfItems_ =  (int)token.nval ;
            found = false ;
            token.nextToken();
            while(!found) {
                if ((token.sval != null) &&
                        ((token.sval.compareTo("CAPACITY") == 0)))
                    found = true ;
                else
                    token.nextToken() ;
            } // while

            token.nextToken() ;
            token.nextToken() ;
            token.nextToken() ;
            token.nextToken() ;
            weightBound_ =  (int)token.nval ;
            token.nextToken() ;
            token.nextToken() ;
            weight = new int[numberOfItems_];
            profit = new int[numberOfItems_];

            for (int i = 0 ; i < numberOfItems_ ; i++){
                token.nextToken();
                profit[i] = (int) token.nval;
                token.nextToken();
                weight[i] = (int) token.nval + 100;
            }

        } // try
        catch (Exception e) {
            System.err.println ("Knapsack.readProblem(): error when reading data file "+e);
            e.printStackTrace();
            System.exit(1);
        } // catch
    } // readProblem


    public void setDelta_(double delta_) {
        this.delta_ = delta_;
    }
    public void setWeightBound_(int weightBound_) {
        this.weightBound_ = weightBound_;
    }

    public String toString()
    {
        return "Knapsack instance: capacity = " +weightBound_ + " | var = " + delta_ + " | alpha = " + alpha_;
    }

} // OnePlusOne
