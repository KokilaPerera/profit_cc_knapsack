package kokila.gecco22.com.metaheuristics.mu_var;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.util.JMException;
import kokila.gecco22.com.metaheuristics.GSEMO;
import kokila.gecco22.com.problems.knapsack.mu_var.CCKnapsack;

import java.util.HashMap;

/**
 * This class runs a one plus one genetic algorithm (GA)
 * Problem is the chance-constrained knapsack problem
 **/
public class CC_GSEMO {

    public static void main(String [] args) throws JMException, ClassNotFoundException {
        CCKnapsack problem   ;         // The problem to solve
        Algorithm algorithm ;         // The algorithm to use
        Operator  mutation  ;         // Mutation operator
        Operator  selection  ;         // Mutation operator

        HashMap  parameters ; // Operator parameters

        problem = new CCKnapsack("Binary", "Knapsack_01.ttp");

        algorithm = new GSEMO(problem) {
            @Override
            public boolean weaklyDominates(Solution first, Solution second) {
                double firstG = first.getObjective(CCKnapsack.OBJECTIVE_G);
                double secondG = second.getObjective(CCKnapsack.OBJECTIVE_G);

                return firstG >= secondG;
            }

            @Override
            public boolean stronglyDominates(Solution first, Solution second) {

                double firstG = first.getObjective(CCKnapsack.OBJECTIVE_G);
                double secondG = second.getObjective(CCKnapsack.OBJECTIVE_G);

                return (firstG > secondG);
            }
        };
        /* Algorithm parameters*/
        algorithm.setInputParameter("maxEvaluations", 10000);

        parameters = new HashMap() ;
        parameters.put("probability", 1.0/problem.getNumberOfBits()) ;
        mutation = MutationFactory.getMutationOperator("BitFlipMutation", parameters);

        selection = SelectionFactory.getSelectionOperator("RandomSelection", null) ;

        /* Add the operators to the algorithm*/
        algorithm.addOperator("mutation",mutation);
        algorithm.addOperator("selection", selection);

        /* Execute the Algorithm */
        long initTime = System.currentTimeMillis();
        SolutionSet population = algorithm.execute();
        long estimatedTime = System.currentTimeMillis() - initTime;
        System.out.println("Total execution time: " + estimatedTime);

        /* Log messages */
        System.out.println("Objectives values have been written to file FUN");
        population.printObjectivesToFile("FUN");
        System.out.println("Variables values have been written to file VAR");
        population.printVariablesToFile("VAR");
    } //main
}
