package uk.ac.aston.dc2300.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import uk.ac.aston.dc2300.gui.LandingConfig;

import javax.swing.*;

/**
 * <p>
 *     An implementation of ApplicationController which displays a graphical user interface.
 * </p>
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
        JFrame frame = new JFrame("LandingConfig");
        frame.setContentPane(new LandingConfig().getConfigPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void start() {
        LOGGER.info("Starting GUI from controller to allow configuration.");
        startConfigUI();
    }

}
