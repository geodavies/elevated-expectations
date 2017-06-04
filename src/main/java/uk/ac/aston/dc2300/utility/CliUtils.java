package uk.ac.aston.dc2300.utility;

import java.math.BigDecimal;
import java.util.Scanner;

/**
 * A utility class to perform repetitive CLI operations.
 *
 * @author Dan Cotton
 * @since 08/04/17
 */
public class CliUtils {

    private final Scanner cliScanner;

    public CliUtils () {
        cliScanner = new Scanner(System.in);
    }

    /**
     * Uses CLI to gain integer input from user. Will prompt, verify and return result or defaultValue where appropriate.
     *
     * @param prompt a brief description of the field (used as prompt)
     * @param defaultValue the default integer to be used if input not provided
     * @return the integer result as decided by the user
     */
    public int readIntegerViaCli(String prompt, int defaultValue) {
        int result = -1;
        while (result == -1) {
            System.out.printf(prompt + " [" + defaultValue + "]: ");
            // Get user input
            String resultInput = cliScanner.nextLine();
            // If left empty, set to default value
            if (resultInput.equals("")) {
                result = defaultValue;
            } else {
                try {
                    // Check if valid int
                    int validationInt = Integer.parseInt(resultInput);
                    // Check greater than 0
                    if(validationInt < 0){
                        System.out.println("Must be greater than or equal to 0");
                        continue;
                    }
                    result = validationInt;
                } catch (NumberFormatException e) {
                    System.out.println("Must be a valid whole number");
                }
            }
        }
        return result;
    }

    /**
     * Uses CLI to gain long input from user. Will prompt, verify
     * and return result or defaultValue where appropriate.
     *
     * @param prompt a brief description of the field (used as prompt)
     * @param defaultValue the default long to be used if input not provided
     * @return the long result as decided by the user
     */
    public long readLongViaCli(String prompt, long defaultValue) {
        long result = -1;
        while (result == -1) {
            System.out.printf(prompt + " [" + defaultValue + "]: ");
            // Get user input
            String resultInput = cliScanner.nextLine();
            // If left empty, set to default value
            if (resultInput.equals("")) {
                result = defaultValue;
            } else {
                try {
                    // Check if valid long
                    long validationLong = Long.parseLong(resultInput);
                    result = validationLong;
                } catch (NumberFormatException e) {
                    System.out.println("Must be a valid whole number");
                }
            }
        }
        return result;
    }

    /**
     * Uses CLI to gain BigDecimal input from user. Will prompt, verify
     * and return result or defaultValue where appropriate.
     *
     * @param prompt a brief description of the field (used as prompt)
     * @param defaultValue the default BigDecimal to be used if input not provided
     * @return the BigDecimal result as decided by the user
     */
    public BigDecimal readBigDecimalViaCli(String prompt, BigDecimal defaultValue) {
        BigDecimal result = null;
        while (result == null) {
            System.out.printf(prompt + " [" + defaultValue + "]: ");
            // Get user input
            String resultInput = cliScanner.nextLine();
            // If left empty, set to default value
            if (resultInput.isEmpty()) {
                result = defaultValue;
            } else {
                try {
                    // Check value is valid decimal
                    BigDecimal validationDecimal = new BigDecimal(resultInput);
                    // Check value is between 0 and 1
                    if (validationDecimal.compareTo(BigDecimal.ZERO) < 0 || validationDecimal.compareTo(BigDecimal.ONE) > 0) {
                        System.out.println("Probability must be a decimal between 0 and 1");
                        continue;
                    }
                    result = validationDecimal;
                } catch (NumberFormatException e) {
                    System.out.println("Probability must be a decimal between 0 and 1");
                }
            }
        }
        return result;
    }
}
