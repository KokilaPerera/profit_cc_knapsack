package kokila.gecco22.com.metaheuristics;

import jmetal.core.*;
import jmetal.operators.mutation.MutationFactory;
import jmetal.util.JMException;
import jmetal.util.comparators.ObjectiveComparator;
import kokila.gecco22.com.problems.knapsack.DynW_KnapsackMO;
import kokila.gecco22.com.problems.knapsack.DynW_KnapsackSO;
import kokila.gecco22.com.problems.knapsack.Knapsack;

import java.util.HashMap;

/**
 * This class runs a one plus one genetic algorithm (GA)
 * Problem is the chance constrained knapsack problem
 **/
public class OnePlusOne_main {

    public static void main(String [] args) throws JMException, ClassNotFoundException {
        Knapsack problem   ;         // The problem to solve
        Algorithm algorithm ;         // The algorithm to use
        Operator  mutation  ;         // Mutation operator\

        //int bits ; // Length of gene sequence (genome) in evolutionary problem
        HashMap  parameters ; // Operator parameters


        // number of bits = length of problem
        problem = new DynW_KnapsackMO("Binary", "Knapsack_02.ttp");

        ObjectiveComparator comparator_U = new ObjectiveComparator(DynW_KnapsackSO.OBJECTIVE_U);
        ObjectiveComparator comparator_V = new ObjectiveComparator(DynW_KnapsackSO.OBJECTIVE_V);
        ObjectiveComparator comparator_P = new ObjectiveComparator(Knapsack.OBJECTIVE_P);
        algorithm = new OnePlusOne(problem) {
            @Override
            public boolean fitnessComparison(SolutionSet population, Solution parent, Solution offspring) {
                int u = comparator_U.compare(offspring,parent);
                int v = comparator_V.compare(offspring,parent);
                int p = comparator_P.compare(offspring,parent);
                boolean fitterOffspring = false;
                if  ( u < 0 || ( u == 0 && v < 0) || (u == 0 && v == 0 && p > 0 ))
                {
                    population.clear();
                    population.add(offspring);
                    fitterOffspring = true;
                    //System.out.println(offspring.getDecisionVariables()[0]);
                }
                return fitterOffspring;
            }
        };
        /* Algorithm parameters*/
        algorithm.setInputParameter("populationSize",1);
        algorithm.setInputParameter("maxEvaluations", 100000);

        parameters = new HashMap() ;
        parameters.put("probability", 1.0/problem.getNumberOfBits()) ;
        mutation = MutationFactory.getMutationOperator("BitFlipMutation", parameters);

        /* Add the operators to the algorithm*/
        algorithm.addOperator("mutation",mutation);

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
