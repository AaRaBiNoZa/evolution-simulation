package inhabitants_of_the_world;

import error_handling.Guard;
import randomizing.RandomExtensionBoolean;
import world.Board;
import world.Field;
import world.Parameters;

import java.util.Random;

/**
 * Class representing a Rob
 *
 * @author Adam Al-Hosam
 */
public class Rob {
    private final Program program;
    private int direction; //0 - top, 1 - right, 2 - bottom, 3 - left
    private float energy;
    private int age;
    private Field field;

    /**
     * Constructor that creates a new Rob with parameters specified in
     * starting_parameters and assigns a given field to it. The starting
     * program can mutate during the creation.
     *
     * @param starting_parameters : simulation parameters
     * @param field               : field, on which the Rob will be
     */
    public Rob(Parameters starting_parameters, Field field) {
        Random random = new Random();
        program =
                starting_parameters.getStartingProgram()
                        .mutation(starting_parameters);
        energy = starting_parameters.getStartingEnergy();
        direction = random.nextInt(4);   // random choice of direction
        age = 0;
        this.field = field;
    }

    /**
     * Constructor that creates a Rob's descendant.
     *
     * @param parent              : new Rob's parent
     * @param starting_parameters : simulation parameters
     */
    public Rob(Rob parent, Parameters starting_parameters) {
        this.program = parent.program.mutation(starting_parameters);
        this.energy =
                starting_parameters.getParentsEnergyFraction() *
                        parent.getEnergy();
        this.field = parent.field;
        this.age = 0;
        this.direction = parent.direction > 1
                ? parent.direction - 2
                : parent.direction + 2;
    }

    /**
     * Rotates Rob by 90 degrees left.
     */
    private void instructionLeft() {
        this.direction = (this.direction - 1) < 0
                ? this.direction - 1 + 4
                : this.direction - 1;
    }

    /**
     * Rotates Rob by 90 degrees right.
     */
    private void instructionRight() {
        this.direction = (this.direction + 1) > 3
                ? this.direction + 1 - 4
                : this.direction + 1;
    }

    /**
     * Returns field the Rob is facing.
     *
     * @param board : simulation board
     * @return field, that the Rob is facing
     */
    private Field getNextField(Board board) {
        switch (this.direction) {
            case 0:
                return this.field.getFieldAbove(board);
            case 1:
                return this.field.getFieldToTheRight(board);
            case 2:
                return this.field.getFieldBelow(board);
            case 3:
                return this.field.getFieldToTheLeft(board);
            default:
                Guard.endProgramWithAMessage("Wrong direction");
                return this.field;
        }
    }

    /**
     * Increases the Rob's energy by the amount that the food gives it.
     *
     * @param parameters : simulation parameters
     */
    public void eat(Parameters parameters) {
        if (Float.MAX_VALUE - this.energy < 8) {
            this.energy = Float.MAX_VALUE;
        } else {
            this.energy += parameters.getHowMuchEnergyFoodGives();
        }
    }

    /**
     * Performs a "go" instruction - makes the Rob go onto a field that it's
     * facing.
     *
     * @param board      : simulation board
     * @param parameters : simulation parameters
     */
    private void performGoInstruction(Board board, Parameters parameters) {
        wejdzNaPole(this.getNextField(board), parameters);
    }

    /**
     * Performs each action needed when Rob is going on some field.
     *
     * @param new_field  : field that the Rob is getting on
     * @param parameters : simulation parameters
     */
    private void wejdzNaPole(Field new_field, Parameters parameters) {
        this.field.releaseARob(this);
        new_field.acceptARob(this, parameters);
        this.field = new_field;
    }

    /**
     * Performs a "sniff" instruction - places Rob in the direction of the food
     * if there is any amongst it's neighbours (without diagonals)
     *
     * @param board : simulation_board
     */
    private void performSniffInstruction(Board board) {
        if (this.field.getFieldAbove(board).doesHaveFood()) {
            this.direction = 0;
        } else if (this.field.getFieldToTheRight(board).doesHaveFood()) {
            this.direction = 1;
        } else if (this.field.getFieldBelow(board).doesHaveFood()) {
            this.direction = 2;
        } else if (this.field.getFieldToTheLeft(board).doesHaveFood()) {
            this.direction = 3;
        }
    }

    /**
     * Performs an "eat" instruction.
     * A rob searches for food looking around its neighbours clockwise
     * starting from the one above. If it finds any then the Rob goes to this
     * field. If it doesn't find any, nothing happens.
     *
     * @param board      : simulation board
     * @param parameters : simulation parameters
     */
    private void performEatInstruction(Board board, Parameters parameters) {
        if (this.field.getFieldAbove(board).doesHaveFood()) {
            wejdzNaPole(this.field.getFieldAbove(board), parameters);
        } else if (this.field.getFieldAbove(board).getFieldToTheRight(board)
                .doesHaveFood()) {
            wejdzNaPole(this.field.getFieldAbove(board)
                    .getFieldToTheRight(board), parameters);
        } else if (this.field.getFieldToTheRight(board)
                .doesHaveFood()) {
            wejdzNaPole(this.field.getFieldToTheRight(board), parameters);
        } else if (this.field.getFieldToTheRight(board)
                .getFieldBelow(board).doesHaveFood()) {
            wejdzNaPole(this.field.getFieldToTheRight(board)
                    .getFieldBelow(board), parameters);
        } else if (this.field.getFieldBelow(board).doesHaveFood()) {
            wejdzNaPole(this.field.getFieldBelow(board), parameters);
        } else if (this.field.getFieldBelow(board).getFieldToTheLeft(board)
                .doesHaveFood()) {
            wejdzNaPole(this.field.getFieldBelow(board)
                    .getFieldToTheLeft(board), parameters);
        } else if (this.field.getFieldToTheLeft(board).doesHaveFood()) {
            wejdzNaPole(this.field.getFieldToTheLeft(board), parameters);
        } else if (this.field.getFieldToTheLeft(board)
                .getFieldAbove(board).doesHaveFood()) {
            wejdzNaPole(this.field.getFieldToTheLeft(board)
                    .getFieldAbove(board), parameters);
        }
    }

    /**
     * Interprets an instruction by its id and performs it.
     * After doing it, it takes an appropriate amount of energy.
     *
     * @param id         : instruction id
     * @param parameters : simulation parameters
     * @param board      : simulation board
     */
    private void performInstruction(char id, Parameters parameters,
                                    Board board) {
        assert (energy > 0);
        switch (id) {
            case 'l' -> instructionLeft();
            case 'p' -> instructionRight();
            case 'i' -> performGoInstruction(board, parameters);
            case 'w' -> performSniffInstruction(board);
            case 'j' -> performEatInstruction(board, parameters);
        }
        energy -= 1;
    }

    /**
     * @return returns a field that is assigned to this Rob
     */
    public Field getField() {
        return field;
    }

    /**
     * Executes the whole Rob's program instruction after instruction. If
     * at any moment the energy should fall below 0, then the Rob's energy is
     * set to -1.
     *
     * @param parameters : simulation parameters
     * @param board      : simulation board
     */
    public void executeTheProgram(Parameters parameters, Board board) {
        for (char instruction_id : this.program.getInstructions()) {
            if (energy > 0) {
                performInstruction(instruction_id, parameters, board);
            } else {
                this.energy = -1;
                break;
            }
        }

        if (this.energy < parameters.getRoundCost()) {
            this.energy = -1;
        } else {
            this.energy -= parameters.getRoundCost();
        }
    }

    /**
     * @return Rob's age
     */
    public int getAge() {
        return age;
    }

    /**
     * @return Rob's energy
     */
    public float getEnergy() {
        return energy;
    }

    /**
     * @return number of instructions in Rob's program
     */
    public int getProgramLength() {
        return program.getInstructionsArrayLength();
    }

    /**
     * Method responsible for Rob's duplication. Creates a new Rob
     * from an appropriate constructor and decreases the parent's energy.
     *
     * @param parameters : simulation parameters
     * @return Rob - descendant
     */
    public Rob duplicate(Parameters parameters) {
        Rob result = new Rob(this, parameters);
        this.energy *= (1 - parameters.getParentsEnergyFraction());
        return result;
    }

    /**
     * Simulates Rob's death (removes it from the board)
     */
    public void die() {
        this.field.releaseARob(this);
    }

    /**
     * Checks if a given Rob meets the conditions necessary for duplication.
     *
     * @param parameters : simulation parameters
     * @return true - if it can duplicate, else false
     */
    private boolean canDuplicate(Parameters parameters) {
        return this.energy >= parameters.getDuplicationLimit();
    }

    /**
     * Simulates trying to survive one round by the Rob.
     * First it randomly chooses if the Rob could duplicate and if it can -
     * - does it. Afterwards it executes the program and increases Rob's age.
     * Returns a boolean stating if the Rob survived the round.
     *
     * @param starting_parameters : simulation parameters
     * @param board               : simulation board
     * @return : true if the Rob survived, else false
     */
    public boolean tryToSurviveARound(Parameters starting_parameters,
                                      Board board) {
        RandomExtensionBoolean los = new RandomExtensionBoolean();
        if (canDuplicate(starting_parameters) && los
                .sampleBooleanWithProbability(starting_parameters
                        .getDuplicationProbability())) {
            this.field.placeNextRob(this.duplicate(starting_parameters));
        }
        executeTheProgram(starting_parameters, board);
        this.age++;
        return this.energy >= 0;
    }

    /**
     * Helper method for toString(). Used to change the direction's id to
     * an appropriate string.
     *
     * @return string representing a direction
     */
    private String changeDirectionIdToString() {
        return switch (direction) {
            case 0 -> "top";
            case 1 -> "right";
            case 2 -> "bottom";
            case 3 -> "left";
            default -> "error";
        };
    }

    @Override
    public String toString() {
        return "Rob{" +
                "direction=" + changeDirectionIdToString() +
                ", program=" + program +
                ", energy=" + String.format("%.2f", energy) +
                ", age=" + age +
                ", field=" + field.getCoordinates() +
                '}';
    }
}
