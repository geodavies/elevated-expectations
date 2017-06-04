package uk.ac.aston.dc2300.gui.util;

import javax.swing.*;

/**
 * Adds validation rules to JTextFields ensuring only valid integer values (that are greater than zero) may be used.
 *
 * @author Dan Cotton
 * @since 12/04/17
 *
 */
public class IntegerVerifier extends InputVerifier {

    @Override
    public boolean verify(JComponent input) {
        try {
            // Go fetch string value from JComponent
            String inputValue = ((JTextField) input).getText();

            // Check if valid int
            int validationInt = Integer.parseInt(inputValue);

            // Check greater than 0
            return validationInt > 0;

        } catch (NumberFormatException e) {
            return false;
        }
    }
}
