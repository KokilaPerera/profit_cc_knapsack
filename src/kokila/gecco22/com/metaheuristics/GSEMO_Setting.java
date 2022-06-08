package kokila.gecco22.com.metaheuristics;

import jmetal.core.Operator;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.operators.mutation.MutationFactory;
import jmetal.util.JMException;
import jmetal.util.comparators.ObjectiveComparator;
import kokila.gecco22.com.problems.knapsack.Knapsack;
import kokila.gecco22.com.problems.knapsack.MOKnapsack;

import java.util.HashMap;

/**
 * This class runs a one plus one genetic algorithm (GA)
 * Problem is the chance constrained knapsack problem
 **/
public class GSEMO_Setting extends ExperimentSetting {

    public void init(Knapsack knapsack, double delta, int iterations) throws JMException {
                 // The problem to solve
        Operator  mutation  ;         // Mutation operator\

        HashMap  parameters ; // Operator parameters

        problem = new MOKnapsack(knapsack.getName());
        problem.init(knapsack.getWeight().length, knapsack.getWeightBound(), knapsack.getProfit().clone(), knapsack.getWeight().clone());
        problem.setDelta(delta);

        ObjectiveComparator comparator_g1 = new ObjectiveComparator(MOKnapsack.OBJECTIVE_G1);
        ObjectiveComparator comparator_g2 = new ObjectiveComparator(MOKnapsack.OBJECTIVE_G2);
        algorithm = new GSEMO(problem) {

            @Override
            public boolean weaklyDominates(Solution first, Solution second) {
                int g1 = comparator_g1.compare(first,second);
                int g2 = comparator_g2.compare(first,second);
                return  ( g1 <= 0 && g2 >= 0 );
            }

            @Override
            public boolean stronglyDominates(Solution first, Solution second) {
                // MOKnapsack does not use stronglyDominated operation
                return weaklyDominates(first, second);
            }
        };

        algorithm.setInputParameter("maxEvaluations", iterations);
        algorithm.setInputParameter("populationSize", 100);

        parameters = new HashMap();
        parameters.put("probability", 1.0/problem.getNumberOfBits()) ;
        mutation = MutationFactory.getMutationOperator("BitFlipMutation", parameters);
        algorithm.addOperator("mutation",mutation);

    }

    @Override
    protected void logResults(SolutionSet population) {

        population.sort(new ObjectiveComparator(Knapsack.OBJ_P,true));
        Solution finalSolution = population.get(0);
        System.out.println(problem);
        System.out.println(finalSolution);
        System.out.println("Final profit: (max) "+ finalSolution.getObjective(Knapsack.OBJ_P));
    }
}
