package uk.ac.aston.dc2300.gui.frames;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * A control panel used to select speed, pause and save stats
 *
 * @author Dan Cotton
 * @since 31/05/17
 */
public class ControlPanel {

    private ActionListener speedHandler;
    private ActionListener fileSaveHandler;

    private JPanel containerPanel;
    private JButton backButton;
    private JButton pauseButton;
    private JButton normalSpeedButton;
    private JButton twoXSpeedButton;
    private JButton fiveXSpeedButton;
    private JButton twentyFiveXSpeedButton;
    private JButton goToEndButton;

    private JFileChooser fileChooser;

    public ControlPanel() {
        setupUI();
    }

    /**
     * Creates elements and populates container
     */
    private void setupUI() {
        // Container
        containerPanel = new JPanel();
        containerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.X_AXIS));

        // Buttons
        backButton = new JButton("Back");
        pauseButton = new JButton("Pause");
        normalSpeedButton = new JButton("1x");
        twoXSpeedButton = new JButton("2x");
        fiveXSpeedButton = new JButton("5x");
        twentyFiveXSpeedButton = new JButton("25x");
        goToEndButton = new JButton("Go to End");

        // File chooser
        fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Statistics CSV");

        // Add to container
        containerPanel.add(backButton);
        containerPanel.add(pauseButton);
        containerPanel.add(normalSpeedButton);
        containerPanel.add(twoXSpeedButton);
        containerPanel.add(fiveXSpeedButton);
        containerPanel.add(twentyFiveXSpeedButton);
        containerPanel.add(goToEndButton);
    }

    /**
     * Prompts user to save the file to disk
     */
    public void saveStatsFile() {
        int returnVal = fileChooser.showSaveDialog(containerPanel);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (fileSaveHandler != null) {
                fileSaveHandler.actionPerformed(new ActionEvent(file, 0, ""));
            }
        }
    }

    public void setSpeedHandler(ActionListener speedHandler) {
        this.speedHandler = speedHandler;
        registerSpeedHandlers();
    }

    public void setBackHandler(ActionListener backHandler) {
        this.backButton.addActionListener(backHandler);
    }

    private void registerSpeedHandlers() {
        this.normalSpeedButton.addActionListener(e -> {
            ActionEvent speedEvent = new ActionEvent(normalSpeedButton, 1, null);
            this.speedHandler.actionPerformed(speedEvent);
        });
        this.twoXSpeedButton.addActionListener(e -> {
            ActionEvent speedEvent = new ActionEvent(twoXSpeedButton, 2, null);
            this.speedHandler.actionPerformed(speedEvent);
        });
        this.fiveXSpeedButton.addActionListener(e -> {
            ActionEvent speedEvent = new ActionEvent(fiveXSpeedButton, 5, null);
            this.speedHandler.actionPerformed(speedEvent);
        });
        this.twentyFiveXSpeedButton.addActionListener(e -> {
            ActionEvent speedEvent = new ActionEvent(twentyFiveXSpeedButton, 25, null);
            this.speedHandler.actionPerformed(speedEvent);
        });
        this.goToEndButton.addActionListener(e -> {
            ActionEvent speedEvent = new ActionEvent(goToEndButton, -1, null);
            this.speedHandler.actionPerformed(speedEvent);
        });
    }

    public void setPauseHandler(ActionListener pauseHandler) {
        this.pauseButton.addActionListener(pauseHandler);
    }

    public void setFileSaveHandler(ActionListener fileSaveHandler) {
        this.fileSaveHandler = fileSaveHandler;
    }

    public JPanel getPanel() {
        return containerPanel;
    }

    /**
     * Displays an error prompt to the user
     *
     * @param errorString the content of the error prompt
     */
    public void showError(String errorString) {
        JOptionPane.showMessageDialog(getPanel(), errorString);
    }
}
