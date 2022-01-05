package kokila.gecco22.com.metaheuristics;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.util.JMException;
import jmetal.util.comparators.ObjectiveComparator;
import kokila.gecco22.com.problems.knapsack.MOKnapsack;

import java.util.HashMap;

/**
 * This class runs a one plus one genetic algorithm (GA)
 * Problem is the chance constrained knapsack problem
 **/
public class GSEMO_main {

    public static void main(String [] args) throws JMException, ClassNotFoundException {
        MOKnapsack problem   ;         // The problem to solve
        Algorithm algorithm ;         // The algorithm to use
        Operator  mutation  ;         // Mutation operator\
        Operator  selection  ;         // Mutation operator\

        //int bits ; // Length of gene sequence (genome) in evolutionary problem
        HashMap  parameters ; // Operator parameters


        // number of bits = length of problem
        problem = new MOKnapsack("Binary", "Knapsack_01.ttp");

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
        /* Algorithm parameters*/
        algorithm.setInputParameter("maxEvaluations", 100000);

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
} // OnePlusOne_main
