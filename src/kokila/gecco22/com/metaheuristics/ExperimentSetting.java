package kokila.gecco22.com.metaheuristics;

import jmetal.core.Algorithm;
import jmetal.core.SolutionSet;
import jmetal.util.JMException;
import kokila.gecco22.com.problems.knapsack.Knapsack;

public abstract class ExperimentSetting {
    protected Algorithm algorithm;
    protected Knapsack problem;
    protected double delta;
    public SolutionSet run() throws JMException, ClassNotFoundException {
        if(algorithm == null)
        {
            throw new JMException("Initialize the algorithm first ... ");
        }

        /* Execute the Algorithm */
        long initTime = System.currentTimeMillis();
        SolutionSet population = algorithm.execute();
        logResults(population);
        long estimatedTime = System.currentTimeMillis() - initTime;
        //System.out.println("Execution time:\t" + estimatedTime + "\tms");
        System.out.println(estimatedTime);

        return population;

    }

    protected abstract void logResults(SolutionSet population);
}
