package kokila.gecco22.com.metaheuristics;

import jmetal.core.*;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.comparators.ObjectiveComparator;
import kokila.gecco22.com.problems.knapsack.Knapsack;
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
        int maxEvaluations = ((Integer)this.getInputParameter("maxEvaluations")).intValue();
        int populationSize = maxEvaluations;//TODO assign correct value
        int evaluations = 0;

        // Initialize the variables
        SolutionSet population          = new SolutionSet(populationSize) ;

        // Read the operators
        Operator mutationOperator  = this.operators_.get("mutation");

        // Create the initial population
        Solution newIndividual = new Solution(problem_);
        new Solution().setDecisionVariables(new Variable[]{});
        problem_.evaluate(newIndividual);
        evaluations ++;
        population.add(newIndividual);

        while (evaluations < maxEvaluations) {

            // Create the offspring from a random parent from the population
            Solution parent = population.get(PseudoRandom.randInt(0,population.size()-1));
            Solution offspring = new Solution(parent);
            SolutionSet newPopulation = new SolutionSet(populationSize) ;

            //mutate the offspring
            mutationOperator.execute(offspring);

            // Evaluation of the new individual
            problem_.evaluate(offspring);evaluations ++;

            Iterator<Solution> it = population.iterator();

            boolean dominantSolutionsExists = false;
            while (it.hasNext()) {
                //check if there exist a parent that dominates offspring
                Solution w = it.next();
                int x = (int) w.getObjective(Knapsack.OBJECTIVE_C);
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
                while(it.hasNext()){
                    Solution z = it.next();
                    if(!weaklyDominates(offspring,z)){
                        newPopulation.add(z);
                        //System.out.println( evaluations +":"+z.getObjective(Knapsack.OBJECTIVE_W));
                    }
                }
                population.clear();
                it = newPopulation.iterator();
                while (it.hasNext()){
                    Solution s = it.next();
                    population.add(s);
                    //System.out.print(", " +  s.getObjective(Knapsack.OBJECTIVE_W));
                }

                if (evaluations % 1000 == 0)
                {
                    plot(evaluations, population, "Variance", MuVar_KnapsackMO.OBJECTIVE_VAR, "Mu", MuVar_KnapsackMO.OBJECTIVE_MU);
                    //for(int time=0; time<100000;time++);//wait for few moments
                }
                newPopulation.clear();
            }
            //System.out.println();

        } // while

        // Return a population with the best individual

        System.out.println("Final population size: " + population.size());

        System.out.println("Final profit: "+ population.get(0).getObjective(Knapsack.OBJECTIVE_P));
        System.out.println("Final weight: "+ population.get(0).getObjective(Knapsack.OBJECTIVE_W));
        population.sort(new ObjectiveComparator(Knapsack.OBJECTIVE_P,true));

        System.out.println("Final profit2: "+ population.get(0).getObjective(Knapsack.OBJECTIVE_P));
        System.out.println("Final weight2: "+ population.get(0).getObjective(Knapsack.OBJECTIVE_W));
        return population ;
    }// execute

    //check whether the first dominates the second
    public abstract boolean weaklyDominates(Solution first, Solution second);
    public abstract boolean stronglyDominates(Solution first, Solution second);

    protected void plot(int iteration, SolutionSet population, String labelX, int objectiveX, String labelY, int objectiveY)
    {
        return;
    }
} // OnePlusOne