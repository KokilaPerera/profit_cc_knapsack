package kokila.gecco22.com.problems.knapsack;

import jmetal.core.Solution;

/**
 * Class representing problem KnapsackMO.
 */
public class DynW_KnapsackMO extends Knapsack {
    public static final int OBJECTIVE_G1 = 3;
    public static final int OBJECTIVE_G2 = 4;
    /**
     * Creates a new Knapsack problem instance
     * @param file_data Path of the problem data
     */
    public DynW_KnapsackMO(String solutionType, String file_data) {
        super(solutionType, file_data);
        problemName_ = "DynamicWeightKnapsack_Multi-Objective";

    } // KnapsackMO

    /**
     * Evaluates a solution
     * @param solution The solution to evaluate
     */
    public void evaluate(Solution solution) {
        super.evaluate(solution);
        int weight = (int) solution.getObjective(OBJECTIVE_W);
        int profit = (int) solution.getObjective(OBJECTIVE_P);
        int counter = (int) solution.getObjective(OBJECTIVE_C);

        double g1 = (weight < weightBound_)? chebyshevProbability(counter, weight, weightBound_, 0, delta_) : 1 + weight - weightBound_;
        double g2 = (g1 > alpha_) ? 1 : profit; //TODO read alpha, delta from as an input
        solution.setObjective(OBJECTIVE_G1, g1);
        solution.setObjective(OBJECTIVE_G2, g2);
    } // evaluate

} // DynamicWeightKnapsack
