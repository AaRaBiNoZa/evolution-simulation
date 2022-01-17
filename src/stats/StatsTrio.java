package stats;

/**
 * Class representing information (minimum, maximum and sum) about
 * some data.
 *
 * @author Adam Al-Hosam
 */
public class StatsTrio {
    private final String name;
    private float min;
    private float max;
    private float sum;

    /**
     * Constructor that creates a new object with min set as MAX_VALUE, because
     * when we start comparing the data (trying to find min), we want the first
     * value to update those fields. (similar when finding max).
     *
     * @param name : name of the data the stats are about
     */
    public StatsTrio(String name) {
        this.name = name;
        min = Integer.MAX_VALUE;
        max = Integer.MIN_VALUE;
        sum = 0;
    }

    /**
     * @return minimum value from the data
     */
    public float getMin() {
        return min;
    }

    /**
     * @return maximum value from the data
     */
    public float getMax() {
        return max;
    }

    /**
     * @return sum of the data
     */
    public float getSum() {
        return sum;
    }

    /**
     * Computes and returns a mean from the data (after receiving count
     * of the values)
     *
     * @return a mean of the data
     */
    public float mean(int count) {
        if (count == 0) {
            return 0;
        }
        return sum / count;
    }

    /**
     * Clears the stats before the next update
     */
    public void reset() {
        this.min = Integer.MAX_VALUE;
        this.max = Integer.MIN_VALUE;
        this.sum = 0;
    }

    /**
     * Updates the stats by a given value (adds to the  sum, compares with min
     * and max)
     *
     * @param value : value, with which the data is updated
     */
    public void updateByAValue(float value) {
        if (value < min) {
            min = value;
        }
        if (value > max) {
            max = value;
        }
        sum += value;
    }

    /**
     * Represents an object as a string.
     *
     * @param count : count of values in the data
     * @return string representing the stats
     */
    public String computeAndReturnString(int count) {
        if (count == 0) {
            min = 0;
            max = 0;
        }
        return name + ": " + String.format("%.2f", min) + "/" +
                String.format("%.2f", mean(count)) + "/" +
                String.format("%.2f", max);
    }
}
