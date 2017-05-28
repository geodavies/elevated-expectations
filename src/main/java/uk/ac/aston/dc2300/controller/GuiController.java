package uk.ac.aston.dc2300.controller;

import uk.ac.aston.dc2300.component.Simulation;
import uk.ac.aston.dc2300.gui.LandingConfig;
import uk.ac.aston.dc2300.gui.SimulationSummary;
import uk.ac.aston.dc2300.model.configuration.SimulationConfiguration;
import uk.ac.aston.dc2300.model.status.SimulationStatus;

import javax.swing.*;
import java.awt.*;

/**
 * An implementation of ApplicationController which displays a graphical user interface.
 *
 * @author Dan Cotton
 * @since 06/04/17
 */
public class GuiController implements ApplicationController {

    private JFrame configFrame;

    public GuiController() {
        System.out.println("Initializing application in 'GUI' mode");
        configFrame = new JFrame("Elevator Simulation");
    }

    @Override
    public void start() {
        System.out.println("Starting GUI from controller to allow configuration.");
        startConfigUI();
    }

    private void startConfigUI() {

        LandingConfig landingConfig = new LandingConfig((SimulationConfiguration configuration) -> {
            System.out.println("[GUI] Config completed - initialising simulation");
            setupSimulation(configuration);
        });

        configFrame.setContentPane(landingConfig.getConfigPanel());
        configFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        configFrame.setResizable(false);
        configFrame.pack();
        configFrame.setVisible(true);

        // Calc and set location
        final Dimension screenDimensions = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension frameDimensions = configFrame.getSize();

        System.out.println("[GUI] Screen Dimensions: X-" + screenDimensions.width + " & Y-" + screenDimensions.height);
        System.out.println("[GUI] Frame Dimensions: X-" + frameDimensions.width + " & Y-" + frameDimensions.height);

        int xPosition = (screenDimensions.width / 2) - (frameDimensions.width / 2);
        int yPosition = (screenDimensions.height / 2) - (frameDimensions.height / 2);

        System.out.println("[GUI] Centering Window @ position: X-" + xPosition + " & Y-" + yPosition);
        configFrame.setLocation(xPosition, yPosition);
    }

    public void setupSimulation(SimulationConfiguration configuration) {


        configFrame.setVisible(false);
        configFrame.dispose();

        SimulationSummary runningFrame = new SimulationSummary(e -> {
            configFrame.setVisible(false);
            configFrame.dispose();
            startConfigUI();
        });

        configFrame.setContentPane(runningFrame.getFrame());
        configFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        configFrame.setResizable(true);
        configFrame.pack();
        configFrame.setVisible(true);


        Runnable sim = () -> {

            // Construct new simulation from GUI config
            Simulation simulation = new Simulation(configuration);
            boolean simulationRunning = true;
            SimulationStatus currentStatus = null;

            String summary = "";

            // Loop until simulation isn't running
            while (simulationRunning) {
                currentStatus = simulation.tick();
                simulationRunning = currentStatus.isSimulationRunning();
                summary += currentStatus.getBuilding().toString();
            }
            runningFrame.updateText(summary);
            System.out.println(String.format("Simulation Completed at time: %s ", currentStatus.getTime()));

        };
        SwingUtilities.invokeLater(sim);


    }
}