package uk.ac.aston.dc2300.gui;

import com.sun.media.jfxmedia.logging.Logger;
import uk.ac.aston.dc2300.component.Simulation;
import de.craften.ui.swingmaterial.MaterialButton;
import de.craften.ui.swingmaterial.MaterialColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

/**
 * Created by dan on 07/04/2017.
 */
public class LandingConfig {
    private JPanel landingConfigPanel;
    private JTextField floorChangeProbabilityField;
    private JTextField clientArrivalProbabilityField;
    private JTextField randomSeedField;
    private JTextField numberEmployeesField;
    private JTextField numberDevelopersField;
    private JPanel saveButtonContainer;
    private MaterialButton saveButton;

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

    public LandingConfig(){
        super();
        saveButton.setRippleColor(new Color(0,0,0));
    }

    public JPanel getConfigPanel(){
        return landingConfigPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
