package uk.ac.aston.dc2300.gui.util;

import javax.swing.*;

/**
 * Adds validation rules to JTextFields ensuring only valid long values may be used.
 *
 * @author Dan Cotton
 * @since 12/04/17
 */
public class LongVerifier extends InputVerifier {

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
