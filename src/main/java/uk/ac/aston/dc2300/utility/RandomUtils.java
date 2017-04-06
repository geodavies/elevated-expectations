package uk.ac.aston.dc2300.utility;

import java.util.Random;

/**
 * This is a utility class to avoid random number generation code duplication
 *
 * @author George Davies
 * @since 06/04/17.
 */
public class RandomUtils {

    private final Random RANDOM;

    /**
     * @param seed the seed to initialize the random with
     */
    public RandomUtils(long seed) {
        RANDOM = new Random(seed);
    }

    /**
     * Gets a random int between a minimum and maximum value inclusive of both.
     *
     * @param min the minimum value
     * @param max the maximum value
     * @return the random int
     */
    public int getIntInRange(int min, int max) {
        return RANDOM.nextInt((max - min) + 1) + min;
    }

}
