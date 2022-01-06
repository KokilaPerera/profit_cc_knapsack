package kokila.gecco22.com.visualization;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

//Extends JPanel class  
public class GraphPlot extends JPanel{
    private final String labelX;
    private final String labelY;
    //initialize coordinates
    double[] cordX ;
    double[] cordY ;
    int marg = 60;

    public GraphPlot(double[] cordX, double[] cordY, String labelX, String labelY) {
        this.cordX = cordX;
        this.cordY = cordY;
        this.labelX = labelX;
        this.labelY = labelY;
    }

    protected void paintComponent(Graphics grf){
        //create instance of the Graphics to use its methods  
        super.paintComponent(grf);
        Graphics2D graph = (Graphics2D)grf;

        //Sets the value of a single preference for the rendering algorithms.  
        graph.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // get width and height  
        int width = getWidth();
        int height = getHeight();

        //find value of x and scale to plot points  
        double x = (double)(width-2*marg)/getMaxX();
        double scale = (double)(height-2*marg)/getMaxY();

        // draw graph
        graph.setPaint(Color.BLUE);
        graph.draw(new Line2D.Double(marg, marg, marg, height-marg));
        graph.draw(new Line2D.Double(marg, height-marg, width-marg, height-marg));

        graph.drawString(getMaxX()+"", width - marg - 10, height-marg + 10);
        graph.drawString(getMaxY()+"", marg -50, marg +15 );
        graph.drawString("0,0", marg -20, height - marg +10 );
        graph.drawString(labelX, width - marg, height-marg );
        graph.drawString(labelY, marg , marg);
        graph.setPaint(Color.RED);
        // set points to the graph  
        for(int i = 0; i< cordY.length; i++){
            double x1 = marg+cordX[i]*x;
            double y1 = height-marg-scale* cordY[i];
            graph.fill(new Ellipse2D.Double(x1-2, y1-2, 4, 4));
        }
    }

    //create getMaxX() method to find maximum value of cordX
    private double getMaxY(){
        /*double max = -Integer.MAX_VALUE;
        for (double v : cordY) {
            if (v > max)
                max = v;

        }
        System.out.println(max);*/
        return 38000.0;//max;
    }
    //create getMaxY() method to find maximum value of cordY
    private double getMaxX(){
        /*double max = -Integer.MAX_VALUE;
        for (double x : cordX) {
            if (x > max)
                max = x;
        }
        System.out.print(max+",");*/
        return 6000;
    }
}  