package kokila.gecco22.com.run;

import java.util.ArrayList;
import java.util.List;

public class Test {
    private static int runNumber = 0;
    private static Solution runOnce(Problem problem, FitnessFunction fitnessFunction, double alpha, double delt, int maxGenerations) {
        System.out.printf("%s,run %d\n", fitnessFunction.toString(), runNumber++);
        System.out.println("generation,profit,weight,items,probability");

        //Setting alpha and delta
        problem.setAlpha(alpha);
        problem.setDelta(delt);
        double varitem = delt * delt / 3;
        int bound = problem.getBound();
        List<Integer> weights = problem.getWeights();
        List<Integer> values = problem.getValues();
        List<Entry> T = new ArrayList<>();
        List<Entry> N = new ArrayList<>();
        ;
        T.add(new Entry(0, 0, 0));
        int size = weights.size();
        int listsize;
        int newlistsize = 0;
        double w, p;
        Entry E, F;
        boolean removed = false;
        boolean isdominated = false;
        double newweight = 0;
        double newvar = 0;
        double newval = 0;

        for (int i = 0; i < size; i++) {

            N.addAll(T);
            w = weights.get(i);
            p = values.get(i);
            //System.out.println(i + " : " + w + " " + p + " " + varitem);
            listsize = T.size();
            for (int k = 0; k < listsize; k++) {
                E = T.get(k);


                newweight = E.get_weight() + w;
                newval = E.get_val() + p;
                newvar = E.get_var() + 1;

                if (newweight <= bound) {
                    isdominated = false;
                    for (int j = 0; j < N.size() && !isdominated; j++) {
                        F = N.get(j);
                        if ((F.get_weight() <= newweight) && (F.get_val() >= newval) && (F.get_var() <= newvar)) {
                            isdominated = true;
                            //       System.out.println("dominated");
                        }
                    }
                    if (isdominated == false) {
                        for (int j = N.size() - 1; j >= 0; j--) {
                            F = N.get(j);
                            if ((F.get_weight() >= newweight) && (F.get_val() <= newval) && (F.get_var() >= newvar)) {
                                N.remove(j);
                                //       System.out.println("removed");
                            }
                        }
                        N.add(new Entry(newweight, newval, newvar));
                        //     System.out.println("new added");
                    }

                }
            }
            T.clear();
            T.addAll(N);
            N.clear();
        }


        double[] deltas = {25.0, 50.0};
        double[] alphas = {0.1, 0.01, 0.001};

        System.out.println("Chebyshev results");
        for (double alph : alphas) {

            for (double delta : deltas) {
                double value = 0;
                double bestvalue = 0;
                for (int k = 0; k < T.size(); k++) {
                    E = T.get(k);
                    //    System.out.println(E.get_weight()+" "+ E.get_val()+" "+E.get_var());

                    if (E.get_weight() <= problem.getBound()) {
                        value = E.get_val() - Math.sqrt((1 - alph) * E.get_var() * delta * delta / (3 * alph));
                        if (value > bestvalue) {
                            bestvalue = value;
                        }


                    }
                }

                System.out.println("Best value for delta, alpha, bound" + delta + ", " + alph + ", " + problem.getBound() + " : " + bestvalue);
            }
        }


        System.out.println("Chernoff results");
        for (double alph : alphas) {

            for (double delta : deltas) {
                double value = 0;
                double bestvalue = 0;
                for (int k = 0; k < T.size(); k++) {
                    E = T.get(k);
                    //    System.out.println(E.get_weight()+" "+ E.get_val()+" "+E.get_var());

                    if (E.get_weight() <= problem.getBound()) {
                        value = E.get_val() - delta * Math.sqrt((Math.log(1 / alph)) * 2 * E.get_var());
                        if (value > bestvalue) {
                            bestvalue = value;
                        }


                    }
                }

                System.out.println("Best value for delta, alpha, bound" + delta + ", " + alph + ", " + problem.getBound() + " : " + bestvalue);
            }
        }


        Solution best = new Solution(problem, fitnessFunction);


        return best;
    }

    private static class Entry {
        public Entry(double i, double i1, double i2) {

        }

        public double get_weight() {
            return 0;
        }

        public double get_val() {
            return 0.0;

        }

        public double get_var() {
            return 0.0;

        }
    }

    private static class Solution {
        public Solution(Problem problem, FitnessFunction fitnessFunction) {

        }
    }

    private static class Problem {
        private double alpha;
        private double delta;
        private int bound;
        List<Integer> weights = new ArrayList<>();
        List<Integer> values = new ArrayList<>();
        public void setAlpha(double alpha) {
            this.alpha = alpha;
        }

        public void setDelta(double delta) {
            this.delta = delta;
        }

        public void setBound(int bound) {
            this.bound = bound;
        }

        public double getAlpha() {
            return alpha;
        }

        public double getDelta() {
            return delta;
        }

        public int getBound() {
            return this.bound;
        }

        public List<Integer> getWeights() {
            return weights;
        }

        public List<Integer> getValues() {
            return values;
        }
    }

    private static class FitnessFunction {
    }
}