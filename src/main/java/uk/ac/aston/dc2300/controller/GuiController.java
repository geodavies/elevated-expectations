package uk.ac.aston.dc2300.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import uk.ac.aston.dc2300.component.Simulation;
import uk.ac.aston.dc2300.gui.LandingConfig;

import javax.swing.*;

/**
 * <p>
 *     An implementation of ApplicationController which displays a graphical user interface.
 * </p>
 *
 * @author George Davies
 * @since 05/04/17
 */
public class GuiController implements ApplicationController {

    private static final Logger LOGGER = LogManager.getLogger(GuiController.class);

    private Simulation simulation;
    private boolean configured;

    public GuiController() {
        LOGGER.info("Initializing application in 'gui' mode");
        configured = false;
        //simulation = new Simulation(new SimulationConfiguration(BigDecimal.valueOf(0.2),BigDecimal.valueOf(0.2),420,500,500));
    }

    @Override
    public void start() {
        // TODO: Implement
        // ConfigFrame f = new ConfigFrame();
        // f.show();
        JFrame frame = new JFrame("LandingConfig");
        frame.setContentPane(new LandingConfig().getConfigPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

}
