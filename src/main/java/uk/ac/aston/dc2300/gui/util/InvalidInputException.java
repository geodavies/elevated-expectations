package uk.ac.aston.dc2300.gui.util;

import javax.swing.*;

/**
 * An exception to be thrown if a field is invalid upon submission.
 *
 * @author Dan Cotton
 * @since 13/04/17
 */
public class InvalidInputException extends Exception {

    private JTextField offenderField;

    public InvalidInputException(JTextField offenderField) {
        super();
        this.offenderField = offenderField;
    }

    @Override
    public String toString() {
        return "Field Invalid with value: " + offenderField.getText();
    }
}
