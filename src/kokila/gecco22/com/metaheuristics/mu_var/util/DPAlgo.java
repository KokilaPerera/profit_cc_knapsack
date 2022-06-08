package kokila.gecco22.com.metaheuristics.mu_var.util;


import kokila.gecco22.com.problems.knapsack.mu_var.MuVar_KnapsackMO;

import java.util.ArrayList;
import java.util.List;


class Solution {

    public Solution(MuVar_KnapsackMO problem, FitnessFunction fitnessFunction) {

    }
}

class Entry {
    double weight, profit, var;

    Entry(double w, double p, double v) {
        this.weight = w;
        this.profit = p;
        this.var = v;
    }

    public double getWeight() {
        return weight;
    }

    public double getVal() {
        return profit;
    }

    public double getVar() {
        return var;
    }
}

class FitnessFunction {

}

public class DPAlgo {
    private static Solution runOnce(MuVar_KnapsackMO problem, FitnessFunction fitnessFunction, double alpha, double delt, int maxGenerations) {

        /*progressSB.append(String.format("%s,run %d\n", fitnessFunction.toString(), runNumber));
        progressSB.append("generation,profit,weight,items,probability\n");*/

        //Setting alpha and delta
        problem.setAlpha(alpha);
        problem.setDelta(delt);
        double varitem = delt * delt / 3;
        int bound = problem.getWeightBound();
        int[] weights = problem.getWeight();
        int[] values = problem.getProfit();
        List<Entry> targetSolutions = new ArrayList<>();
        List<Entry> newSolutions = new ArrayList<>();
        ;
        targetSolutions.add(new Entry(0, 0, 0));
        int size = weights.length;
        int listsize = 0;
        int newlistsize = 0;
        //double w, p;
        Entry E, F;
        boolean removed = false;
        boolean isdominated = false;
        double newWeight = 0;
        double newVar = 0;
        double newVal = 0;

        for (int i = 0; i < size; i++) {

            newSolutions.addAll(targetSolutions);
            System.out.println(i + " : " + weights[i] + " " + values[i] + " " + varitem);
            listsize = targetSolutions.size();
            for (int k = 0; k < listsize; k++) {
                E = targetSolutions.get(k);
                newWeight = E.getWeight() + weights[i];
                newVal = E.getVal() + values[i];
                newVar = E.getVar() + 1;

                if (newWeight <= bound) {
                    isdominated = false;
                    for (int j = 0; j < newSolutions.size() && !isdominated; j++) {
                        F = newSolutions.get(j);
                        if ((F.getWeight() <= newWeight) && (F.getVal() >= newVal) && (F.getVar() <= newVar)) {
                            isdominated = true;
                            //       System.out.println("dominated");
                        }
                    }
                    if (!isdominated) {
                        for (int j = newSolutions.size() - 1; j >= 0; j--) {
                            F = newSolutions.get(j);
                            if ((F.getWeight() >= newWeight) && (F.getVal() <= newVal) && (F.getVar() >= newVar)) {
                                newSolutions.remove(j);
                                //       System.out.println("removed");
                            }
                        }
                        newSolutions.add(new Entry(newWeight, newVal, newVar));
                        //     System.out.println("new added");
                    }

                }
            }
            targetSolutions.clear();
            targetSolutions.addAll(newSolutions);
            newSolutions.clear();
        }


        double[] deltas = {25.0, 50.0};
        double[] alphas = {0.1, 0.01, 0.001};

        System.out.println("Chebyshev results");
        for (double alph : alphas) {
            for (double delta : deltas) {
                double value = 0;
                double bestValue = 0;
                for (int k = 0; k < targetSolutions.size(); k++) {
                    E = targetSolutions.get(k);
                    //    System.out.println(E.get_weight()+" "+ E.get_val()+" "+E.get_var());

                    if (E.getWeight() <= problem.getWeightBound()) {
                        value = E.getVal() - Math.sqrt((1 - alph) * E.getVar() * delta * delta / (3 * alph));
                        if (value > bestValue) {
                            bestValue = value;
                        }
                    }
                }

                System.out.println("Best value for delta, alpha, bound" + delta + ", " + alph + ", " + problem.getWeightBound() + " : " + bestValue);
            }
        }


        System.out.println("Chernoff results");
        for (double alph : alphas) {
            for (double delta : deltas) {
                double value = 0;
                double bestValue = 0;
                for (int k = 0; k < targetSolutions.size(); k++) {
                    E = targetSolutions.get(k);
                    //    System.out.println(E.get_weight()+" "+ E.get_val()+" "+E.get_var());

                    if (E.getWeight() <= problem.getWeightBound()) {
                        value = E.getVal() - delta * Math.sqrt((Math.log(1 / alph)) * 2 * E.getVar());
                        if (value > bestValue) {
                            bestValue = value;
                        }
                    }
                }
                System.out.println("Best value for delta, alpha, bound" + delta + ", " + alph + ", " + problem.getWeightBound() + " : " + bestValue);
            }
        }
        Solution best = new Solution(problem, fitnessFunction);
        return best;
    }
}