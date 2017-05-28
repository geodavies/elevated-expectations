package uk.ac.aston.dc2300.gui;

import uk.ac.aston.dc2300.gui.util.GUIChange;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by dan on 28/05/2017.
 */
public class SimulationSummary {
    private JTextPane logView;
    private JButton backButton;
    private JPanel panel;
    private JProgressBar progressBar;

    public SimulationSummary(GUIChange changeNotifier) {
        backButton.addActionListener(e -> {
            changeNotifier.guiChange(null);
        });
    }

    public void updateText(String text) {
        progressBar.setVisible(false);
        logView.setText(text);
    }

    public JPanel getFrame() {
        return this.panel;
    }
}
