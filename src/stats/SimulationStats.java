package stats;

import inhabitants_of_the_world.Rob;
import world.Field;

/**
 * Class representing statistics of a simulation.
 *
 * @author Adam Al-Hosam
 */
public class SimulationStats {
    private final StatsTrio robs_program_length, robs_energy, robs_age;
    private int round_number;
    private int rob_count;
    private int fields_with_food_count;

    /**
     * Constructor that creates up-to-date statistics from simulation with
     * appropriate fields.
     */
    public SimulationStats() {
        round_number = 0;
        rob_count = 0;
        fields_with_food_count = 0;
        robs_program_length = new StatsTrio("prg");
        robs_energy = new StatsTrio("energ");
        robs_age = new StatsTrio("age");
    }

    /**
     * Updates the round number.
     */
    public void increaseRoundNumber() {
        round_number++;
    }

    /**
     * Clears the stats before collecting new ones.
     */
    public void clearTheStats() {
        rob_count = 0;
        fields_with_food_count = 0;
        robs_program_length.reset();
        robs_energy.reset();
        robs_age.reset();
    }

    /**
     * Updates statistics by a given field.
     *
     * @param field : field used to update statistics
     */
    public void updateByAField(Field field) {
        rob_count += field.getRobCount();

        if (field.doesHaveFood()) {
            fields_with_food_count += 1;
        }

        for (Rob current_rob : field.robsCopyArray()) {
            robs_program_length.updateByAValue(current_rob.getProgramLength());
            robs_energy.updateByAValue(current_rob.getEnergy());
            robs_age.updateByAValue(current_rob.getAge());
        }
    }

    /**
     * Returns this object's string representation in a specified before format.
     *
     * @return : string that represents this object.
     */
    @Override
    public String toString() {
        return round_number +
                ", rob: " + rob_count +
                ", food_fields: " + fields_with_food_count + ", " +
                robs_program_length.computeAndReturnString(rob_count) +
                ", " + robs_energy.computeAndReturnString(rob_count) +
                ", " + robs_age.computeAndReturnString(rob_count);
    }
}

