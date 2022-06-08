package kokila.gecco22.com.run;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class BaseVisualizer extends JPanel {
    protected int mar = 75;
    protected double delta;//DATA PARAMETER
    protected int width;
    protected int height;

    protected double scaleX;
    protected double scaleY;

    double[] varCoords;
    double[] stdevCoords;
    double[] muCoords;
    boolean isSetCoordinates = false;

    String plotHeading = "";
    static String instanceName = "";

    protected void initPaintComponent(Graphics2D g) {
        super.paintComponent(g);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        width = getWidth();
        height = getHeight();
        g.draw(new Line2D.Double(mar, mar, mar, height - mar));
        g.draw(new Line2D.Double(mar, height - mar, width - mar, height - mar));
    }


    public void plotInit(Graphics2D g1, double[] xCoordintes, double[] yCoordinates)
    {
        //initialize paint components

        scaleX = (double) (width - 2 * mar) / this.getMax(yCoordinates);
        scaleY = (double) (height - 2 * mar) / getMax(xCoordintes);

        g1.drawString(plotHeading + instanceName,200,25);
    }

    public abstract void perform(ArrayList<Double> x, ArrayList<Double> y);

    public static double getMax(double[] coordinates, int start, int excludingEnd) {
        double max = -Integer.MAX_VALUE;
        for (int i = start; i < excludingEnd; i++) {
            if (coordinates[i] > max)
                max = coordinates[i];

        }
        return max;
    }

    public static double getMax(double[] coordinates) {
        return getMax(coordinates, 0, coordinates.length);
    }

    public static double getNonZeroMin(double[] coordinates) {
        return getNonZeroMin(coordinates, 0, coordinates.length);
    }

    public static double getNonZeroMin(double[] coordinates, int start, int excludingEnd) {
        double min = Integer.MAX_VALUE;
        for (int i = start; i < excludingEnd; i++) {
            if (coordinates[i] < min && coordinates[i]>0.0)
                min = coordinates[i];

        }
        return min;
    }



    public void runUI() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setSize(1200, 900);
        frame.setLocation(100, 100);
        frame.setVisible(true);
    }

    public void run(String inputFileName) throws FileNotFoundException {

        ArrayList<Double> mu = new ArrayList<>();
        ArrayList<Double> var = new ArrayList<>();
        File inputDataFile;
        if (inputFileName != null && inputFileName.length() > 0) {
            inputDataFile = new File(inputFileName);
        } else {
            System.out.println("CONTINUING WITH DEFAULT FILE");
            String filePath = "/Users/kokila/Documents/Projects/test_results/48475/FUN_i_1"; //scanner.nextLine();
            inputDataFile = new File(filePath);
        }

        Scanner fileScanner = new Scanner(inputDataFile);
        while (fileScanner.hasNext()) {
            double temp = fileScanner.nextDouble();
            if(temp<0.0)
                mu.add(-temp);
            else
                mu.add(temp);
            if (fileScanner.hasNext())
                var.add(fileScanner.nextDouble());
        }
        fileScanner.close();


        setCoordinateValues(var, mu);
        perform(var, mu);
        runUI();


    }

    protected void drawVerticalGrid(Graphics2D g1, double[] xCoords) {
        g1.setPaint(Color.ORANGE);
        for (int i = 0; i < xCoords.length; i++) {
            if(xCoords[i]==0.0)
                continue;
            double x1 = mar + scaleX * xCoords[i];

            g1.draw(new Line2D.Double(x1, height - mar, x1, mar));
        }
    }

    protected void drawHorizontalGrid(Graphics2D g1, double[] xCoords) {
        g1.setPaint(Color.ORANGE);
        for (int i = 0; i < xCoords.length; i++) {
            if(xCoords[i]==0.0)
                continue;
            double x1 = height - (mar + scaleX * xCoords[i]);

            g1.draw(new Line2D.Double(x1, height - mar, x1, mar));
        }
    }

    public void setCoordinateValues(java.util.List<Double> xCoord, List<Double> yCoord) {


        // negate if negative expectation is used in the objective function
        List<Double> finalYCoord = yCoord;
        IntStream.range(0, xCoord.size()).filter(i1 -> finalYCoord.get(i1) < 0).forEach(i1 -> finalYCoord.set(i1, finalYCoord.get(i1) * -1.0));

        xCoord = xCoord.stream().distinct().collect(Collectors.toList());
        yCoord = yCoord.stream().distinct().collect(Collectors.toList());
        xCoord.sort(Comparator.reverseOrder());
        yCoord.sort(Comparator.reverseOrder());

        int size = xCoord.size();
        if (yCoord.size() < size)
            size = yCoord.size();
        this.varCoords = new double[size];
        this.stdevCoords = new double[size];
        this.muCoords = new double[size];
        this.isSetCoordinates = true;

        for (int i = 0; i < size; i++) {
            this.varCoords[i] = xCoord.get(i);
            this.stdevCoords[i] = Math.sqrt(xCoord.get(i));
            this.muCoords[i] = yCoord.get(i);
        }

    }

}
