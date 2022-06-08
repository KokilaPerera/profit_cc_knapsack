package kokila.gecco22.com.problems.knapsack;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.variable.Binary;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Class representing problem OneMax. The problem consist of maximizing the
 * number of '1's in a binary string.
 */
public class Knapsack extends Problem {

    public static final int OBJ_P = 0;
    public static final int OBJ_W = 1;
    public static final int OBJ_C = 2;
    protected double delta_ = 50.0;
    protected double alpha_ = 0.1;

    protected double v;

    /**
     * Stores the number of variables of the problem
     */
    protected int weightBound_;

    /**
     * Stores the number of items of the problem
     */
    protected int numberOfItems_ ;

    /**
     * Stores the profits of items of the problem
     */
    protected int[] profit ;

    /**
     * Stores the weights of items of the problem
     */
    protected int[] weight ;

    public Knapsack(int numberOfItems_, int weightBound_, int[] profit, int[] weight) {
        init(numberOfItems_, weightBound_, profit, weight);
    }

    public void init(int numberOfItems, int weightBound, int[] profit, int[] weight) {
        this.weightBound_ = weightBound;
        this.numberOfItems_ = numberOfItems;
        this.length_[0] = numberOfItems;
        this.profit = profit;
        this.weight = weight;
    }

    public Knapsack() {
        numberOfVariables_  = 1;
        length_ = new int[numberOfVariables_];
        length_[0] = numberOfItems_;
        numberOfObjectives_ = 5;
        numberOfConstraints_= 1;
        solutionType_ = new BinarySolutionType(this) ;
    }

    public Knapsack(String problemName) {
        this();
        this.problemName_ = problemName;
    }



    /**
     * Evaluates a solution
     * @param solution The solution to evaluate
     */
    public void evaluate(Solution solution) {
        Binary variable;
        int counter;
        int profit;
        int weight;
        //KnapsackSolution sol = (KnapsackSolution) solution;
        //for knapsack problem, the binary variable will indicate
        // whether a particular item is selected into the sol or  not
        variable = ((Binary) solution.getDecisionVariables()[0]);

        counter = 0;
        profit = 0;
        weight = 0;

        for (int i = 0; i < variable.getNumberOfBits(); i++){
            if (variable.bits_.get(i)) {
                counter++;
                profit += this.profit[i];
                weight += this.weight[i];
            }
        }
        solution.setObjective(OBJ_C, counter);
        solution.setObjective(OBJ_W, weight);
        solution.setObjective(OBJ_P, profit);

    }

    // calculate the chance of constraint violation using Chebyshev method
    double chebyshevProbability(int count, int value, int constraint, double alpha, double delta)
    {
        //TODO implement chernoff bound later
        double temp1 = delta * delta * count;
        double temp2 = constraint - value;
        temp2 = 3 * temp2 * temp2;
        double chance = temp1 / (temp1 + temp2);
        chance -= alpha;
        if (  chance > 0 ) {
            return BigDecimal.valueOf(chance).setScale(8, RoundingMode.HALF_EVEN).doubleValue();
        }
        return 0;
    }

    public double getCCViolation(int count, int weight)
    {
        //TODO implement chernoff bound later
        double temp1 = delta_ * delta_ * count;
        double temp2 = weightBound_ - weight;
        temp2 = 3 * temp2 * temp2;
        double chance = temp1 / (temp1 + temp2);
        //chance -= this.alpha_;
        if (  chance > 0 ) {
            return BigDecimal.valueOf(chance).setScale(8, RoundingMode.HALF_EVEN).doubleValue();
        }
        return 0;
    }

    public void setDelta(double delta_) {
        this.delta_ = delta_;
        getV(); // to initialize v to this value
    }


    public void setAlpha(double alpha_) {
        this.alpha_ = alpha_;
        getV(); // to initialize v to this value
    }

    public double getV()
    {
        if (v == 0.0)
            v = delta_ * delta_ / 3.0;
        return v;
    }

    public double getDelta() {
        return this.delta_;
    }

    public int[] getProfit()
    {
        return profit;
    }

    public int[] getWeight()
    {
        return weight;
    }

    public int getWeightBound()
    {
        return this.weightBound_;
    }

    public void setWeightBound(int weightBound_) {
        this.weightBound_ = weightBound_;
    }

    public String toString()
    {
        return "Knapsack instance: capacity = " +weightBound_ + " | delta = " + delta_ + " | alpha = " + alpha_;
    }

} // OnePlusOne
