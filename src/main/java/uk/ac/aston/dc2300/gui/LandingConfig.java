package uk.ac.aston.dc2300.gui;

import org.apache.log4j.LogManager;
import uk.ac.aston.dc2300.component.Simulation;
import uk.ac.aston.dc2300.model.configuration.SimulationConfiguration;

import javax.swing.*;
import java.math.BigDecimal;

/**
 * <p>
 *     A JFrame/Swing UI for configuring the parameters of the simulation.
 * </p>
 *
 * @author Dan Cotton
 * @since 06/04/17
 */
public class LandingConfig {
    private static final org.apache.log4j.Logger LOGGER = LogManager.getLogger(LandingConfig.class);

    private JPanel landingConfigPanel;
    private JTextField floorChangeProbabilityField;
    private JTextField clientArrivalProbabilityField;
    private JTextField randomSeedField;
    private JTextField numberEmployeesField;
    private JTextField numberDevelopersField;
    private JButton saveButton;
    private JTextField numberFloorsField;
    private JTextField elevatorCapacityField;

    /*
        Defining Required Simulation Config Data
    */
    private BigDecimal employeeFloorChangeProbability;
    private BigDecimal clientArrivalProbability;
    private long seed;
    private int numEmployees;
    private int numDevelopers;
    private int numFloors;
    private int elevatorCapacity;

    /*
        Defining Required Simulation Config Data
    */
    private Simulation simulation;

    public LandingConfig(){
        saveButton.addActionListener(e -> {
            /*
                Button Pressed - Populate Values
             */
            LOGGER.info("[GUI] Button Pressed - Initiating Simulation");
            collectInputData();

            /*
                Values Retrieved - Pass to Config Object
            */
            SimulationConfiguration configObject = getSimulationConfiguration();
            LOGGER.debug("[GUI] Constructed config obj: " + configObject.toString());

            /*
                SimulationConfiguration Object Instantiated.
                Instantiate Simulation and run it.
            */
            simulation = new Simulation(configObject);
            simulation.start();

        });
    }

    /**
     * Method returns the current simulation configuration object
     * - based on the currently 'saved' UI values.
     *
     * @return a SimulationConfiguration from the GUI user input
     * fields
     */
    private SimulationConfiguration getSimulationConfiguration(){
        return new SimulationConfiguration(employeeFloorChangeProbability,
                clientArrivalProbability,
                seed,
                numEmployees,
                numDevelopers, numFloors, elevatorCapacity);
    }

    /**
     * Method collects values from GUI input fields and stores
     * the type-parsed results in appropriate class fields.
     */
    private void collectInputData() {
        LOGGER.info("[GUI] Collecting Values from Fields");
        /*
            Collect and parse values from each field.
        */
        employeeFloorChangeProbability = new BigDecimal(floorChangeProbabilityField.getText());
        clientArrivalProbability = new BigDecimal(clientArrivalProbabilityField.getText());
        seed = Long.parseLong(randomSeedField.getText());
        numEmployees = Integer.parseInt(numberEmployeesField.getText());
        numDevelopers = Integer.parseInt(numberDevelopersField.getText());
        numFloors = Integer.parseInt(numberFloorsField.getText());
        elevatorCapacity = Integer.parseInt(elevatorCapacityField.getText());
        /*
            Log collected values.
        */
        LOGGER.debug("[GUI] Collected following values from input");
        LOGGER.debug("[GUI] EmpChange: " + employeeFloorChangeProbability);
        LOGGER.debug("[GUI] ClientArrive: " + clientArrivalProbability);
        LOGGER.debug("[GUI] RandSeed: " + seed);
        LOGGER.debug("[GUI] NumEmp: " + numEmployees);
        LOGGER.debug("[GUI] NumDev: " + numDevelopers);
        LOGGER.debug("[GUI] NumFloors: " + numFloors);
        LOGGER.debug("[GUI] ElevatorCapacity: " + elevatorCapacity);
    }

    /**
     * Method returns current config-panel to allow this UI
     * to be injected into another JFrame.
     *
     * @return JPanel instance of the UI
     */
    public JPanel getConfigPanel(){
        return landingConfigPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
