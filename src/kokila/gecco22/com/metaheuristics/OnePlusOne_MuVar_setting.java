package kokila.gecco22.com.metaheuristics;

import jmetal.core.Operator;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.operators.mutation.MutationFactory;
import jmetal.util.JMException;
import kokila.gecco22.com.problems.knapsack.mu_var.MuProb_KnapsackMO;
import kokila.gecco22.com.problems.knapsack.Knapsack;

import java.util.HashMap;

/**
 * This class runs a one plus one genetic algorithm (GA)
 * Problem is the chance constrained knapsack problem
 **/
public class OnePlusOne_MuVar_setting extends ExperimentSetting{

    public void init(Knapsack prob, double delta, int iterations) throws JMException {

        Operator  mutation  ;         // Mutation operator\

        //int bits ; // Length of gene sequence (genome) in evolutionary problem
        HashMap  parameters ; // Operator parameters


        // number of bits = length of problem
        problem = new MuProb_KnapsackMO(prob.getName());
        problem.init(prob.getLength(0), prob.getWeightBound(), prob.getProfit(), prob.getWeight());
        problem.setDelta(delta);
        problem.setAlpha(0.001);
        this.delta = delta;

        algorithm = new OnePlusOne(problem) {
            @Override
            public boolean fitnessComparison(SolutionSet population, Solution parent, Solution offspring) {
                boolean fitterOffspring = false;
                double parentP, offspringP, parentW, offspringW;
                parentP = parent.getObjective(MuProb_KnapsackMO.PROFIT);
                offspringP = offspring.getObjective(MuProb_KnapsackMO.PROFIT);
                parentW = parent.getObjective(MuProb_KnapsackMO.WEIGHT_EXCESS);
                offspringW = offspring.getObjective(MuProb_KnapsackMO.WEIGHT_EXCESS);
                if(offspringW < parentW || (offspringW == parentW && offspringP > parentP)){
                    population.clear();
                    population.add(offspring);
                    fitterOffspring = true;
                    System.out.println(offspringP+","+offspringW+","+offspring.getObjective(MuProb_KnapsackMO.COUNTER));
                }
                return fitterOffspring;
            }
        };
        /* Algorithm parameters*/
        algorithm.setInputParameter("populationSize",1);
        algorithm.setInputParameter("maxEvaluations", iterations);

        parameters = new HashMap() ;
        parameters.put("probability", 1.0/problem.getNumberOfBits()) ;
        mutation = MutationFactory.getMutationOperator("BitFlipMutation", parameters);
        algorithm.addOperator("mutation",mutation);
    } //main

    @Override
    protected void logResults(SolutionSet population) {
        System.out.println("------");
        double mu, var, p;
        mu = population.get(0).getObjective(MuProb_KnapsackMO.PROFIT);
        var = population.get(0).getObjective(MuProb_KnapsackMO.WEIGHT_EXCESS);
        System.out.println("Final solution (Alpha, P, W) : "+problem.getAlpha()+","+mu+","+var);
    }
} // OnePlusOne_main
