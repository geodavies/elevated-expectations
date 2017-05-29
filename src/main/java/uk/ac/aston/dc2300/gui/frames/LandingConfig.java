package uk.ac.aston.dc2300.gui.frames;

import uk.ac.aston.dc2300.component.Simulation;
import uk.ac.aston.dc2300.gui.util.*;
import uk.ac.aston.dc2300.model.configuration.SimulationConfiguration;

import javax.swing.*;
import java.awt.*;
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

    private JPanel landingConfigPanel;
    private JTextField empFloorChangeProbabilityField;
    private JTextField clientArrivalProbabilityField;
    private JTextField randomSeedField;
    private JTextField numberEmployeesField;
    private JTextField numberDevelopersField;
    private JButton saveButton;
    private JTextField numberFloorsField;
    private JTextField elevatorCapacityField;
    private JTextField simulationTimeField;

    // Define array of input fields
    private JTextField[] inputFields;

    // Defining Required Simulation Config Data
    private BigDecimal empFloorChangeProbability;
    private BigDecimal clientArrivalProbability;
    private long seed;
    private int numEmployees;
    private int numDevelopers;
    private int numFloors;
    private int elevatorCapacity;
    private int simulationTime;

    // Defining Required Simulation Config Data
    private Simulation simulation;

    public LandingConfig(GUIChange changeNotifier){
        // Construct UI
        constructUI();

        // Setup input verifiers
        empFloorChangeProbabilityField.setInputVerifier(new BigDecimalVerifier());
        clientArrivalProbabilityField.setInputVerifier(new BigDecimalVerifier());
        randomSeedField.setInputVerifier(new LongVerifier());
        numberEmployeesField.setInputVerifier(new IntegerVerifier());
        numberDevelopersField.setInputVerifier(new IntegerVerifier());
        numberFloorsField.setInputVerifier(new IntegerVerifier());
        elevatorCapacityField.setInputVerifier(new IntegerVerifier());
        simulationTimeField.setInputVerifier(new IntegerVerifier());

        // Setup save button listener
        saveButton.addActionListener(e -> {
            /*
                Run Validation
             */
            try {
                // Try validating fields.
                validateFields();

                /*
                    Button Pressed - Populate Values
                 */
                System.out.println("[GUI] Button Pressed - Initiating Simulation");
                collectInputData();

                /*
                    Values Retrieved - Pass to Config Object
                */
                SimulationConfiguration configObject = getSimulationConfiguration();
                System.out.println("[GUI] Constructed config obj: " + configObject.toString());

               /*
                    Call GUI Change listener
                */
                if (changeNotifier != null) {
                    System.out.println("[GUI] Passing changes to controller");
                    changeNotifier.guiChange(configObject);
                }

            } catch (InvalidInputException invalidException){
                JOptionPane.showMessageDialog(getConfigPanel(), invalidException.toString());
            }

        });
    }

    private void constructUI() {

        landingConfigPanel = new JPanel();
        landingConfigPanel.setLayout(new GridLayout(10,1));

        // Add title
        JLabel title = new JLabel("Elevated Expectations");
        landingConfigPanel.add(title);


        empFloorChangeProbabilityField = new JTextField();
        empFloorChangeProbabilityField.setText("0.01");
        landingConfigPanel.add(wrapFieldWithLabel("Floor Change probability:", empFloorChangeProbabilityField));

        clientArrivalProbabilityField = new JTextField();
        clientArrivalProbabilityField.setText("0.005");
        landingConfigPanel.add(wrapFieldWithLabel("Client arrival probability:", clientArrivalProbabilityField));

        randomSeedField = new JTextField();
        randomSeedField.setText("420");
        landingConfigPanel.add(wrapFieldWithLabel("Random Seed:", randomSeedField));

        numberEmployeesField = new JTextField();
        numberEmployeesField.setText("10");
        landingConfigPanel.add(wrapFieldWithLabel("Number of Employees:", numberEmployeesField));

        numberDevelopersField = new JTextField();
        numberDevelopersField.setText("10");
        landingConfigPanel.add(wrapFieldWithLabel("Number of Developers:", numberDevelopersField));

        numberFloorsField = new JTextField();
        numberFloorsField.setText("6");
        landingConfigPanel.add(wrapFieldWithLabel("Number of floors:", numberFloorsField));

        elevatorCapacityField = new JTextField();
        elevatorCapacityField.setText("4");
        landingConfigPanel.add(wrapFieldWithLabel("Elevator Capacity:", elevatorCapacityField));

        simulationTimeField = new JTextField();
        simulationTimeField.setText("28800");
        landingConfigPanel.add(wrapFieldWithLabel("Time to run sim (s):", simulationTimeField));

        saveButton = new JButton();
        saveButton.setText("Run Simulation");
        landingConfigPanel.add(saveButton);


        // Setup array
        inputFields = new JTextField[]{empFloorChangeProbabilityField,
                clientArrivalProbabilityField,
                randomSeedField,
                numberEmployeesField,
                numberDevelopersField,
                numberFloorsField,
                elevatorCapacityField,
                simulationTimeField};
    }

    private JPanel wrapFieldWithLabel(String label, JTextField textField) {
        JPanel formContainer = new JPanel();
        formContainer.setLayout(new GridLayout(1, 2));
        formContainer.add(new JLabel(label));
        formContainer.add(textField);
        return formContainer;
    }

    /**
     * Method validates all input fields against their respective input
     * verifiers.
     *
     * @throws InvalidInputException if any input field doesn't validate
     */
    private void validateFields() throws InvalidInputException {
        for (JTextField currentField: inputFields) {

            System.out.println("[GUI] Current field is valid: "
                    + currentField.getInputVerifier().verify(currentField));

            // Get input verifier and validate fields with it.
            if (!currentField.getInputVerifier().verify(currentField)) {
                // If invalid throw exception
                throw new InvalidInputException(currentField);
            }
        }

    }

    /**
     * Method returns the current simulation configuration object
     * - based on the currently 'saved' UI values.
     *
     * @return a SimulationConfiguration from the GUI user input
     * fields
     */
    private SimulationConfiguration getSimulationConfiguration(){
        return new SimulationConfiguration(empFloorChangeProbability, clientArrivalProbability, seed, numEmployees,
                numDevelopers, numFloors, elevatorCapacity, simulationTime);
    }

    /**
     * Method collects values from GUI input fields and stores
     * the type-parsed results in appropriate class fields.
     */
    private void collectInputData() {
        System.out.println("[GUI] Collecting Values from Fields");

        // Collect and parse values from each field.
        empFloorChangeProbability = new BigDecimal(empFloorChangeProbabilityField.getText());
        clientArrivalProbability = new BigDecimal(clientArrivalProbabilityField.getText());
        seed = Long.parseLong(randomSeedField.getText());
        numEmployees = Integer.parseInt(numberEmployeesField.getText());
        numDevelopers = Integer.parseInt(numberDevelopersField.getText());
        numFloors = Integer.parseInt(numberFloorsField.getText());
        elevatorCapacity = Integer.parseInt(elevatorCapacityField.getText());
        simulationTime = Integer.parseInt(simulationTimeField.getText());

        // Log collected values.
        System.out.println("[GUI] Collected following values from input");
        System.out.println("[GUI] EmpChange: " + empFloorChangeProbability);
        System.out.println("[GUI] ClientArrive: " + clientArrivalProbability);
        System.out.println("[GUI] RandSeed: " + seed);
        System.out.println("[GUI] NumEmp: " + numEmployees);
        System.out.println("[GUI] NumDev: " + numDevelopers);
        System.out.println("[GUI] NumFloors: " + numFloors);
        System.out.println("[GUI] ElevatorCapacity: " + elevatorCapacity);
        System.out.println("[GUI] SimulationTime: " + simulationTime);
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

}
