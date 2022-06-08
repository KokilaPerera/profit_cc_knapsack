package kokila.gecco22.com.problems.knapsack;

import jmetal.core.Solution;

/**
 * Class representing problem KnapsackMO.
 */
public class MOKnapsack extends Knapsack {
    public static final int OBJECTIVE_G1 = 3;
    public static final int OBJECTIVE_G2 = 4;

    public MOKnapsack(int numberOfItems_, int weightBound_, int[] profit, int[] weight) {
        super(numberOfItems_, weightBound_, profit, weight);
    }
    /**
     * Creates a new Knapsack problem instance
     * @param problemName NAme of the problem data
     */
    public MOKnapsack(String problemName) {
        super(problemName);

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

        double g1 = (weight < weightBound_)? chebyshevProbability(counter, weight, weightBound_, alpha_,
                delta_) : 1 + weight - weightBound_;
        double g2 = (g1 > alpha_) ? -1 : profit;
        solution.setObjective(OBJECTIVE_G1, g1);
        solution.setObjective(OBJECTIVE_G2, g2);
    } // evaluate

} // MOKnapsack
