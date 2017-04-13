package uk.ac.aston.dc2300.gui.util;

import javax.swing.*;

/**
 * Created by dan on 13/04/2017.
 */
public class InvalidInputException extends Exception {

    private JTextField offenderField;

    /**
     * An exception to be thrown if a field is invalid
     * upon submission.
     *
     * @param offenderField the JComponent with invalid input.
    */
    public InvalidInputException(JTextField offenderField) {
        super();
        this.offenderField = offenderField;
    }

    @Override
    public String toString() {
        return "Field Invalid with value: " + offenderField.getText();
    }
}
