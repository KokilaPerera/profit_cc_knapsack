package kokila.gecco22.com.problems.knapsack;

import jmetal.core.Solution;

/**
 * Class representing problem KnapsackMO.
 */
public class DynW_KnapsackSO extends Knapsack {
    public static final int OBJECTIVE_U = 3;
    public static final int OBJECTIVE_V = 4;
    /**
     * Creates a new Knapsack problem instance
     * @param file_data Path of the problem data
     */
    public DynW_KnapsackSO(String solutionType, String file_data) {
        super(solutionType, file_data);
        problemName_ = "DynamicWeightKnapsack_Single-Objective";

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

        double U = weightBound_ - weight;
        if (U < 0)
            U = 0;

        double V = chebyshevProbability(counter, weight, weightBound_, alpha_, delta_) ;
        if (V < 0)
            V = 0;
        solution.setObjective(OBJECTIVE_U, U);
        solution.setObjective(OBJECTIVE_V, V);
    } // evaluate

} // DynamicWeightKnapsack
