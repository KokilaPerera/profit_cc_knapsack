package kokila.gecco22.com.metaheuristics;

import jmetal.core.*;
import kokila.gecco22.com.problems.knapsack.Knapsack;
import jmetal.util.JMException;
import jmetal.core.Algorithm;

/**
 * Class implementing a generational genetic algorithm
 */
public abstract class OnePlusOne extends Algorithm {

    /**
     *
     * Constructor
     * Create a new GGA instance.
     * @param problem Problem to solve.
     */
    public OnePlusOne(Problem problem){
        super(problem) ;
    } // GGA

    /**
     * Execute the GGA algorithm
     * @throws JMException
     */
    public SolutionSet execute() throws JMException, ClassNotFoundException {
        int populationSize ;
        int maxEvaluations ;
        int evaluations    ;

        SolutionSet population          ;

        Operator    mutationOperator  ;

        // Read the params
        populationSize = 1 ;
        maxEvaluations = ((Integer)this.getInputParameter("maxEvaluations")).intValue();

        // Initialize the variables
        population          = new SolutionSet(populationSize) ;
        evaluations  = 1;

        // Read the operators
        mutationOperator  = this.operators_.get("mutation");

        // Create the initial population
        Solution newIndividual;
        newIndividual = new Solution(problem_);
        problem_.evaluate(newIndividual);
        evaluations++;
        population.add(newIndividual); //for

        // Sort population
        //population.sort(profitComparator) ;
        while (evaluations < maxEvaluations) {


            // Create the offspring from the parent and perform mutation
            Solution parent = population.get(0);
            Solution offspring = new Solution(parent);
            mutationOperator.execute(offspring);

            // Evaluation of the new individual
            problem_.evaluate(offspring);
            evaluations ++;

            fitnessComparison(population, parent, offspring);
        } // while

        // Return a population with the best individual
        System.out.println("Final profit: "+ population.get(0).getObjective(Knapsack.OBJ_P));
        return population ;
    } // execute
    public abstract boolean fitnessComparison(SolutionSet population, Solution parent, Solution offspring);
} // OnePlusOne