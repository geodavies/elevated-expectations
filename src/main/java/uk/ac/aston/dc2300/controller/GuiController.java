package uk.ac.aston.dc2300.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import uk.ac.aston.dc2300.component.Simulation;
import uk.ac.aston.dc2300.gui.ConfigFrame;

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
        simulation = new Simulation();
    }

    @Override
    public void start() {
        // TODO: Implement
        ConfigFrame f = new ConfigFrame();

        f.show();
    }

}
