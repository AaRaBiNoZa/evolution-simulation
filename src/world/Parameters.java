package world;

import error_handling.Guard;
import inhabitants_of_the_world.Program;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Class that represents a set of parameters for the simulation.
 *
 * @author Adam Al-Hosam
 */
public class Parameters {
    private int how_many_rounds;
    private int how_many_robs_on_start;
    private float starting_energy;
    private float how_much_energy_food_gives;
    private int how_long_does_food_grow;
    private float round_cost;
    private float duplication_probability;
    private float parents_energy_fraction;
    private float duplication_limit;
    private int how_often_to_print;
    private float probability_of_removing_instr;
    private float probability_of_adding_instr;
    private float probability_of_changing_instr;
    private Program starting_program;
    private Program valid_instructions;

    /**
     * Constructor made in a way that enables expanding by new parameters.
     * It reads the data from path_to_file. Determines if the parameters
     * are valid and don't violate the specifications.
     *
     * @param path_to_file    : a valid path file with parameters
     * @param parameter_count : number of parameters
     */
    public Parameters(String path_to_file, int parameter_count)
            throws FileNotFoundException {
        int counter = 0;

        String input_line;
        String[] data;
        Scanner sc = new Scanner(new File(path_to_file)).useDelimiter("\n");

        while (counter != parameter_count && sc.hasNext()) {
            input_line = sc.next();
            data = input_line.split(" ", 2);

            // if  starting_program or valid_instructions are empty
            if (data.length == 1) {
                if (input_line.equals("starting_program ")) {
                    starting_program = new Program("");
                    counter++;
                    continue;
                } else if (input_line.equals("valid_instructions ")) {
                    valid_instructions = new Program("");
                    counter++;
                    continue;
                }
            }

            if (data.length != 2 || !setParameter(data[0], data[1])) {
                Guard.endProgramWithAMessage("Not valid " +
                        "parameters value - " + input_line);
            }
            counter++;
        }

        if (counter != parameter_count || sc.hasNext()) {
            Guard.endProgramWithAMessage("Not valid " +
                    "parameter count");
        }
        sc.close();
    }

    /**
     * Constructor that constructs the parameters assuming it's the basic
     * version of the simulation (no extensions)
     *
     * @param path_to_file : valid path to file with parameters
     */
    public Parameters(String path_to_file) throws FileNotFoundException {
        this(path_to_file, 15);
    }

    /**
     * Determines if each character from valid_instructions given in
     * parameters is allowed. (l,p,i,w,j).
     * If it sees any not allowed character, it ends the loop and returns false.
     *
     * @param instructions : [string] instructions read from parameters file
     * @return do all the characters match any of the available chars
     */
    private boolean czyInstrukcjeSaPoprawne(String instructions) {
        String availableChars = "lpiwj";
        for (char ch : instructions.toCharArray()) {
            if (!availableChars.contains("" + ch))
                return false;
        }

        return true;
    }

    /**
     * Method used to check if read probability/fraction values are valid.
     * The values need to be in the [0,1] interval.
     *
     * @param w : value being checked
     * @return is the value valid
     */
    private boolean isReadFloatValid(float w) {
        return 0 <= w && w <= 1;
    }

    /**
     * Checks if a given string can be safely read as float.
     *
     * @param str : string to check
     * @return true - if the string can be safely read as float, else false
     */
    private boolean isFloat(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if a given string can be safely read as int.
     *
     * @param str : string to check
     * @return true - if the string can be safely read as int, else false
     */
    private boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Method that based on a parameter name sets its value, while checking
     * if it is valid.
     * If in the input data there is a parameter that doesn't exist in
     * this class' attributes, then false is returned.
     *
     * @param parameter_name : name of the parameter that is being read
     * @param value          : object that represents a file scanner for
     *                       parameters
     * @return true if it was possible to set the parameter
     */
    private boolean setParameter(String parameter_name, String value) {
        switch (parameter_name) {
            case "how_many_rounds":
                if (isInt(value)) {
                    how_many_rounds = Integer.parseInt(value);
                    return how_many_rounds > 0;
                }
                return false;
            case "how_many_robs_on_start":
                if (isInt(value)) {
                    how_many_robs_on_start = Integer.parseInt(value);
                    return how_many_robs_on_start >= 0;
                }
                return false;
            case "starting_energy":
                if (isFloat(value)) {
                    starting_energy = Float.parseFloat(value);
                    return starting_energy > 0;
                }
                return false;
            case "how_much_energy_food_gives":
                if (isFloat(value)) {
                    how_much_energy_food_gives = Float.parseFloat(value);
                    return how_much_energy_food_gives >= 0;
                }
                return false;
            case "how_long_does_food_grow":
                if (isInt(value)) {
                    how_long_does_food_grow = Integer.parseInt(value);
                    return how_long_does_food_grow > 0;
                }
                return false;
            case "round_cost":
                if (isFloat(value)) {
                    round_cost = Float.parseFloat(value);
                    return round_cost >= 0;
                }
                return false;
            case "duplication_probability":
                if (isFloat(value)) {
                    duplication_probability = Float.parseFloat(value);
                    return isReadFloatValid(duplication_probability);
                }
                return false;
            case "parents_energy_fraction":
                if (isFloat(value)) {
                    parents_energy_fraction = Float.parseFloat(value);
                    return isReadFloatValid(parents_energy_fraction);
                }
                return false;
            case "duplication_limit":
                if (isFloat(value)) {
                    duplication_limit = Float.parseFloat(value);
                    return duplication_limit >= 0;
                }
                return false;
            case "starting_program":
                if (czyInstrukcjeSaPoprawne(value)) {
                    starting_program = new Program(value);
                    return true;
                }
                return false;
            case "how_often_to_print":
                if (isInt(value)) {
                    how_often_to_print = Integer.parseInt(value);
                    return how_often_to_print > 0;
                }
                return false;
            case "probability_of_removing_instr":
                if (isFloat(value)) {
                    probability_of_removing_instr = Float.parseFloat(value);
                    return isReadFloatValid(probability_of_removing_instr);
                }
                return false;
            case "probability_of_adding_instr":
                if (isFloat(value)) {
                    probability_of_adding_instr = Float.parseFloat(value);
                    return isReadFloatValid(probability_of_adding_instr);
                }
                return false;
            case "probability_of_changing_instr":
                if (isFloat(value)) {
                    probability_of_changing_instr = Float.parseFloat(value);
                    return isReadFloatValid(probability_of_changing_instr);
                }
                return false;
            case "valid_instructions":
                if (czyInstrukcjeSaPoprawne(value)) {
                    valid_instructions = new Program(value);
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    /**
     * @return how_often_to_print
     */
    public int getHowOftenToPrint() {
        return how_often_to_print;
    }

    /**
     * @return probability_of_removing_instr
     */
    public float getProbabilityOfRemovingInstr() {
        return probability_of_removing_instr;
    }

    /**
     * @return probability_of_adding_instr
     */
    public float getProbabilityOfAddingInstr() {
        return probability_of_adding_instr;
    }

    /**
     * @return probability_of_changing_instr
     */
    public float getProbabilityOfChangingInstr() {
        return probability_of_changing_instr;
    }

    /**
     * @return how_long_does_food_grow
     */
    public int getHowLongDoesFoodGrow() {
        return how_long_does_food_grow;
    }

    /**
     * @return how_many_rounds
     */
    public int getHowManyRounds() {
        return how_many_rounds;
    }

    /**
     * @return how_many_robs_on_start
     */
    public int getHowManyRobsOnStart() {
        return how_many_robs_on_start;
    }

    /**
     * @return starting_program
     */
    public Program getStartingProgram() {
        return starting_program;
    }

    /**
     * @return starting_energy
     */
    public float getStartingEnergy() {
        return starting_energy;
    }

    /**
     * @return how_much_energy_food_gives
     */
    public float getHowMuchEnergyFoodGives() {
        return how_much_energy_food_gives;
    }

    /**
     * @return round_cost
     */
    public float getRoundCost() {
        return round_cost;
    }

    /**
     * @return duplication_probability
     */
    public float getDuplicationProbability() {
        return duplication_probability;
    }

    /**
     * @return parents_energy_fraction
     */
    public float getParentsEnergyFraction() {
        return parents_energy_fraction;
    }

    /**
     * @return duplication_limit
     */
    public float getDuplicationLimit() {
        return duplication_limit;
    }

    /**
     * @return valid_instructions
     */
    public Program getValidInstructions() {
        return valid_instructions;
    }
}
