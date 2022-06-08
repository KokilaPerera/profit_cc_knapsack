package kokila.gecco22.com.run;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class BestSolutionVisualizer extends BaseVisualizer {

    double[] xCoordinates;
    private double[] alphas = new double[]{
            1.0,
            0.0337,
            0.0335,
            0.0166,
            0.0164,
            0.0003097,
            0.0003096,
            0.0003010,
            0.0003009,
            0.0001882,
            0.0001881,
            0.0000688,
            0.0000687,
            0.0000399,
            0.0000397,
            0.0000323,
            0.0000322
    };

    private int[] chernI = new int[alphas.length];
    private int[] chebyI = new int[alphas.length];

    public BestSolutionVisualizer(double delta) {
        this.delta = delta;
        for (int i = 0; i < chebyI.length; i++) {
            chebyI[i] = -1;
            chernI[i] = -1;
        }
    }

    protected void paintComponent(Graphics g) {

        Graphics2D g1 = (Graphics2D) g;
        initPaintComponent(g1);
        xCoordinates = this.stdevCoords;

        double xMax = getMax(xCoordinates);
        double yMax = getMax(muCoords);
        scaleX = (double) (width - 2 * mar) / xMax;
        scaleY = (double) (height - 2 * mar) / yMax;

        g1.drawString("Best solutions for given alpha values",200,25);
        //Print Axis Labels
        g1.fill(new Rectangle2D.Double(mar+scaleX*xMax, height-mar - 5, 2, 10));
        g1.drawString(String.format("%.6f",xMax) +"\n(max var)", (int) (scaleX*xMax-100),height-mar+10);
        g1.fill(new Rectangle2D.Double(mar-5, height-mar-scaleY*yMax, 10, 2));
        g1.drawString( "(max mu) "+String.format("%.6f",yMax), (int) (mar-20), (int) (height-mar-scaleY*yMax - 10));
        //Label the origin
        g1.drawString("(0.0, 0.0)", mar/2, height - mar + 10);
        //Print Legends
        /*g1.setPaint(Color.RED);
        g1.drawString("RED - Chernoff", width/2 - 100, mar);
        g1.setPaint(Color.BLUE);
        g1.drawString("BLUE - Chebyshev", width/2 - 100, mar + 20);*/


        g1.setPaint(Color.GREEN);
        for (int i = 0; i < xCoordinates.length; i++) {
            double x1 = mar + scaleX * xCoordinates[i];
            double y1 = height - mar - scaleY * muCoords[i];
            g1.fill(new Ellipse2D.Double(x1 - 2, y1 - 2, 4, 4));
        }

        g1.setPaint(Color.ORANGE);
        for (int i = 0; i < xCoordinates.length; i++) {
            double x1 = mar + scaleX * this.xCoordinates[i];
            double y1 = height - mar - scaleY * muCoords[i];
            g1.fill(new Ellipse2D.Double(x1 - 2, y1 - 2, 4, 4));
        }

        g1.setPaint(Color.RED);
        g1.setFont(g1.getFont().deriveFont(10.0f));

        //plotSpecialMarkers(g1, Color.RED, chernI, alphas, 8);
        plotSpecialMarkers(g1, Color.BLUE, chebyI, alphas, 4);

    }

    protected void plotSpecialMarkers(Graphics2D g1, Color color, int[] markerIndex, double[] param, int diameter)
    {
        g1.setPaint(color);
        double[] xCoordinates;
        if(this.xCoordinates!=null)
            xCoordinates = this.xCoordinates;
        else
            xCoordinates = varCoords;
        for (int i = 0; i < markerIndex.length; i++) {
            int j = markerIndex[i];

            if (j == -1) {
                //System.out.println("No best solution for "+ markerName + param[i]);
                continue;
            }
            double x1 = mar + scaleX * xCoordinates[j];
            double y1 = height - mar - scaleY * muCoords[j];
            g1.fill(new Ellipse2D.Double(x1 - (diameter/2), y1 - (diameter/2), diameter, diameter));
            g1.drawString(param[i]+"", (int) (x1 + 4), (int) (y1 + 5 + 8 * i));
        }
    }


    public void run(String inputFileName, double[] alphas) throws FileNotFoundException {
        if(alphas!=null && alphas.length>0)
            this.alphas = alphas;
        run(inputFileName);
    }


    public void perform(ArrayList<Double> varValues, ArrayList<Double> muValues) {
        double[] cheby = new double[alphas.length];
        chebyI = new int[alphas.length];
        double[] chern = new double[alphas.length];
        chernI = new int[alphas.length];


        for (int i = 0; i < alphas.length; i++) {
            cheby[i] = Double.NEGATIVE_INFINITY;
            chern[i] = Double.NEGATIVE_INFINITY;
        }


        double VAR = delta * delta / 3.0;

        if (!isSetCoordinates)
            setCoordinateValues(varValues, muValues);

        for (int i = 0; i < muCoords.length; i++) {
            double mu = muCoords[i];
            double var = varCoords[i];
            double count = var / VAR;

            for (int j = 0; j < alphas.length; j++) {
                double value = mu - Math.sqrt(var * (1 - alphas[j]) / alphas[j]);
                if (value > cheby[j]) {
                    cheby[j] = value;
                    chebyI[j] = i;
                }
                value = mu - (delta * Math.sqrt(Math.log(1 / alphas[j]) * 2 * count));
                if (value > chern[j]) {
                    chern[j] = value;
                    chernI[j] = i;
                }
            }
        }
        //System.out.printf("[ weight, profit_mu] = [%s\t%s]");
        for (int i = 0; i < alphas.length; i++) {
            double a = alphas[i];
            if(chebyI[i] == -1)
                System.out.println(a+"\tNONE");
            else
                System.out.printf("%.10f: %d>\t%.4f\t%.4f\n",
                        a,chebyI[i],
                        muCoords[chebyI[i]],
                        varCoords[chebyI[i]]);
        }

    }

    public static void main(String args[]) {

        try {
            if (args.length == 0)
            {
                System.out.println("File is not specified...\nPlease enter a file name<underscore>delta value:");
                args = new Scanner(System.in).nextLine().split("_");
            }
            new BestSolutionVisualizer(Double.valueOf(args[1])).run(args[0]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}