package kokila.gecco22.com.visualization;

import jmetal.util.JMException;
import kokila.gecco22.com.metaheuristics.mu_var.MuVarGSEMO_main;
import kokila.gecco22.com.metaheuristics.mu_var.PlotListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainUITest extends JFrame {

    private final JComboBox<String> iterationComboBox = new JComboBox<>();
    private final JFrame plotFrame = new JFrame();
    ArrayList<GraphPlot> graphPlots = new ArrayList<>();
    ArrayList<String> graphIDs = new ArrayList<>();

    public MainUITest() {
        setTitle("Iterations");
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 200);
        setLocation(100, 100);
        setVisible(true);

        JButton runButton = new JButton("Run Algorithm");
        add(runButton);
        JLabel iterationLabel = new JLabel("Iteration Number");
        add(iterationLabel);
        add(iterationComboBox);
        JButton plotButton = new JButton("Plot Iteration");
        add(plotButton);

        plotFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        plotFrame.setSize(600, 600);
        plotFrame.setLocation(120,120);
        plotFrame.setTitle("Plot");
        plotFrame.setVisible(true);

        plotButton.addActionListener(e -> {

            int iteration = iterationComboBox.getSelectedIndex();
            plotFrame.setTitle((String) iterationComboBox.getSelectedItem());
            plotFrame.add(graphPlots.get(iteration));
            plotFrame.repaint();
            plotFrame.setVisible(true);
        });

        runButton.addActionListener(e -> {
            run();
        });
    }

    private void run()
    {
        PlotListener listener = (iteration, labelX, x, labelY, y) -> {
            GraphPlot graphPlot = new GraphPlot(x,y, labelX, labelY);
            String iterationName = "Iteration " + iteration;
            iterationComboBox.addItem(iterationName);
            graphIDs.add(iterationName);

            graphPlots.add(graphPlot);
            plotFrame.add(graphPlot);
            plotFrame.setTitle(iterationName);
            plotFrame.setVisible(true);
        };

        try {
            MuVarGSEMO_main.run();
        } catch (JMException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new MainUITest();
    }
}