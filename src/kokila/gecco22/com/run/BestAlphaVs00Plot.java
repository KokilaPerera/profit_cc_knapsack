package kokila.gecco22.com.run;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class BestAlphaVs00Plot extends BestAlphaVisualizer{

    BestAlphaVs00Plot(double delta){
        super(delta);
        this.plotHeading = "Alpha value for solution (comparing with E=0.0, var=0.0)";
    }

    public void plot(Graphics2D g1)
    {

        double[] yCoordinates = this.stdevCoords;
        plotInit(g1, this.muCoords, yCoordinates);


        g1.setFont(g1.getFont().deriveFont(10.0f));
        for (int i = 0; i < yCoordinates.length; i++) {
            double x1 = mar + scaleX * yCoordinates[i];
            double y1 = height - mar - scaleY * muCoords[i];
            g1.fill(new Ellipse2D.Double(x1 - 2, y1 - 2, 4, 4));

            if(i<yCoordinates.length/2)
                //g1.drawString(String.format("%.8f - %.8f", alphas[i],  alphas2[i]), (int) (x1 - mar), (int) (y1 + 12));
                //g1.drawString(alphas[i] + " - " +alphas2[i], (int) (x1 - mar), (int) (y1 + 12));
                g1.drawString(alphas[i] +"", (int) (x1 - mar), (int) (y1 + 12));
            else
                g1.drawString( alphas[i] + "", (int) (x1 - mar), (int) (y1 + 12));
            //g1.drawString(String.format("%.8f - %.8f", alphas[i],  alphas2[i]), (int) (x1 - mar), (int) (y1 + 12));
        }
    }


    public static void main(String args[]) {

        try {
            if (args.length == 0)
            {
                System.out.println("File is not specified...\nPlease enter a file name:");
                args = new String[]{new Scanner(System.in).nextLine()};
            }
            new BestAlphaVs00Plot(Double.parseDouble(args[1])).run(args[0]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }
    }
}
