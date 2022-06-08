package kokila.gecco22.com.problems.knapsack.mu_var;

import jmetal.core.Solution;
import jmetal.encodings.variable.Binary;
import kokila.gecco22.com.problems.knapsack.Knapsack;

/**
 * Class representing problem DynProfit_Knapsack
 */
public class MuVar_KnapsackMO2 extends Knapsack {
    public static final int MINUS_MU = 0;
    public static final int VAR = 1;
    private double v;
    private double vMax;

    /**
     * Creates a new Knapsack problem instance
     * @param problemName Name of the problem instance
     */
    public MuVar_KnapsackMO2(String problemName) {
        super(problemName);
        numberOfObjectives_ = 2;
    }

    private double getVMax()
    {
        if(vMax == 0.0)
            vMax = getV() * numberOfItems_;
        return vMax;
    }
    /**
     * Evaluates a solution
     * @param solution The solution to evaluate
     */
    public void evaluate(Solution solution) {

        Binary variable;
        int counter;
        int profit;
        int weight;
        double mu, var;
        //KnapsackSolution sol = (KnapsackSolution) solution;
        //for knapsack problem, the binary variable will indicate
        // whether a particular item is selected into the sol or  not
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

        if (weight > weightBound_) {
            mu = weight - weightBound_;
            //var = getVMax() + weight - weightBound_;
            var = variable.getNumberOfBits() + weight - weightBound_;
        }
        else{
            mu = -profit;
            //var = getV() * counter;
            var = counter;
        }
        solution.setObjective(MINUS_MU, mu);
        solution.setObjective(VAR, Math.sqrt(var));
    }

    public String toString()
    {
        return "Knapsack instance: capacity = " +weightBound_ + " | delta = " + delta_;
    }

} // MuVar_KnapsackMO