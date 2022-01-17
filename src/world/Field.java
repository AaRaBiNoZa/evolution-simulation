package world;

import inhabitants_of_the_world.Rob;

import java.util.LinkedList;

/**
 * Class representing a single field in the simulation
 *
 * @author Adam Al-Hosam
 */
public abstract class Field {
    private final int row;
    private final int column;
    // A list of Rob's being on this field at the moment
    private LinkedList<Rob> robs;
    // this field helps not to count the same robs during one round
    // for example when they move from one field to the other
    private int rob_count_on_round_start;

    /**
     * Constructor that creates a Field object with given coordinates and
     * an empty rob list.
     *
     * @param row    : field's x coordinate
     * @param column : field's y coordinate
     */
    public Field(int row, int column) {
        this.robs = new LinkedList<Rob>();
        rob_count_on_round_start = 0;
        this.row = row;
        this.column = column;
    }

    /**
     * Places a given Rob objects in the beginning of rob list.
     *
     * @param new_rob : Rob object to place
     */
    public void placeNextRob(Rob new_rob) {
        this.robs.addFirst(new_rob);
    }

    /**
     * Places a given rob on the beginning of rob list and does every
     * action related to both objects interaction (Rob and Field).
     * Differs from placeNextRob by the fact, that in that method
     * we ignore every logical/resulting from specification consequence of
     * Rob going on a field.
     *
     * @param rob_to_accept : rob that is going on this field
     * @param parameters    : simulation parameters
     */
    public void acceptARob(Rob rob_to_accept, Parameters parameters) {
        this.robs.addFirst(rob_to_accept);
    }

    /**
     * Removes a given Rob from rob list
     *
     * @param rob : rob to remove
     */
    public void releaseARob(Rob rob) {
        robs.remove(rob);
    }

    /**
     * The next 4 methods are very similar.
     * I am using the fact that board's edges are "connected".
     * (top <-> bottom,  left <-> right).
     * It is similar to using modulo, but I need the remainder to always be
     * positive, also I am using the fact that a value change can only
     * make the number be at most 1 over the boundaries.
     *
     * @param board : simulation board
     * @return field in an appropriate position in relation to this
     */
    public Field getFieldAbove(Board board) {
        int new_row = this.row - 1 < 0
                ? this.row - 1 + board.getRowCount()
                : this.row - 1;
        return board.getField(new_row, this.column);
    }

    public Field getFieldBelow(Board board) {
        int new_row = this.row + 1 >= board.getRowCount()
                ? this.row + 1 - board.getRowCount()
                : this.row + 1;
        return board.getField(new_row, this.column);
    }

    public Field getFieldToTheRight(Board board) {
        int new_column = this.column + 1 >= board.getColumnCount()
                ? this.column + 1 - board.getColumnCount()
                : this.column + 1;
        return board.getField(this.row, new_column);
    }

    public Field getFieldToTheLeft(Board board) {
        int new_column = this.column - 1 < 0
                ? this.column - 1 + board.getColumnCount()
                : this.column - 1;
        return board.getField(this.row, new_column);
    }

    /**
     * Sends a message to each rob in rob list, so they can perform one round.
     * We take robs from the ending and put new ones in the beginning.
     * Thanks to that and knowing therob count in the beginning we don't
     * create any loops/weird behaviours such as a situation where a Rob
     * repeats its actions after going to a new field. Also, we ensure that
     * if a Rob didn't move (and survived the round) then this object is
     * responsible to add him to the rob list.
     * If Rob doesn't survive the round, it dies.
     *
     * @param parameters : simulation parameters
     * @param board      : simulation board
     */
    public void performARound(Parameters parameters, Board board) {
        for (int i = 0; i < rob_count_on_round_start; i++) {
            Rob current_rob = this.robs.removeLast();
            if (!current_rob.tryToSurviveARound(parameters, board)) {
                current_rob.die();
            } else if (this.robs.peekFirst() != current_rob &&
                    current_rob.getField() == this && current_rob
                    .getEnergy() != -1) {
                this.placeNextRob(current_rob);
            }
        }
    }

    /**
     * Prepares the field before the next round
     *
     * @param p : simulation parameters
     */
    public void updateFieldState(Parameters p) {
        this.rob_count_on_round_start = robs.size();
    }

    /**
     * @return length of rob list
     */
    public int getRobCount() {
        return this.robs.size();
    }

    /**
     * toArray method creates a safe copy, so I can be sure that when
     * passing its result the robs field will not change - only the owner
     * of the attribute can change it
     *
     * @return an array with a safe robs attribute copy
     */
    public Rob[] robsCopyArray() {
        return robs.toArray(new Rob[0]);
    }

    /**
     * @return true - if there is food on this field, else false
     */
    public boolean doesHaveFood() {
        return false;
    }

    /**
     * Method to output the simulation statistics. Prints robs state one by one
     * from robs list.
     */
    public void printRobsStates() {
        for (Rob rob : robs) {
            System.out.println(rob);
        }
    }

    /**
     * @return Coordinates this field on the board as a string.
     */
    public String getCoordinates() {
        return "row: " + (row + 1) + ", column: " + (column + 1);
    }
}
