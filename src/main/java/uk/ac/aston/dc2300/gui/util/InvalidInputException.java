package uk.ac.aston.dc2300.gui.util;

/**
 * An exception to be thrown if a field is invalid upon submission.
 *
 * @author Dan Cotton
 * @since 13/04/17
 */
public class InvalidInputException extends Exception {

    private String offenderValue;

    /**
     * An exception to be thrown if a field is invalid
     * upon submission.
     *
     * @param offenderValue the String value.
     */
    public InvalidInputException(String offenderValue) {
        super();
        this.offenderValue = offenderValue;
    }

    @Override
    public String toString() {
        return "Field Invalid with value: " + offenderValue;
    }
}
