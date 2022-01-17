package inhabitants_of_the_world;

import error_handling.Guard;
import randomizing.RandomExtensionBoolean;
import world.Parameters;

import java.util.Arrays;
import java.util.Random;

/**
 * Class representing a Rob's program
 *
 * @author Adam Al-Hosam
 */
public class Program {
    private final char[] instructions;

    /**
     * Constructor that creates a new object from a given string.
     *
     * @param input : a string representing instructions
     */
    public Program(String input) {
        instructions = input.toCharArray();
    }

    /**
     * Constructor that creates a new object from a given char array
     * (instructions).
     *
     * @param instructions : instructions array
     */
    public Program(char[] instructions) {
        this.instructions = instructions.clone();
    }

    /**
     * A safe getter - returns a copy of the array.
     *
     * @return a copy of instructions array
     */
    public char[] getInstructions() {
        return instructions.clone();
    }

    /**
     * @return length of the instructions array
     */
    public int getInstructionsArrayLength() {
        return instructions.length;
    }

    /**
     * Performs a mutation on this object and returns a new object representing
     * a program after a mutation. First it uses RandomExtensionBoolean to
     * choose which mutations will take place and then checks different
     * combinations  of mutations. Finally, it copies the beginning of
     * the instructions Array.
     *
     * @param parameters : simulation parameters
     * @return : a mutated program
     */
    public Program mutation(Parameters parameters) {
        // if instructions are empty then a mutation doesn't make sense
        if (parameters.getValidInstructions()
                .getInstructionsArrayLength() == 0) {
            return new Program("");
        }

        RandomExtensionBoolean randomizer = new RandomExtensionBoolean();
        boolean should_remove, should_add, should_change;
        should_remove = instructions.length > 0 &&
                randomizer.sampleBooleanWithProbability(parameters
                        .getProbabilityOfRemovingInstr());
        should_add =
                randomizer.sampleBooleanWithProbability(parameters
                        .getProbabilityOfAddingInstr());
        should_change =
                randomizer.sampleBooleanWithProbability(parameters
                        .getProbabilityOfChangingInstr());

        // setting a new array such that last 1-2 elements are placed
        // so I can mass-copy the array
        char[] new_instructions;
        if (should_add && should_remove) {
            new_instructions = new char[instructions.length];
            new_instructions[new_instructions.length - 1] =
                    parameters.getValidInstructions().getRandomInstruction();
        } else if (should_add) { // should_add && !should_remove
            new_instructions = new char[instructions.length + 1];
            new_instructions[new_instructions.length - 1] =
                    parameters.getValidInstructions().getRandomInstruction();

            if (instructions.length > 0) {
                new_instructions[new_instructions.length - 2] =
                        instructions[instructions.length - 1];
            }
        } else if (should_remove) { //!should_add && should_remove
            new_instructions = new char[instructions.length - 1];
        } else {          // !should_add && !should_remove
            new_instructions = new char[instructions.length];
            if (new_instructions.length > 0) {
                new_instructions[new_instructions.length - 1] =
                        instructions[instructions.length - 1];
            }
        }

        for (int i = 0; i < instructions.length - 1; i++) {
            new_instructions[i] = this.instructions[i];
        }

        if (should_change && new_instructions.length > 0) {
            new_instructions[randomizer.nextInt(new_instructions.length)] =
                    parameters.getValidInstructions().getRandomInstruction();
        }

        return new Program(new_instructions);
    }

    /**
     * Randomly chooses an instruction from a given program - helper function.
     *
     * @return : random instruction id
     */
    private char getRandomInstruction() {
        Random random = new Random();
        if (this.instructions.length <= 0) {
            Guard.endProgramWithAMessage("getRandomInstruction cannot" +
                    " execute");
        }

        return this.instructions[random.nextInt(instructions.length)];
    }

    /**
     * Determines if a given program has only instructions from
     * valid_instructions
     *
     * @param parameters : simulation parameters
     * @return true - if there are only valid instructions, else false
     */
    public boolean doesOnlyHaveValidInstructions(Parameters parameters) {
        String instructions_as_string =
                String.valueOf(parameters.getValidInstructions().instructions);

        for (int i = 0; i < this.getInstructionsArrayLength(); i++) {
            if (!instructions_as_string.contains(this.instructions[i] + "")) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return Program's representation as a string
     */
    @Override
    public String toString() {
        return Arrays.toString(instructions);
    }

    /**
     * @return copy of the object
     */
    @Override
    public Program clone() {
        return new Program(instructions);
    }
}

