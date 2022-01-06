package kokila.gecco22.com.metaheuristics.mu_var;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import kokila.gecco22.com.metaheuristics.GSEMO;

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

    protected void addListener(PlotListener listener)
    {
        listenerList.add(listener);
    }
}