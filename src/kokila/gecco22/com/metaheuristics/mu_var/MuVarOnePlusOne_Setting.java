package kokila.gecco22.com.metaheuristics.mu_var;

import jmetal.core.Operator;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.encodings.variable.Binary;
import jmetal.operators.mutation.MutationFactory;
import jmetal.util.JMException;
import jmetal.util.comparators.ObjectiveComparator;
import kokila.gecco22.com.metaheuristics.ExperimentSetting;
import kokila.gecco22.com.metaheuristics.OnePlusOne;
import kokila.gecco22.com.problems.knapsack.Knapsack;
import kokila.gecco22.com.problems.knapsack.mu_var.MuVar_KnapsackMO;

import java.util.HashMap;
import java.util.Iterator;

public class MuVarOnePlusOne_Setting extends ExperimentSetting {
    public void init(Knapsack prob, double delta, int iterations) throws JMException {
        MuVar_KnapsackMO problem;         // The problem to solve
        Operator mutation  ;         // Mutation operator
        HashMap parameters ; // Operator parameters

        problem = new MuVar_KnapsackMO( prob.getName());
        problem.init(prob.getLength(0), prob.getWeightBound(), prob.getProfit(), prob.getWeight());
        problem.setDelta(delta);
        this.delta = delta;

        algorithm = new OnePlusOne(problem) {

            public boolean weaklyDominates(Solution first, Solution second) {
                double firstMean = first.getObjective(MuVar_KnapsackMO.MU);
                double secondMean = second.getObjective(MuVar_KnapsackMO.MU);
                double firstVar = first.getObjective(MuVar_KnapsackMO.VAR);
                double secondVar = second.getObjective(MuVar_KnapsackMO.VAR);

                return firstMean >= secondMean && firstVar <= secondVar;
            }

            @Override
            public boolean fitnessComparison(SolutionSet population, Solution parent, Solution offspring) {
                boolean fitterOffspring = false;
                double firstMean = parent.getObjective(MuVar_KnapsackMO.MU);
                double secondMean = offspring.getObjective(MuVar_KnapsackMO.MU);
                double firstVar = parent.getObjective(MuVar_KnapsackMO.VAR);
                double secondVar = offspring.getObjective(MuVar_KnapsackMO.VAR);


                if  (firstMean>secondMean || (firstVar < secondVar))
                {
                    population.clear();
                    population.add(offspring);
                    return true;
                }
                return false;
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
        double[] alphas = new double[]{0.1, 0.0000000001, 0.0000000000001};;//{0.1, 0.01, 0.001};
        double[] cheby = new double[]{-0.0, -0.0, -0.0};
        double[] chern = new double[]{-0.0, -0.0, -0.0};
        double[] chebyX = new double[3];
        double[] chernX = new double[3];
        population.sort(new ObjectiveComparator(MuVar_KnapsackMO.MU, true));

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
                    chebyX[i] = mu;
                }
                int count = ((Binary)(solution.getDecisionVariables()[0])).bits_.cardinality();//solution.getObjective(MuVar_KnapsackMO.OBJ_C);
                value = mu -
                        (delta * Math.sqrt(Math.log(1 / alphas[i]) * 2 * count));
                if (value > chern[i])
                {
                    chern[i] = value;
                    chernX[i] = mu;
                }
            }
        }
        //System.out.println("Cheby\t\tChern\t");
        //int delta= (int) ((MuVar_KnapsackMO)this.problem_).getDelta();

        System.out.printf("%.4f\t%.4f\t" +
                        "%.4f\t%.4f\t" +
                        "%.4f\t%.4f\t",
                cheby[0], chern[0],
                cheby[1], chern[1],
                cheby[2], chern[2]);

        System.out.printf("\n\t%.4f\t%.4f\t" +
                        "%.4f\t%.4f\t" +
                        "%.4f\t%.4f\t",
                chebyX[0], chernX[0],
                chebyX[1], chernX[1],
                chebyX[2], chernX[2]);
    }
}
