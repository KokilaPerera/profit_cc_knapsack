package kokila.gecco22.com.problems.knapsack.mu_var;

import jmetal.core.Solution;
import jmetal.encodings.variable.Binary;
import kokila.gecco22.com.problems.knapsack.Knapsack;

/**
 * Class representing problem DynProfit_Knapsack
 */
public class MuC_KnapsackMO extends Knapsack {
    public static final int MU = 0;
    public static final int Csqrt = 1;

    private double vMax;

    /**
     * Creates a new Knapsack problem instance
     * @param problemName Name of the problem instance
     */
    public MuC_KnapsackMO(String problemName) {
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

        //super.evaluate(solution);
        int counter;
        double profit, weight;
        double mu, var;
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

        if (weight > weightBound_) {
            mu = weightBound_ - weight;
            //var = getVMax() + weight - weightBound_;
            var = variable.getNumberOfBits() + weight - weightBound_;
        }
        else{
            mu = profit;
            var = counter;//getV() * counter;
        }
        solution.setObjective(MU, mu);
        solution.setObjective(Csqrt, Math.sqrt(var));
    }

    public String toString()
    {
        return "Knapsack instance: capacity = " +weightBound_ + " | delta = " + delta_;
    }

} // MuVar_KnapsackMO