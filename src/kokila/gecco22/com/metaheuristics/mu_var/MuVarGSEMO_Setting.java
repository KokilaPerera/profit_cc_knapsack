package kokila.gecco22.com.metaheuristics.mu_var;

import jmetal.core.Operator;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.encodings.variable.Binary;
import jmetal.operators.mutation.MutationFactory;
import jmetal.util.JMException;
import jmetal.util.comparators.ObjectiveComparator;
import kokila.gecco22.com.metaheuristics.ExperimentSetting;
import kokila.gecco22.com.problems.knapsack.Knapsack;
import kokila.gecco22.com.problems.knapsack.mu_var.MuVar_KnapsackMO;

import java.util.HashMap;
import java.util.Iterator;

/**
 * This class runs a one plus one genetic algorithm (GA)
 * Problem is the chance-constrained knapsack problem
 **/
public class
MuVarGSEMO_Setting extends ExperimentSetting {
    //private static Algorithm algorithm ;

    public void init(Knapsack prob, double delta, int iterations) throws JMException {
        Operator  mutation  ;         // Mutation operator
        HashMap  parameters ; // Operator parameters

        problem = new MuVar_KnapsackMO( prob.getName());
        problem.init(prob.getLength(0), prob.getWeightBound(), prob.getProfit(), prob.getWeight());
        problem.setDelta(delta);
        this.delta = delta;

        algorithm = new MuVarGSEMO(problem) {
            @Override
            public boolean weaklyDominates(Solution first, Solution second) {
                double firstMean = first.getObjective(MuVar_KnapsackMO.MU);
                double secondMean = second.getObjective(MuVar_KnapsackMO.MU);
                double firstVar = first.getObjective(MuVar_KnapsackMO.VAR);
                double secondVar = second.getObjective(MuVar_KnapsackMO.VAR);

                return firstMean >= secondMean && firstVar <= secondVar;
            }

            @Override
            public boolean stronglyDominates(Solution first, Solution second) {

                /*double firstMean = first.getObjective(MuVar_KnapsackMO.OBJECTIVE_MU);
                double secondMean = second.getObjective(MuVar_KnapsackMO.OBJECTIVE_MU);
                double firstVar = first.getObjective(MuVar_KnapsackMO.OBJECTIVE_VAR);
                double secondVar = second.getObjective(MuVar_KnapsackMO.OBJECTIVE_VAR);

                return (firstMean >= secondMean && firstVar <= secondVar) &&
                        (firstMean > secondMean || firstVar < secondVar);*/
                return weaklyDominates(first, second);
            }

            @Override
            public SolutionSet sanitize(SolutionSet population)
            {
                SolutionSet newPopulation = new SolutionSet(population.getMaxSize()) ;
                double alphaStar = 0.000001;// 10^(-3) * 10^(-3)
                double temp = Math.sqrt((1-alphaStar)*alphaStar);
                population.sort(new ObjectiveComparator(MuVar_KnapsackMO.MU,true));
                Solution s1 = population.get(0);
                double maxCheby = s1.getObjective(MuVar_KnapsackMO.MU)- Math.sqrt(temp * s1.getObjective(MuVar_KnapsackMO.VAR));

                newPopulation.add(s1);
                for(int i=1; i<population.size();i++)
                {
                    Solution s2 = population.get(0);
                    double cheby = s2.getObjective(MuVar_KnapsackMO.MU)- Math.sqrt(temp * s2.getObjective(MuVar_KnapsackMO.VAR));
                    if(cheby > maxCheby)
                    {
                        newPopulation.add(s2);
                    }
                }
                return newPopulation;
            }
        };

        parameters = new HashMap() ;
        parameters.put("probability", 1.0/problem.getNumberOfBits()) ;
        mutation = MutationFactory.getMutationOperator("BitFlipMutation", parameters);
        /* Add the operators to the algorithm*/
        algorithm.addOperator("mutation",mutation);
        algorithm.setInputParameter("maxEvaluations", iterations);
        algorithm.setInputParameter("populationSize", iterations);
    } //init

    @Override
    protected void logResults(SolutionSet population){
        double[] alphas = new double[]{0.1, 0.01, 0.001};;//{0.1, 0.01, 0.001};
        double[] cheby = new double[3];
        double[] chern = new double[3];
        population.sort(new ObjectiveComparator(MuVar_KnapsackMO.MU, true));

        int c = ((Binary)(population.get(0).getDecisionVariables()[0])).bits_.cardinality();
        //(Binary)(population.get(0).getDecisionVariables()[0]).get;
        for (Iterator<Solution> it = population.iterator(); it.hasNext(); ) {
            Solution solution = it.next();

            for(int i = 0; i<alphas.length;i++)
            {

                double mu = solution.getObjective(MuVar_KnapsackMO.MU);
                double var = solution.getObjective(MuVar_KnapsackMO.VAR);
                double value = mu - Math.sqrt(var * (1 - alphas[i]) / alphas[i]);
                if (value > cheby[i])
                {
                    cheby[i] = value;
                }
                int count = ((Binary)(solution.getDecisionVariables()[0])).bits_.cardinality();//solution.getObjective(MuVar_KnapsackMO.OBJ_C);
                value = mu -
                        (delta * Math.sqrt(Math.log(1 / alphas[i]) * 2 * count));
                if (value > chern[i])
                {
                    chern[i] = value;
                }
            }
        }
        //System.out.println("Cheby\t\tChern\t");
        //int delta= (int) ((MuVar_KnapsackMO)this.problem_).getDelta();

        System.out.printf("%d\t" +
                        "%.4f\t%.4f\t" +
                        "%.4f\t%.4f\t" +
                        "%.4f\t%.4f\t",
                c,
                cheby[0], chern[0],
                cheby[1], chern[1],
                cheby[2], chern[2]);
    }
}
