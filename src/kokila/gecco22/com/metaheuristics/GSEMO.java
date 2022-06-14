package kokila.gecco22.com.metaheuristics;

import jmetal.core.*;
import jmetal.encodings.variable.Binary;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.comparators.ObjectiveComparator;
import kokila.gecco22.com.problems.knapsack.mu_var.MuVar_KnapsackMO;

import java.util.Iterator;

/**
 * Class implementing a generational genetic algorithm
 */
public abstract class GSEMO extends Algorithm {

    /**
     *
     * Constructor
     * Create a new GGA instance.
     * @param problem Problem to solve.
     */
    public GSEMO(Problem problem){
        super(problem) ;
    } // GGA

    /**
     * Execute the GGA algorithm
     * @throws JMException
     */
    public SolutionSet execute() throws JMException, ClassNotFoundException {
        // Read the params
        int maxEvaluations = (Integer) this.getInputParameter("maxEvaluations");
        int populationSize = (Integer) this.getInputParameter("populationSize");
        int evaluations = 0;

        // Initialize the variables
        SolutionSet population = new SolutionSet(populationSize) ;

        // Read the operators
        Operator mutationOperator = this.operators_.get("mutation");

        // Create the initial population
        Solution newIndividual = new Solution(problem_);
        problem_.evaluate(newIndividual);
        evaluations ++;
        population.add(newIndividual);

        while (evaluations < maxEvaluations) {

            // Create the offspring from a random parent from the population
            Solution parent = population.get(PseudoRandom.randInt(0,population.size()-1));
            Solution offspring = new Solution(parent);

            SolutionSet newPopulation = new SolutionSet(populationSize) ;

            mutationOperator.execute(offspring);

            // Evaluation of the new individual
            problem_.evaluate(offspring);
            evaluations ++;

            Iterator<Solution> it = population.iterator();

            boolean dominantSolutionsExists = false;
            while (it.hasNext()) {
                //check if there exist a parent that dominates offspring
                Solution w = it.next();
                if(stronglyDominates(w,offspring))
                {
                    dominantSolutionsExists = true;
                    break;
                }
            }

            if (!dominantSolutionsExists) // if none in the population dominates y
            {
                newPopulation.add(offspring);
                it = population.iterator();
                while(it.hasNext())
                {
                    Solution z = it.next();
                    if(!weaklyDominates(offspring,z))
                    {
                        newPopulation.add(z);
                    }
                }
                population.clear();
                it = newPopulation.iterator();
                while (it.hasNext())
                {
                    Solution s = it.next();
                    population.add(s);
                }
                newPopulation.clear();
                //population = sanitize(population);
            }
        }
        return population ;
    }// execute

    //check whether the first dominates the second
    public abstract boolean weaklyDominates(Solution first, Solution second);
    public abstract boolean stronglyDominates(Solution first, Solution second);

    protected SolutionSet sanitize(SolutionSet population)
    {
        return population;
    }


} // OnePlusOne