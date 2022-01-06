package kokila.gecco22.com.metaheuristics.mu_var;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import kokila.gecco22.com.metaheuristics.GSEMO;
import kokila.gecco22.com.problems.knapsack.Knapsack;
import kokila.gecco22.com.problems.knapsack.mu_var.MuVar_KnapsackMO;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public abstract class MuVarGSEMO extends GSEMO {
    private List<PlotListener> listenerList = new LinkedList<>();

    /**
     * Constructor
     * Create a new GGA instance.
     *
     * @param problem Problem to solve.
     */
    public MuVarGSEMO(Problem problem) {
        super(problem);
    }

    protected void plot(int iteration, SolutionSet population, String labelX, int objectiveX, String labelY, int objectiveY)
    {
        double[] x = new double[population.size()];
        double[] y = new double[population.size()];

        Iterator<Solution> iter = population.iterator();
        for (int i = 0; iter.hasNext(); i++ ) {
            Solution s = iter.next();
            x[i] = s.getObjective(objectiveX);
            y[i] = s.getObjective(objectiveY);
        }

        listenerList.forEach(listener -> listener.plot(iteration, labelX, x, labelY, y));

    }

    @Override
    protected void log(Solution solution) {
        System.out.println("Final profit: (max) "+ solution.getObjective(Knapsack.OBJECTIVE_P));
        System.out.println("Final weight: "+ solution.getObjective(Knapsack.OBJECTIVE_W));

        double mu = solution.getObjective(MuVar_KnapsackMO.OBJECTIVE_MU);
        double count = solution.getObjective(MuVar_KnapsackMO.OBJECTIVE_C);
        double var = solution.getObjective(MuVar_KnapsackMO.OBJECTIVE_VAR);
        double alpha = 0.001;
        int[] v = new int[3];
        v[0] = (int) (mu - Math.sqrt(var*(1-alpha)/alpha));
        alpha = 0.01;
        v[1] = (int) (mu - Math.sqrt(var*(1-alpha)/alpha));
        alpha = 0.1;
        v[2] = (int) (mu - Math.sqrt(var*(1-alpha)/alpha));
        System.out.println("CC Violation (chebyshev) (alpha = 0.001, 0.01, 0.1): " + Arrays.toString(v));

        double delta = ((MuVar_KnapsackMO) problem_).getDelta_();
        v[0] = (int) (mu - Math.sqrt(Math.log(1/alpha)*3*delta*count));
        alpha = 0.01;
        v[1] = (int) (mu - Math.sqrt(Math.log(1/alpha)*3*delta*count));
        alpha = 0.1;
        v[2] = (int) (mu - Math.sqrt(Math.log(1/alpha)*3*delta*count));
        System.out.println("CC Violation (chernoff) (alpha = 0.001, 0.01, 0.1): " + Arrays.toString(v));

    }

    protected void addListener(PlotListener listener)
    {
        listenerList.add(listener);
    }
}