package randomizing;

import java.util.Random;

/**
 * Extends Random class by a method for sampling a boolean with given
 * probability.
 *
 * @author Adam Al-Hosam
 */
public class RandomExtensionBoolean extends Random {

    /**
     * Constructor that just calls superclass constructor.
     */
    public RandomExtensionBoolean() {
        super();
    }

    /**
     * Returns true with pr chance.
     *
     * @param pr : probability to return true
     * @return result of sampling
     */
    public boolean sampleBooleanWithProbability(float pr) {
        return nextFloat() < pr;
    }
}
