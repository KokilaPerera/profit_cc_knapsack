package kokila.gecco22.com.problems.knapsack.mu_var;

import jmetal.core.Solution;
import jmetal.encodings.variable.Binary;
import kokila.gecco22.com.problems.knapsack.Knapsack;

/**
 * Class representing problem DynProfit_Knapsack
 */
public class MuVar_KnapsackMO extends Knapsack {
    public static final int OBJECTIVE_MU = 3;
    public static final int OBJECTIVE_VAR = 4;
    /**
     * Creates a new Knapsack problem instance
     * @param file_data Path of the problem data
     */
    public MuVar_KnapsackMO(String solutionType, String file_data) {
        super(solutionType, file_data);
        problemName_ = "Dynamic Profit Knapsack - Multi Objective";

    } // KnapsackMO

    /**
     * Evaluates a solution
     * @param solution The solution to evaluate
     */
    public void evaluate(Solution solution) {
        Binary variable;
        int counter = 0;
        int profit = 0;
        int weight = 0;

        double var = delta_ * delta_ / 12.0;//TODO recheck

        //for knapsack problem, the binary variable will indicate
        // whether a particular item is selected into the solution or  not
        variable = ((Binary) solution.getDecisionVariables()[0]);

        for (int i = 0; i < variable.getNumberOfBits(); i++){
            if (variable.bits_.get(i)) {
                counter++;
                profit += this.profit[i];
                weight += this.weight[i];
            }
        }

        if (weight > weightBound_) {
            double diff = weight - weightBound_;
            //to penalize the profit needs to be reduced; variance need to be increased
            double mu = profit / (diff);
            solution.setObjective(OBJECTIVE_MU,  mu);
            solution.setObjective(OBJECTIVE_VAR, diff * var * counter);
        }
        else{
            solution.setObjective(OBJECTIVE_MU, profit);
            solution.setObjective(OBJECTIVE_VAR, var * counter);//TODO  0.99 * counter / 3
        }

        solution.setObjective(OBJECTIVE_P, profit);
        solution.setObjective(OBJECTIVE_W, weight);
        solution.setObjective(OBJECTIVE_C, counter);
    } // evaluate

} // MuVar_KnapsackMO