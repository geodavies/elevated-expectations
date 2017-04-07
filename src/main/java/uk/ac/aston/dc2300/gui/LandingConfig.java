package uk.ac.aston.dc2300.gui;

import com.oracle.tools.packager.Log;
import de.craften.ui.swingmaterial.MaterialProgressSpinner;
import org.apache.log4j.LogManager;
import uk.ac.aston.dc2300.component.Simulation;
import uk.ac.aston.dc2300.controller.CliController;
import uk.ac.aston.dc2300.model.configuration.SimulationConfiguration;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

/**
 * Created by dan on 07/04/2017.
 */
public class LandingConfig {
    private static final org.apache.log4j.Logger LOGGER = LogManager.getLogger(LandingConfig.class);

    private JPanel landingConfigPanel;
    private JPanel landingConfigPanelCopy;
    private JTextField floorChangeProbabilityField;
    private JTextField clientArrivalProbabilityField;
    private JTextField randomSeedField;
    private JTextField numberEmployeesField;
    private JTextField numberDevelopersField;
    private JPanel saveButtonContainer;
    private JButton saveButton;

    /*
        Material UI Components
     */

    /*
        Defining Required Simulation Config Data
    */
    private BigDecimal employeeFloorChangeProbability;
    private BigDecimal clientArrivalProbability;
    private long seed;
    private int numEmployees;
    private int numDevelopers;

    /*
        Defining Required Simulation Config Data
    */
    private Simulation simulation;

    public LandingConfig(){
        super();
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*
                    Button Pressed - Populate Values
                 */
                LOGGER.info("[GUI] Button Pressed - Initiating Simulation");
                employeeFloorChangeProbability = new BigDecimal(floorChangeProbabilityField.getText());
                clientArrivalProbability = new BigDecimal(clientArrivalProbabilityField.getText());
                seed = Long.parseLong(randomSeedField.getText());
                numEmployees = Integer.parseInt(numberEmployeesField.getText());
                numDevelopers = Integer.parseInt(numberDevelopersField.getText());

                LOGGER.debug("[GUI] Using following values for simulation");
                LOGGER.debug("[GUI] EmpChange: " + employeeFloorChangeProbability);
                LOGGER.debug("[GUI] ClientArrive: " + clientArrivalProbability);
                LOGGER.debug("[GUI] RandSeed: " + seed);
                LOGGER.debug("[GUI] NumEmp: " + numEmployees);
                LOGGER.debug("[GUI] NumDev: " + numDevelopers);

                /*
                    Values Retrieved - Pass to Config Object
                */
                SimulationConfiguration configObject = new SimulationConfiguration(employeeFloorChangeProbability,
                        clientArrivalProbability,
                        seed,
                        numEmployees,
                        numDevelopers);
                LOGGER.debug("[GUI] Constructed config obj: " + configObject.toString());

                /*
                    SimulationConfiguration Object Instantiated.
                    Instantiate Simulation and run it.
                */
                simulation = new Simulation(configObject);
                simulation.start();

            }
        });
    }

    public JPanel getConfigPanel(){
        return landingConfigPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
