package kokila.gecco22.com;

import kokila.gecco22.com.visualization.GraphPlot;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GraphDialog extends JPanel {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox iterationComboBox;

    ArrayList<GraphPlot> graphPlots = new ArrayList<>();
    ArrayList<Integer> graphIDs = new ArrayList<>();

    public void addPlot(int id, GraphPlot graphPlot)
    {
        graphPlots.add(graphPlot);
        graphIDs.add(id);
        iterationComboBox.addItem(id);
    }

    public GraphDialog() {
        //setContentPane(contentPane);
        //setModal(true);
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        //setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        /*addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });*/

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        iterationComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

            }
        });
    }

    private void onOK() {
        // add your code here
        //dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        //dispose();
    }

    public static void main(String[] args) {
        GraphDialog dialog = new GraphDialog();
        //dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
