package kokila.gecco22.com.problems.knapsack.mu_var;

import jmetal.core.Solution;
import jmetal.encodings.variable.Binary;
import kokila.gecco22.com.problems.knapsack.Knapsack;

/**
 * Class representing problem DynProfit_Knapsack
 */
public class MuProb_KnapsackMO extends Knapsack {
    public static final int PROFIT = 0;
    public static final int WEIGHT_EXCESS = 1;
    public static final int COUNTER = 2;

    private double vMax;

    /**
     * Creates a new Knapsack problem instance
     * @param problemName Name of the problem instance
     */
    public MuProb_KnapsackMO(String problemName) {
        super(problemName);
        numberOfObjectives_ = 3;

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

        double alpha = this.alpha_;
        //super.evaluate(solution);
        int counter;
        double profit, weight;
        double mu, var, prob;
        Binary variable = ((Binary) solution.getDecisionVariables()[0]);
        counter = variable.bits_.cardinality();
        profit = 0;
        weight = 0;

        for (int i = 0; i < variable.getNumberOfBits(); i++){
            if (variable.bits_.get(i)) {
                profit += this.profit[i];
                weight += this.weight[i];
            }
        }

        mu = profit;
        mu -= Math.sqrt( getV() * counter * (1-alpha/alpha));
        if (weight > weightBound_) {
            prob = weight - weightBound_;
        }
        else{ // if weight is within the capacity bound
            prob = 0;
        }
        solution.setObjective(PROFIT, mu);
        solution.setObjective(WEIGHT_EXCESS, prob);
        solution.setObjective(COUNTER, counter);
    }

    public String toString()
    {
        return "Knapsack instance: capacity = " +weightBound_ + " | delta = " + delta_;
    }

} // MuVar_KnapsackMO