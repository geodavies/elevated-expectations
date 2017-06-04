package uk.ac.aston.dc2300.controller;

import uk.ac.aston.dc2300.component.Simulation;
import uk.ac.aston.dc2300.gui.frames.ControlPanel;
import uk.ac.aston.dc2300.gui.frames.LandingConfig;
import uk.ac.aston.dc2300.gui.frames.SimulationCanvas;
import uk.ac.aston.dc2300.model.configuration.SimulationConfiguration;
import uk.ac.aston.dc2300.model.entity.Building;
import uk.ac.aston.dc2300.model.status.SimulationStatistics;
import uk.ac.aston.dc2300.model.status.SimulationStatus;
import uk.ac.aston.dc2300.utility.FileUtils;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * An implementation of ApplicationController which displays a graphical user interface.
 *
 * @author Dan Cotton
 * @since 06/04/17
 */
public class GuiController implements ApplicationController {

    private JFrame uiFrame;
    private int simSpeedMultiplier;
    private boolean simulationRunning;
    private boolean simulationPaused;
    private static final int SIM_SPEED_DEFAULT = 200;
    private boolean goToEnd;

    public GuiController() {
        System.out.println("Initializing application in 'GUI' mode");
        simSpeedMultiplier = 1;
        simulationRunning = false;
        goToEnd = false;
    }

    @Override
    public void start() {
        System.out.println("Starting GUI from controller to allow configuration.");
        startLandingConfigUI();
    }

    /**
     * Configures and displays the LandingConfig window
     */
    private void startLandingConfigUI() {

        // Create LandingConfig and listener to set up the simulation
        LandingConfig landingConfig = new LandingConfig((SimulationConfiguration configuration) -> {
            System.out.println("[GUI] Config completed - initialising simulation");
            setupSimulation(configuration);
        });

        // Set JFrame properties
        uiFrame = new JFrame("Elevator Simulation");
        uiFrame.setContentPane(landingConfig.getConfigPanel());
        uiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        uiFrame.setResizable(false);
        uiFrame.pack();
        uiFrame.setVisible(true);

        // Calculate and set location
        final Dimension screenDimensions = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension frameDimensions = uiFrame.getSize();

        System.out.println("[GUI] Screen Dimensions: X-" + screenDimensions.width + " & Y-" + screenDimensions.height);
        System.out.println("[GUI] Frame Dimensions: X-" + frameDimensions.width + " & Y-" + frameDimensions.height);

        int xPosition = (screenDimensions.width / 2) - (frameDimensions.width / 2);
        int yPosition = (screenDimensions.height / 2) - (frameDimensions.height / 2);

        System.out.println("[GUI] Centering Window @ position: X-" + xPosition + " & Y-" + yPosition);
        uiFrame.setLocation(xPosition, yPosition);
    }

    private void setupSimulation(SimulationConfiguration simulationConfiguration) {

        uiFrame.setVisible(false);
        uiFrame.revalidate();

        SimulationCanvas simulationCanvas = new SimulationCanvas();

        ControlPanel controlPanel = new ControlPanel();
        // Back button
        controlPanel.setBackHandler(e -> {
            uiFrame.setVisible(false);
            uiFrame.revalidate();
            stopSim();
            startLandingConfigUI();
        });
        // Simulation speed controls
        controlPanel.setSpeedHandler(e -> {
            // Get new speed and assign to multiplier
            this.simulationPaused = false;
            int speed =  e.getID();
            if (speed >= 0) {
                this.simSpeedMultiplier = speed;
                this.goToEnd = false;
            } else {
                this.goToEnd = true;
            }
        });
        // Pause button
        controlPanel.setPauseHandler(e -> this.simulationPaused = true);

        // Create container and add canvas + control panel
        JPanel containerPanel = new JPanel();
        containerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        containerPanel.add(simulationCanvas);
        containerPanel.add(controlPanel.getPanel());

        uiFrame.setContentPane(containerPanel);
        uiFrame.pack();
        uiFrame.setVisible(true);

        // Create and start an asynchronous SwingWorker
        SwingWorker<SimulationStatus, Object> simulationWorker = getSimulationWorker(simulationConfiguration, controlPanel, simulationCanvas);
        simulationWorker.execute();
    }

    /**
     * Creates a SwingWorker which controls the simulation
     *
     * @param simulationConfiguration configuration of the simulation
     * @param controlPanel control panel to draw
     * @param simulationCanvas canvas to draw
     * @return the SwingWorker
     */
    private SwingWorker<SimulationStatus, Object> getSimulationWorker(SimulationConfiguration simulationConfiguration,
                                                                      ControlPanel controlPanel,
                                                                      SimulationCanvas simulationCanvas) {

        return new SwingWorker<SimulationStatus, Object>() {
            @Override
            protected SimulationStatus doInBackground() throws Exception {
                // Construct new simulation from GUI config
                Simulation simulation = new Simulation(simulationConfiguration);

                // Add stats listener
                controlPanel.setFileSaveHandler(file -> {
                    FileUtils fileUtils = new FileUtils((File) file.getSource());
                    SimulationStatistics stats = simulation.getStatistics();
                    try {
                        fileUtils.writeToFile(simulationConfiguration.toCSV() + "," + stats.toCSV(),simulationConfiguration.getCSVHeaders() + "," +  stats.getCSVHeaders());
                    } catch (IOException e) {
                        controlPanel.showError("File writing failed. Please try again.");
                    }
                });

                // Set initial running status
                simulationRunning = true;
                SimulationStatus currentStatus = new SimulationStatus(null, 0, true);

                // Loop until simulation isn't running

                while (simulationRunning) {
                    if (!simulationPaused) {
                        currentStatus = simulation.tick();
                        simulationCanvas.update(currentStatus.getBuilding());
                        simulationCanvas.drawStats(simulation.getStatistics());
                        simulationRunning = currentStatus.isSimulationRunning();
                    }
                    if (!goToEnd) {
                        Thread.sleep(SIM_SPEED_DEFAULT / simSpeedMultiplier);
                    }
                }

                // Collect end of sim stats.
                SimulationStatistics stats = simulation.getStatistics();

                // Render end of sim stats
                simulationCanvas.drawStats(stats);

                // Trigger stats file save process
                controlPanel.saveStatsFile();

                System.out.println(String.format("Simulation Completed at time: %s ", currentStatus.getTime()));
                return currentStatus;
            }
        };
    }

    /**
     * Stops the simulation running
     */
    private void stopSim() {
        this.simulationRunning = false;
    }
}
