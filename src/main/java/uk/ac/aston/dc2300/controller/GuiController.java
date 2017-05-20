package uk.ac.aston.dc2300.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import uk.ac.aston.dc2300.component.Simulation;
import uk.ac.aston.dc2300.gui.LandingConfig;
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

    private static final Logger LOGGER = LogManager.getLogger(GuiController.class);

    public GuiController() {
        LOGGER.info("Initializing application in 'GUI' mode");
    }

    private void startConfigUI() {

        LandingConfig landingConfig = new LandingConfig((SimulationConfiguration configuration) -> {
            LOGGER.info("[GUI] Config completed - initialising simulation");
            setupSimulation(configuration);
        });

        JFrame configFrame = new JFrame("Elevator Simulation");
        configFrame.setContentPane(landingConfig.getConfigPanel());
        configFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        configFrame.setResizable(false);
        configFrame.pack();
        configFrame.setVisible(true);

        // Calc and set location
        final Dimension screenDimensions = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension frameDimensions = configFrame.getSize();

        LOGGER.debug("[GUI] Screen Dimensions: X-" + screenDimensions.width + " & Y-" + screenDimensions.height);
        LOGGER.debug("[GUI] Frame Dimensions: X-" + frameDimensions.width + " & Y-" + frameDimensions.height);

        int xPosition = (screenDimensions.width / 2) - (frameDimensions.width / 2);
        int yPosition = (screenDimensions.height / 2) - (frameDimensions.height / 2);

        LOGGER.debug("[GUI] Centering Window @ position: X-" + xPosition + " & Y-" + yPosition);
        configFrame.setLocation(xPosition, yPosition);
    }

    @Override
    public void start() {
        LOGGER.info("Starting GUI from controller to allow configuration.");
        startConfigUI();
    }

    public void setupSimulation(SimulationConfiguration configuration) {

        // Construct new simulation from GUI config
        Simulation simulation = new Simulation(configuration);
        simulation.start();

        // Set initial running status
        boolean simulationRunning = true;
        SimulationStatus currentStatus = null;

        // Loop until simulation isn't running
        while (simulationRunning) {
            currentStatus = simulation.tick();
            simulationRunning = currentStatus.isSimulationRunning();
        }

        LOGGER.info(String.format("Simulation Completed at time: %s ", currentStatus.getTime()));
    }
}
