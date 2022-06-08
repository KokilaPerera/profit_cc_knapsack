package kokila.gecco22.com.run;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class BestAlphaVisualizer extends BaseVisualizer {

    protected double[] alphas = new double[]{0.01, 0.005, 0.001, 0.00005, 0.000025, 0.0000275};
    protected double[] alphas2 = new double[]{0.01, 0.005, 0.001, 0.00005, 0.000025, 0.0000275};

    BestAlphaVisualizer(double delta) {
        this.delta = delta;
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g1 = (Graphics2D) g;


        initPaintComponent(g1);
        plot(g1);

    }

    protected void calculateScale(double[] xCoordinates, double[] yCoordinates, int excludeXStart, int excludeYStart)
    {

    }
    protected void calculateScale(double[] xCoordinates, double[] yCoordinates, int excludeXStart, int excludeXEnd, int excludeYStart, int excludeYEnd)
    {

    }

    public void plot(Graphics2D g1){

    }

    public void perform(ArrayList<Double> varValues, ArrayList<Double> muValues) {

        if (!isSetCoordinates)
            setCoordinateValues(varValues, muValues);
        alphas = new double[varCoords.length];
        alphas2 = new double[varCoords.length];

        double VAR = delta * delta / 3.0;
        System.out.println("PRINTING....\n %2i: alphaCheby_i\t(AlphaChern_i*)\t|\tbestSolIndex: profit");

        for ( int i = 0; i < varValues.size(); i++) {
            alphas[i] = getChebyAlpha(muCoords[i], varCoords[i]);
            System.out.printf("%2d: %.8f\t|\t", i, alphas[i]);
        }
        arrangeRange(alphas, alphas2, true);
    }

    protected void arrangeRange(double[] lowerLimits, double[] upperLimits, boolean overrideUpperLimits)
    {
        if(overrideUpperLimits) {
            upperLimits[0] = 1.0;
            for (int j = 1; j < lowerLimits.length - 1; j++) {
                upperLimits[j] = lowerLimits[j - 1];
            }
        }
        for (int j = 0; j < lowerLimits.length; j++) {
            if(lowerLimits[j]>upperLimits[j]) {
                double temp = upperLimits[j];
                upperLimits[j] =lowerLimits[j];
                lowerLimits[j] = temp;
            }
            //System.out.println(j +"\t"+alphas[j] +"<<==>>"+ alphas2[j]);
        }
    }

    protected int getBestSolIndexForCheby(double[] mu, double[] var, double alpha)
    {
        double maxProfit = Double.NEGATIVE_INFINITY;
        int maxProfitI = -1;
        for(int j=0; j<mu.length-1 ; j++) {
            double value = getChebyProfit(mu[j], var[j], alpha);
            if(maxProfit<value) {
                maxProfit = value;
                maxProfitI = j;
            }
        }
        return maxProfitI;
    }

    protected double getChebyProfit(double mu, double var, double alpha){
        double profit = mu - Math.sqrt(var * (1 - alpha) / alpha);
        return profit;
    }

    protected double getChebyAlpha(double mu1, double var1, double mu2, double var2) {
        double r_sq = (mu1 - mu2) / (Math.sqrt(var1) - Math.sqrt(var2));
        r_sq = r_sq * r_sq;
        double alpha = 1.0 / (r_sq+1.0);
        return alpha;
    }

    protected double getChernAlpha(double mu1, int c1, double mu2, int c2, double delta) {
        double r_sq = (mu1 - mu2) / ((Math.sqrt(c1) - Math.sqrt(c2))*delta);
        r_sq = r_sq * r_sq / 2;
        double alpha = Math.exp(-r_sq);
        return alpha;
    }

    protected double getChebyAlpha(double mu1, double var1) {
        double r_sq = (mu1*mu1) / var1;
        double alpha = 1.0 / (r_sq+1.0);
        return alpha;
    }


    public static void main(String args[]) {

        try {
            if (args.length == 0)
            {
                System.out.println("File is not specified...\nPlease enter a file name:");
                args = new String[]{new Scanner(System.in).nextLine()};
            }
            new BestAlphaVisualizer(Double.parseDouble(args[1])).run(args[0]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }
    }

    //BACKUP alpha range by neighbour calculation
    /*
    * public void perform(ArrayList<Double> varValues, ArrayList<Double> muValues) {
        if (!isSetCoordinates)
            setCoordinateValues(varValues, muValues);
        alphas = new double[varCoordinates.length];
        alphas2 = new double[varCoordinates.length];


        for ( int i = 0; i < varValues.size() - 1; i++) {
            double mu1 = muCoordinates[i];
            double var1 = varCoordinates[i];

                double mu2 = muCoordinates[i+1];
                double var2 = varCoordinates[i+1];
                double r = (mu1 - mu2) / (Math.sqrt(var1) - Math.sqrt(var2));
                double alpha = 1 / ((r * r) + 1);
                alphas[i] = alpha;
                if(i>0)
                    alphas2[i-1] = alpha;
        }
    }
    */

    //BACKUP MATRIX
    /*
    * public void perform(ArrayList<Double> varValues, ArrayList<Double> muValues) {
        if (!isSetCoordinates)
            setCoordinateValues(varValues, muValues);
        alphas = new double[varCoordinates.length];
        alphas2 = new double[varCoordinates.length];
        alphaMatrix = new double[varCoordinates.length][varCoordinates.length];


        for ( int i = 0; i < varValues.size() - 1; i++) {
            double mu1 = muCoordinates[i];
            double var1 = varCoordinates[i];
            for (int j = i+1; j < varCoordinates.length; j++) {
                if(i==j)
                    continue;
                double mu2 = muCoordinates[j];
                double var2 = varCoordinates[j];
                double r = (mu1 - mu2) / (Math.sqrt(var1) - Math.sqrt(var2));
                double alpha = 1 / ((r * r) + 1);
                alphaMatrix[i][j] = alpha;
                alphaMatrix[j][i] = alpha;
             }
        }

        System.out.println("Alpha Matrix\n\n");
        for (int j = 0; j < alphaMatrix.length; j++) {

            double[] row = alphaMatrix[j];
            double rowMin = 1.0;
            double rowMax = -1.0;
            for (int k = 0; k < row.length; k++) {
                //System.out.printf("%.8f\t",row[k]);
                if(rowMin>row[k] && row[k]>0.0)
                    rowMin = row[k];
                if(rowMax<row[k] && row[k]<1.0)
                    rowMax = row[k];
            }
            alphas[j] = rowMin;
            alphas2[j] = rowMax;
            System.out.println(j +"\t"+rowMin +"<<==>>"+ rowMax);
            //System.out.printf(">>%.8f - %.8f\n",rowMin, rowMax);
        }

        System.out.println("<<END of Alpha Matrix>>");
    }
    * */

    /*
    *
    * public void perform(ArrayList<Double> varValues, ArrayList<Double> muValues) {
        if (!isSetCoordinates)
            setCoordinateValues(varValues, muValues);
        alphas = new double[varCoordinates.length];
        alphas2 = new double[varCoordinates.length];

        for ( int i = 0; i < varValues.size() - 1; i++) {
            double mu1 = muCoordinates[i];
            double var1 = varCoordinates[i];

            double mu2 = muCoordinates[i + 1];
            double var2 = varCoordinates[i + 1];
            double r = (mu1 - mu2) / (Math.sqrt(var1) - Math.sqrt(var2));
            double alpha = 1 / ((r * r));

            // for best alpha for 11th solution, calculate the profit of all solutions
            for(int j=0; i==11 && j<muCoordinates.length-1 ; j++) {
                double value = muCoordinates[j] - Math.sqrt(varCoordinates[j] * (1 - alpha) / alpha);
                double value2 = muCoordinates[j+1] - Math.sqrt(varCoordinates[j+1] * (1 - alpha) / alpha);
                System.out.printf("VALUES: %d %d : %f  | %f\n", j, j + 1, value, value2);
            }

            alphas[i] = alpha;
            if(i>1)
            {
                alphas2[i-1] = alphas[i];
            }


        }
        //arrange the alpha range
        for (int j = 0; j < alphas.length; j++) {

            if(alphas[j]>alphas2[j]) {
                double temp = alphas2[j];
                alphas2[j] =alphas[j];
                alphas[j] = temp;
            }
            System.out.println(j +"\t"+alphas[j] +"<<==>>"+ alphas2[j]);
        }
    }
    * */
}
