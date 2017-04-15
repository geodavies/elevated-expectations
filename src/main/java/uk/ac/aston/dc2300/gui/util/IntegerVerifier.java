package uk.ac.aston.dc2300.gui.util;

import javax.swing.*;
import java.math.BigDecimal;

/**
 * Created by dan on 12/04/2017.
 */
public class IntegerVerifier extends InputVerifier {
    /**
     * Adds validation rules to JTextFields - ensuring only valid
     * integer values (that are greater than zero) may be used.
     *
     * @param input the JComponent to be validated.
     * @return the validity as a boolean
     */
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
