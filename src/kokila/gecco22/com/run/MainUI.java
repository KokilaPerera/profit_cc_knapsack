package kokila.gecco22.com.run;

import kokila.gecco22.com.metaheuristics.mu_var.util.PlotListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainUI extends JFrame {
    private final JComboBox<Integer> iterationComboBox = new JComboBox<>();
    private final JComboBox<Integer> deltaComboBox = new JComboBox<>();
    private final JComboBox<Integer> capacityComboBox = new JComboBox<>();
    private final List<double[]> param_x = new ArrayList<>();
    private final List<double[]> param_y = new ArrayList<>();
    private JFrame plotWindow;

    public MainUI() {

        JPanel contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setVisible(true);
        contentPane.setSize(300,400);

        GridLayout gridLayout = new GridLayout(6,2);
        gridLayout.setHgap(5);
        gridLayout.setVgap (5);

        JPanel buttonPanel = new JPanel();

        buttonPanel.setLayout(gridLayout);

        buttonPanel.add(new JLabel("Algorithm Name: "));
        buttonPanel.add(new JLabel("GSEMO"));

        buttonPanel.add(new JLabel("Capacity: "));
        capacityComboBox.addItem(11775);
        capacityComboBox.addItem(31027);
        capacityComboBox.addItem(58455);
        buttonPanel.add(capacityComboBox);

        buttonPanel.add(new JLabel("Delta: "));
        deltaComboBox.addItem(25);
        deltaComboBox.addItem(50);
        buttonPanel.add(deltaComboBox);

        JButton runButton = new JButton("Run");
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(runButton);

        buttonPanel.add(new JLabel("Iteration Number"));
        buttonPanel.add(iterationComboBox);
        JButton plotButton = new JButton("Plot");
        buttonPanel.add(new JLabel(""));
        buttonPanel.add(plotButton);
        contentPane.add(buttonPanel);

        PlotListener listener = (iteration, labelX, x, labelY, y) -> {
            //String iterationName = "" + iteration;
            param_x.add(x);
            param_y.add(y);
            //plot(iteration, x, y);//TODO
            iterationComboBox.addItem(iteration);
            iterationComboBox.setSelectedIndex(-1);

        };

        runButton.addActionListener(e -> {
            /*try {
                //TODO MuVarGSEMO_main.init((int)deltaComboBox.getSelectedItem(), (int) capacityComboBox.getSelectedItem(), listener);
                //MuVarGSEMO_Setting.printProblem();
                //MuVarGSEMO_Setting.run();
            } catch (JMException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }*/
        });

        plotButton.addActionListener(e -> {
            int selectedIndex = iterationComboBox.getSelectedIndex();
            double[] x = param_x.get(selectedIndex);
            double[] y = param_y.get(selectedIndex);

            plot((Integer) iterationComboBox.getSelectedItem(), x, y);
        });


        setTitle("Main UI");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocation(100, 100);
        setVisible(true);
    }

    private void plot(int iteration, double[] x, double[] y)
    {
        //if(plotWindow==null){
            plotWindow = new JFrame();
            plotWindow.setSize(600, 600);
            plotWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //}
        GraphPlot graphPlot = new GraphPlot(x,y, "Variance", "Mu");
        plotWindow.add(graphPlot);
        plotWindow.setTitle("Iteration: " + iteration);
        plotWindow.setVisible(true);
    }



    public static void main(String[] args) {
        new MainUI();
    }
}