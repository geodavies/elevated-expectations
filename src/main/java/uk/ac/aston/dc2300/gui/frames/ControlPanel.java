package uk.ac.aston.dc2300.gui.frames;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by dan on 31/05/2017.
 */
public class ControlPanel {
    private ActionListener speedHandler;

    private JPanel containerPanel;
    private JButton backButton;
    private JButton pauseButton;
    private JButton normalSpeedButton;
    private JButton twoXSpeedButton;
    private JButton fiveXSpeedButton;
    private JButton twentyFiveXSpeedButton;

    public ControlPanel() {
        setupUI();
    }

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

        // Add to container
        containerPanel.add(backButton);
        containerPanel.add(pauseButton);
        containerPanel.add(normalSpeedButton);
        containerPanel.add(twoXSpeedButton);
        containerPanel.add(fiveXSpeedButton);
        containerPanel.add(twentyFiveXSpeedButton);
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
    }

    public void setPauseHandler(ActionListener pauseHandler) {
        this.pauseButton.addActionListener(pauseHandler);
    }

    public JPanel getPanel() {
        return containerPanel;
    }

}
