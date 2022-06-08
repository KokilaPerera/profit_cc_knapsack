package kokila.gecco22.com.metaheuristics.mu_var;

import jmetal.core.Operator;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.encodings.variable.Binary;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.JMException;
import jmetal.util.comparators.ObjectiveComparator;
import kokila.gecco22.com.metaheuristics.ExperimentSetting;
import kokila.gecco22.com.problems.knapsack.Knapsack;
import kokila.gecco22.com.problems.knapsack.mu_var.MuVar_KnapsackMO;
import kokila.gecco22.com.problems.knapsack.mu_var.MuVar_KnapsackMO2;

import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class NSGAII_Setting extends ExperimentSetting {
    public static Logger logger_;
    public static FileHandler fileHandler_;

    public void init(Knapsack prob, double delta, int iterations) throws JMException {
        /*
        logger_ = Configuration.logger_;
        fileHandler_ = new FileHandler("NSGAII_main.log");
        logger_.addHandler(fileHandler_);*/
        QualityIndicator indicators = null;
        this.delta = delta;
        problem = new MuVar_KnapsackMO2(prob.getName());
        problem.init(prob.getLength(0), prob.getWeightBound(), prob.getProfit(), prob.getWeight());
        problem.setDelta(delta);

        algorithm = new NSGAII(problem);
        algorithm.setInputParameter("populationSize", 100);
        algorithm.setInputParameter("maxEvaluations", iterations);

        HashMap parameters = null;
        Operator selection = SelectionFactory.getSelectionOperator("BinaryTournament2", parameters);
        parameters = new HashMap();
        parameters.put("probability", 1.0D);
        Operator crossover = CrossoverFactory.getCrossoverOperator("SinglePointCrossover", parameters);// sbx does not work with binary
        parameters = new HashMap();
        parameters.put("probability", 1.0/ prob.getNumberOfBits() );
        Operator mutation = MutationFactory.getMutationOperator("BitFlipMutation", parameters); // PolynomialMutation >> BitFlipMutation
        algorithm.addOperator("crossover", crossover);
        algorithm.addOperator("mutation", mutation);
        algorithm.addOperator("selection", selection);
        algorithm.setInputParameter("indicators", indicators);

    }

    protected void logResults(SolutionSet population) {
        double[] alphas = new double[]{0.1, 0.01, 0.001};;//{0.1, 0.01, 0.001};
        double[] cheby = new double[3];
        double[] chebyX = new double[3];
        double[] chern = new double[3];
        double[] chernX = new double[3];

        population.sort(new ObjectiveComparator(MuVar_KnapsackMO2.MINUS_MU, false));

        for (Iterator<Solution> it = population.iterator(); it.hasNext(); ) {
            Solution solution = it.next();

            double mu = (-1.0) * solution.getObjective(MuVar_KnapsackMO2.MINUS_MU);
            double var = solution.getObjective(MuVar_KnapsackMO2.VAR);
            double count = ((Binary)(solution.getDecisionVariables()[0])).bits_.cardinality();

            for (int i = 0; i < alphas.length; i++) {
                double alpha = alphas[i];
                double value = mu - Math.sqrt(var * (1 - alpha) / alpha);
                if (value > cheby[i]) {
                    cheby[i] = value;
                    chebyX[i] = mu;
                }
                value = mu - (delta * Math.sqrt(Math.log(1 / alpha) * 2 * count));
                if (value > chern[i]) {
                    chern[i] = value;
                    chernX[i] = mu;
                }
            }
        }
        //System.out.printf("[ weight, profit_mu] = [%s\t%s]");
        System.out.printf("%.4f\t%.4f\t" +
                        "%.4f\t%.4f\t" +
                        "%.4f\t%.4f\t",
                cheby[0], chern[0],
                cheby[1], chern[1],
                cheby[2], chern[2]);

        System.out.printf("mu\n\t%.4f\t%.4f\t" +
                        "%.4f\t%.4f\t" +
                        "%.4f\t%.4f\t",
                chebyX[0], chernX[0],
                chebyX[1], chernX[1],
                chebyX[2], chernX[2]);
    }
}
