package kokila.gecco22.com.visualization;

import jmetal.util.JMException;
import kokila.gecco22.com.metaheuristics.mu_var.MuVarGSEMO_main;
import kokila.gecco22.com.metaheuristics.mu_var.PlotListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainUI extends JFrame {
    private final JComboBox<String> iterationComboBox = new JComboBox<>();
    private final List<double[]> param_x = new ArrayList<>();
    private final List<double[]> param_y = new ArrayList<>();
    ArrayList<String> graphIDs = new ArrayList<>();
    public MainUI() {

        JPanel contentPane = new JPanel();
        setLayout(new GridLayout(3,2));
        setContentPane(contentPane);
        contentPane.setVisible(true);
        contentPane.setSize(300,400);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2,3));

        buttonPanel.add(new JLabel("Algorithm Name: "));
        buttonPanel.add(new JLabel("GSEMO"));
        JButton runButton = new JButton("Run");
        buttonPanel.add(runButton);//.add(runButton);
        JLabel iterationLabel = new JLabel("Iteration Number");
        buttonPanel.add(iterationLabel);
        buttonPanel.add(iterationComboBox);
        JButton plotButton = new JButton("Plot Iteration");
        buttonPanel.add(plotButton);
        contentPane.add(buttonPanel);

        PlotListener listener = (iteration, labelX, x, labelY, y) -> {
            String iterationName = "" + iteration;
            graphIDs.add(iterationName);
            param_x.add(x);
            param_y.add(y);
           // plot(iteration, x, y);//TODO
            iterationComboBox.addItem(iterationName);
            iterationComboBox.setSelectedIndex(-1);
        };

        runButton.addActionListener(e -> {
            try {
                MuVarGSEMO_main.run(listener);
            } catch (JMException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });

        plotButton.addActionListener(e -> {
            int selectedIndex = iterationComboBox.getSelectedIndex();
            double[] x = param_x.get(selectedIndex);
            double[] y = param_y.get(selectedIndex);

            plot(Integer.parseInt((String) iterationComboBox.getSelectedItem()), x, y);
        });


        setTitle("Main UI");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 600);
        setLocation(100, 100);
        setVisible(true);
    }

    private void plot(int iteration, double[] x, double[] y)
    {
        JFrame plotWindow = new JFrame();
        GraphPlot graphPlot = new GraphPlot(x,y, "Variance", "Mu");
        plotWindow.add(graphPlot);
        plotWindow.setTitle("Iteration: " + iteration);
        plotWindow.setSize(600, 600);
        plotWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        plotWindow.setVisible(true);
    }

    public static void main(String[] args) {
        new MainUI();
    }
}