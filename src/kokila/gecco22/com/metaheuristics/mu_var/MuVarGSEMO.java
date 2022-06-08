package kokila.gecco22.com.metaheuristics.mu_var;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import kokila.gecco22.com.metaheuristics.GSEMO;
import kokila.gecco22.com.metaheuristics.mu_var.util.PlotListener;

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



    protected void addListener(PlotListener listener)
    {
        listenerList.add(listener);
    }


}