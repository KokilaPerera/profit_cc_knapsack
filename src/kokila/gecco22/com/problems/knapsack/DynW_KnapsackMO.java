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
     */
    public DynW_KnapsackMO() {
        super();
    }

    /**
     * Evaluates a solution
     * @param solution The solution to evaluate
     */
    public void evaluate(Solution solution) {
        super.evaluate(solution);
        int weight = (int) solution.getObjective(OBJ_W);
        int profit = (int) solution.getObjective(OBJ_P);
        int counter = (int) solution.getObjective(OBJ_C);

        double g1 = (weight < weightBound_)? chebyshevProbability(counter, weight, weightBound_, 0, delta_) : 1 + weight - weightBound_;
        double g2 = (g1 > alpha_) ? 1 : profit; //TODO read alpha, delta from as an input
        solution.setObjective(OBJECTIVE_G1, g1);
        solution.setObjective(OBJECTIVE_G2, g2);
    } // evaluate

} // DynamicWeightKnapsack
