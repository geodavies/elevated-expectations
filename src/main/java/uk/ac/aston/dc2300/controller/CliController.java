package uk.ac.aston.dc2300.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import uk.ac.aston.dc2300.component.Simulation;

/**
 * <p>
 *     An implementation of ApplicationController which allows command line interaction.
 * </p>
 *
 * @author George Davies
 * @since 05/04/17
 */
public class CliController implements ApplicationController {

    private static final Logger LOGGER = LogManager.getLogger(CliController.class);

    private final Simulation simulation;

    public CliController() {
        LOGGER.info("Initializing application in 'cli' mode");
        simulation = new Simulation();
    }

    @Override
    public void start() {
        // TODO: Implement
    }

}
