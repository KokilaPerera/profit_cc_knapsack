package kokila.gecco22.com.problems.knapsack.mu_var;

import jmetal.core.Solution;
import jmetal.encodings.variable.Binary;

/**
 * Class representing problem DynProfit_Knapsack
 */
public class CCKnapsack extends MuVar_KnapsackMO {
    public static final int OBJECTIVE_G = 5;
    /**
     * Creates a new Knapsack problem instance
     * @param file_data Path of the problem data
     */
    public CCKnapsack(String solutionType, String file_data) {
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

        //TODO change the static value of variance

        if (weight > weightBound_) {
            double diff = weight - weightBound_;
            //to penalize the profit needs to be reduced; variance need to be increased
            double mu = profit / (diff);
            //TODO calculate correct fractile-point
            double K_alpha = 1.0;
            double g = profit + (K_alpha * counter * 0.99);
            solution.setObjective(OBJECTIVE_MU,  mu);
            solution.setObjective(OBJECTIVE_VAR, diff * (counter + 0.99));
            solution.setObjective(OBJECTIVE_G, g);

        }
        else{
            //TODO calculate correct fractile-point
            double K_alpha = 1.0;
            double g = profit + (K_alpha * counter * 0.99);
            solution.setObjective(OBJECTIVE_MU, profit);
            solution.setObjective(OBJECTIVE_VAR, 0.99);
            solution.setObjective(OBJECTIVE_G, g);
        }

        solution.setObjective(OBJECTIVE_P, profit);
        solution.setObjective(OBJECTIVE_W, weight);
        solution.setObjective(OBJECTIVE_C, counter);
    } // evaluate

} // DynProfit_Knapsack