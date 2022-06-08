package kokila.gecco22.com.run;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class BestAlphaMatrix extends BestAlphaVisualizer{

    double[][] alphaMatrix;

    BestAlphaMatrix(double delta){
        super(delta);
        plotHeading = "Alpha Intervals: ";
    }

    public void perform(ArrayList<Double> varValues, ArrayList<Double> muValues) {
        if (!isSetCoordinates)
            setCoordinateValues(varValues, muValues);
        int SIZE = varCoords.length;
        double VAR = delta * delta / 3.0;
        alphas = new double[SIZE];
        alphas2 = new double[SIZE];
        alphaMatrix = new double[SIZE][SIZE];
        alphas2 [0] = 1.0;

        for ( int i = 0; i < SIZE; i++) {
            //double alpha = getChebyAlpha(muCoordinates[i], varCoordinates[i], muCoordinates[i+1], varCoordinates[i+1]);
            alphaMatrix[i][i] = 0.0;
            int j;
            for(j = i+1; j < SIZE ;j++ )
            {
                double alpha_i_j = getChebyAlpha(muCoords[i], varCoords[i], muCoords[j], varCoords[j]);
                //double alpha_i_j = getChernAlpha(muCoords[i], (int)Math.round(varCoords[i]/VAR), muCoords[j], (int)Math.round(varCoords[j]/VAR), delta);
                alphaMatrix[i][j] = alpha_i_j;
                alphaMatrix[j][i] = alpha_i_j;
            }
        }
        //printMatrix(alphaMatrix);
        for (int i = 0; i < alphaMatrix.length; i++) {
            findRange(alphaMatrix, i);
        }

    }

    private void findRange(double[][] alphaMatrix, int i) {
        double[] alphas_i = alphaMatrix[i];

        //System.out.print("For instance i = "+i+"\t");
        double upper;
        if(i==0)
            upper = 1.0;
        else
            upper = getNonZeroMin(alphas_i,0, i );

        double lower;
        if(i == alphas_i.length-1)
            lower = 0.0;
        else
            lower = getMax(alphas_i,i+1, alphas_i.length );
        if(upper > lower && upper <= 1.0)
            System.out.printf("& %.20f & %.20f & %d & %d & %.4f \\\\\n", upper, lower, i, (int) muCoords[i], varCoords[i]);

        alphas[i] = lower;
        alphas2[i] = upper;

        /*
        boolean isIncreasing ;//= alphas_i [1] > alphas_i[0];
        for (int j = 0; j < alphas_i.length - 1; j++) {
            if(j==i)
                continue;
            isIncreasing = alphas_i[j+1]>alphas_i[j];

            int k = j;
            while( k < alphas_i.length - 1 && ((isIncreasing && alphas_i[k+1] >alphas_i[k]) || (!isIncreasing && alphas_i[k+1]<alphas_i[k])) )
            {
                k++;
                if(k == i)
                    k++;
            }
            // i - k alpha values are increasing? isIncreasing

            System.out.print("\t"+j+" to "+k + " alpha is " + (isIncreasing?"increasing :\t":"decreasing:\t"));
            //do calculations to determine limit
            double upper, lower;
            if(isIncreasing)
            {
                upper = alphas_i[k];//getMax(Arrays.copyOfRange(alphas_i,j, k+1));
                lower = 1.0;
            }
            else
            {
                lower = alphas_i[k];//getNonZeroMin(Arrays.copyOfRange(alphas_i, j,k+1));
                upper = 0.0;
            }
            System.out.println(upper +" to " + lower);
            isIncreasing = !isIncreasing;
            j = k-1;
        }*/
    }

    public void printMatrix(double[][] matrix)
    {
        for (int i = 0; i < matrix.length ; i++) {
            for (int j = 0; j < matrix.length; j++) {
                //System.out.printf("%.16f\t",matrix[i][j]);
                if(i==j)
                    System.out.print("***");
                else
                System.out.print(matrix[i][j]+"\t");
            }
            System.out.println();
        }
    }

    public void plot(Graphics2D g1)
    {
        plotIntervalVsExpectation(g1);
    }
    public void plotIntervalVsIndex(Graphics2D g1)
    {
        plotInit(g1, this.muCoords, this.varCoords);
        int excludeFromStart = 0;//excluding some outlier from graph
        int excludeFromEnd = 0;//excluding some outlier from graph
        double xMax;
        double xMin;
        double yMax;
        if(excludeFromStart>0 || excludeFromEnd >0 && excludeFromEnd+excludeFromStart < this.alphas.length-1)
        {
            xMax = getMax(this.alphas2, 1, this.alphas2.length);
            xMin= getNonZeroMin(this.alphas);
        }
        else
        {
            xMax = getMax(alphas2, 1, alphas2.length);

            xMin= getNonZeroMin(alphas);
        }
        yMax = alphas.length;//getMax(muCoordinates);


        scaleX = (double) (width - 2 * mar) / xMax;
        scaleY = (double) (height - 2 * mar) / yMax;

        // draw graph title


        // label axis (with min - max)
        g1.setPaint(Color.RED);
        g1.fill(new Rectangle2D.Double(mar+scaleX*xMax, height-mar - 5, 2, 10));
        g1.drawString(String.format("Alpha = %.8f",xMax), (int) (scaleX*xMax-100),height-mar+10);
        g1.fill(new Rectangle2D.Double(mar+scaleX*xMin, height-mar - 5, 2, 10));
        g1.drawString(String.format("Alpha = %.8f",xMin), (int) (scaleX*xMin+mar+5),height-mar+10);
        g1.fill(new Rectangle2D.Double(mar-5, height-mar-scaleY*yMax, 10, 2));

        g1.drawString( String.format("Solution Index = %d", (int)(yMax-1)), mar-20, (int) (height-mar-scaleY*yMax - 10));

        g1.setFont(g1.getFont().deriveFont(8.0f));
        for (int i = 0; i < muCoords.length; i++) {
            g1.setPaint(Color.BLUE);
            double x1 = mar + scaleX * alphas[i];
            double y1 = height - mar - scaleY * (i+1);//muCoordinates[i];
            double x2 = mar + scaleX * alphas2[i];


            if(x1<=x2){
                if(x2 > width)
                {
                    g1.draw(new Line2D.Double(x1, y1, width, y1));
                }
                else {
                    g1.draw(new Line2D.Double(x1, y1, x2, y1));
                }
                g1.setPaint(Color.BLACK);
                g1.drawString(
                        String.format("S_%d : %.8f - %.8f", i, alphas[i], alphas2[i])
                        //String.format("S_%d - S_%d >>> %.8f", i, (i+1), alphas[i])
                        , (int) (x1 - 75), (int) y1 - 1);
            }

        }
    }

    public void plotIntervalVsExpectation(Graphics2D g1)
    {
        int legendCount = 0;
        int legendX = 500;
        int legendY = 400;
        double[] yCoordinates = this.varCoords;

        g1.setFont(g1.getFont().deriveFont(16.0f));
        g1.setPaint(Color.BLACK);

        plotInit(g1, this.muCoords, yCoordinates);

        // label axis (with min - max)
        g1.drawString("E(x)", mar-15, mar);
        g1.drawString("Var(x)", width - mar, height - mar);

        for (int i = 0; i < yCoordinates.length; i++) {
            double x1 = mar + scaleX * yCoordinates[i];
            double y1 = height - mar - scaleY * muCoords[i];

            if( alphas2[i] <= 1.0 && alphas2[i]>alphas[i]) {
                g1.setPaint(Color.BLUE);
                g1.fill(new Ellipse2D.Double(x1 - 2, y1 - 2, 4, 4));
                g1.drawString("\u03B1"+i, (int) (x1-5), (int) (y1-3));
                //g1.drawString(String.format("%.10f < alpha_%2d <=\t%.10f", alphas[i],  i, alphas2[i]), legendX, legendY +(legendCount*15));
                g1.drawString( alphas[i] + "< \u03B1_" + i + "<=\t"+alphas2[i], legendX, legendY +(legendCount*15));
                legendCount++;
            }
            else{
                g1.setPaint(Color.RED);
                g1.fill(new Ellipse2D.Double(x1 - 2, y1 - 2, 4, 4));
            }
        }
    }

    public static void main(String args[]) {

        try {
            if (args.length == 0)
            {
                System.out.println("File is not specified...\nPlease enter a file name:");

                args = new String[]{new Scanner(System.in).nextLine()};
            }
            instanceName = args[2];
            new BestAlphaMatrix(Double.parseDouble(args[1])).run(args[0]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }
    }
}
