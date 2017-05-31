package uk.ac.aston.dc2300.controller;

import uk.ac.aston.dc2300.component.Simulation;
import uk.ac.aston.dc2300.gui.frames.ControlPanel;
import uk.ac.aston.dc2300.gui.frames.LandingConfig;
import uk.ac.aston.dc2300.gui.frames.SimulationCanvas;
import uk.ac.aston.dc2300.model.configuration.SimulationConfiguration;
import uk.ac.aston.dc2300.model.entity.Building;
import uk.ac.aston.dc2300.model.status.SimulationStatus;

import javax.naming.ldap.Control;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;
import java.awt.*;

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
    private static final int SIM_SPEED_DEFAULT = 200;

    public GuiController() {
        System.out.println("Initializing application in 'GUI' mode");
        simSpeedMultiplier = 1;
        simulationRunning = false;
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

        uiFrame = new JFrame("Elevator Simulation");
        uiFrame.setContentPane(landingConfig.getConfigPanel());
        uiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        uiFrame.setResizable(false);
        uiFrame.pack();
        uiFrame.setVisible(true);

        // Calc and set location
        final Dimension screenDimensions = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension frameDimensions = uiFrame.getSize();

        System.out.println("[GUI] Screen Dimensions: X-" + screenDimensions.width + " & Y-" + screenDimensions.height);
        System.out.println("[GUI] Frame Dimensions: X-" + frameDimensions.width + " & Y-" + frameDimensions.height);

        int xPosition = (screenDimensions.width / 2) - (frameDimensions.width / 2);
        int yPosition = (screenDimensions.height / 2) - (frameDimensions.height / 2);

        System.out.println("[GUI] Centering Window @ position: X-" + xPosition + " & Y-" + yPosition);
        uiFrame.setLocation(xPosition, yPosition);
    }

    public void setupSimulation(SimulationConfiguration configuration) {

        uiFrame.setVisible(false);
        uiFrame.revalidate();

        SimulationCanvas canvas = new SimulationCanvas();

        ControlPanel controlPanel = new ControlPanel();
        controlPanel.setBackHandler(e -> {
            uiFrame.setVisible(false);
            uiFrame.revalidate();
            stopSim();
            startConfigUI();
        });
        controlPanel.setSpeedHandler(e -> {
            // Get new speed and assign to multiplier
            simSpeedMultiplier = e.getID();
        });

        JPanel containerPanel = new JPanel();
        containerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));
        containerPanel.add(canvas);
        containerPanel.add(controlPanel.getPanel());


        uiFrame.setContentPane(containerPanel);
        uiFrame.pack();
        uiFrame.setVisible(true);

        SwingWorker<SimulationStatus, Object> worker = new SwingWorker<SimulationStatus, Object>() {

            @Override
            protected SimulationStatus doInBackground() throws Exception {
                // Construct new simulation from GUI config
                Simulation simulation = new Simulation(configuration);

                // Set initial running status
                simulationRunning = true;
                SimulationStatus currentStatus = null;

                // Loop until simulation isn't running
                while (simulationRunning) {
                    currentStatus = simulation.tick();
                    simulationRunning = currentStatus.isSimulationRunning();
                    canvas.update(new Building(currentStatus.getBuilding()));
                    Thread.sleep(SIM_SPEED_DEFAULT / simSpeedMultiplier);
                }

                System.out.println(String.format("Simulation Completed at time: %s ", currentStatus.getTime()));

                return currentStatus;
            }
        };
        worker.execute();
    }

    private void stopSim() {
        this.simulationRunning = false;
    }
}
