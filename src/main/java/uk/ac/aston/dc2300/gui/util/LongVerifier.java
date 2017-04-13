package uk.ac.aston.dc2300.gui.util;

import javax.swing.*;

/**
 * Created by dan on 12/04/2017.
 */
public class LongVerifier extends InputVerifier {
    /**
     * Adds validation rules to JTextFields - ensuring only valid
     * long values may be used.
     *
     * @param input the JComponent to be validated.
     * @return the validity as a boolean
     */
    @Override
    public boolean verify(JComponent input) {
        try {
            // Get input string from component
            String inputString = ((JTextField) input).getText();

            // Check if valid long
            long validationLong = Long.parseLong(inputString);

            return true;
        } catch (NumberFormatException e) {

            return false;

        }
    }
}
