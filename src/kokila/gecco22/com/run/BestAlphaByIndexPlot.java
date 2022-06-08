package kokila.gecco22.com.run;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class BestAlphaByIndexPlot extends BestAlphaVisualizer{

    BestAlphaByIndexPlot(double delta){
        super(delta);

        plotHeading = "Alpha By Solution Index";
    }

    public void plot(Graphics2D g1)
    {
        plotInit(g1, this.muCoords, this.varCoords);
        int excludeFromStart = 1;//excluding some outlier from graph
        int excludeFromEnd = 0;//excluding some outlier from graph
        double xMax;
        double xMin;
        double yMax;
        if(excludeFromStart>0 || excludeFromEnd >0 && excludeFromEnd+excludeFromStart < this.alphas.length-1)
        {
            xMax = getMax(this.alphas, excludeFromStart, this.alphas.length-1-excludeFromEnd);
            xMin= getNonZeroMin(this.alphas, excludeFromStart, this.alphas.length-1-excludeFromEnd);
        }
        else
        {
            xMax = getMax(alphas2);
            xMin= getNonZeroMin(alphas);
        }
        yMax = getMax(muCoords);


        scaleX = (double) (width - 2 * mar) / xMax;
        scaleY = (double) (height - 2 * mar) / yMax;

        // draw graph title
        g1.drawString("Alpha range for each solution",200,25);

        // label axis (with min - max)
        g1.setPaint(Color.RED);
        g1.fill(new Rectangle2D.Double(mar+scaleX*xMax, height-mar - 5, 2, 10));
        g1.drawString(String.format("Alpha = %.8f",xMax), (int) (scaleX*xMax-100),height-mar+10);
        g1.fill(new Rectangle2D.Double(mar+scaleX*xMin, height-mar - 5, 2, 10));
        g1.drawString(String.format("Alpha = %.8f",xMin), (int) (scaleX*xMin+mar+5),height-mar+10);
        g1.fill(new Rectangle2D.Double(mar-5, height-mar-scaleY*yMax, 10, 2));

        g1.drawString( String.format("E(x) = %.1f",yMax), mar-20, (int) (height-mar-scaleY*yMax - 10));

        //grid lines
        g1.setPaint(Color.ORANGE);
        for (double alpha : alphas) {
            if (alpha == 0.0)
                continue;
            double x1 = mar + scaleX * alpha;

            g1.draw(new Line2D.Double(x1, height - mar, x1, mar));
        }


        g1.setFont(g1.getFont().deriveFont(8.0f));
        for (int i = 0; i < muCoords.length; i++) {
            g1.setPaint(Color.BLUE);
            double x1 = mar + scaleX * alphas[i];
            double y1 = height - mar - scaleY * muCoords[i];
            double x2 = mar + scaleX * alphas2[i];

            g1.draw(new Line2D.Double(x1, y1, x2, y1));
            g1.setPaint(Color.BLACK);
            g1.drawString(
                    String.format("S_%d - S_%d >>> %.8f - %.8f", i, (i+1), alphas[i],  alphas2[i])
                    , (int) (x2),(int) y1 - 8);
        }
    }


    public static void main(String args[]) {

        try {
            if (args.length == 0)
            {
                System.out.println("File is not specified...\nPlease enter a file name:");
                args = new String[]{new Scanner(System.in).nextLine()};
            }
            new BestAlphaByIndexPlot(Double.parseDouble(args[1])).run(args[0]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }
    }
}
