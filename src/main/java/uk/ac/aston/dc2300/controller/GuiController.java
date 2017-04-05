package uk.ac.aston.dc2300.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import uk.ac.aston.dc2300.component.Simulation;

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

    public GuiController() {
        LOGGER.info("Initializing application in 'gui' mode");
        simulation = new Simulation();
    }

    @Override
    public void start() {
        // TODO: Implement
    }

}
