package uk.ac.aston.dc2300.gui.util;

import javax.swing.*;
import java.math.BigDecimal;


/**
 * Adds validation rules to JTextFields - ensuring only valid BigDecimal values (and in the range zero to one) may be used.
 *
 * @author Dan Cotton
 * @since 12/04/17
 *
 */
public class BigDecimalVerifier extends InputVerifier {
    @Override
    public boolean verify(JComponent input) {
        try {
            // Go fetch string value from JComponent
            String inputValue = ((JTextField) input).getText();

            // Check value is valid decimal
            BigDecimal validationDecimal = new BigDecimal(inputValue);

            // Return condition for within range of zero & one
            return (validationDecimal.compareTo(BigDecimal.ZERO) >= 0 && validationDecimal.compareTo(BigDecimal.ONE) <= 0);

        } catch (NumberFormatException e) {
            return false;
        }
    }
}