package kokila.gecco22.com.metaheuristics.mu_var;

import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.util.JMException;
import kokila.gecco22.com.problems.knapsack.mu_var.MuVar_KnapsackMO;

import java.util.HashMap;

/**
 * This class runs a one plus one genetic algorithm (GA)
 * Problem is the chance-constrained knapsack problem
 **/
public class MuVarGSEMO_main {

    public static void run(PlotListener plotListener) throws JMException, ClassNotFoundException {
        MuVar_KnapsackMO problem   ;         // The problem to solve
        Algorithm algorithm ;         // The algorithm to use
        Operator  mutation  ;         // Mutation operator\
        Operator  selection  ;         // Mutation operator\

        //int bits ; // Length of gene sequence (genome) in evolutionary problem
        HashMap  parameters ; // Operator parameters


        // number of bits = length of problem
        problem = new MuVar_KnapsackMO("Binary", "Knapsack_01.ttp");

        algorithm = new MuVarGSEMO(problem) {
            @Override
            public boolean weaklyDominates(Solution first, Solution second) {
                double firstMean = first.getObjective(MuVar_KnapsackMO.OBJECTIVE_MU);
                double secondMean = second.getObjective(MuVar_KnapsackMO.OBJECTIVE_MU);
                double firstVar = first.getObjective(MuVar_KnapsackMO.OBJECTIVE_VAR);
                double secondVar = second.getObjective(MuVar_KnapsackMO.OBJECTIVE_VAR);

                return firstMean >= secondMean && firstVar <= secondVar;
            }

            @Override
            public boolean stronglyDominates(Solution first, Solution second) {

                double firstMean = first.getObjective(MuVar_KnapsackMO.OBJECTIVE_MU);
                double secondMean = second.getObjective(MuVar_KnapsackMO.OBJECTIVE_MU);
                double firstVar = first.getObjective(MuVar_KnapsackMO.OBJECTIVE_VAR);
                double secondVar = second.getObjective(MuVar_KnapsackMO.OBJECTIVE_VAR);

                return (firstMean >= secondMean && firstVar <= secondVar) &&
                        (firstMean > secondMean || firstVar < secondVar);
            }

        };
        /* Algorithm parameters*/
        algorithm.setInputParameter("maxEvaluations", 100000);
        ((MuVarGSEMO) algorithm).addListener(plotListener);
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
