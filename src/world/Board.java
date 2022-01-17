package world;

import error_handling.Guard;
import inhabitants_of_the_world.Rob;
import stats.SimulationStats;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

/**
 * Class that represents a Board/Universe, in which the simulation takes place.
 *
 * @author Adam Al-Hosam
 */
public class Board {
    private final SimulationStats stats;
    private final Field[][] fields;
    private int row_count;
    private int column_count;

    /**
     * Constructor that reads a board from file with a specified path.
     * Determines if each row is of the  same length.
     * Fills it's fields attribute with an appropriate subclass of Field object.
     *
     * @param path_to_file : a valid file path
     */
    public Board(String path_to_file) throws FileNotFoundException {
        stats = new SimulationStats();
        computeColumnsCount(path_to_file);
        computeRowCount(path_to_file);
        fields = new Field[row_count][column_count];

        if (column_count <= 0 || row_count <= 0) {
            Guard.endProgramWithAMessage("Not valid board dimensions");
        }

        char[] current_row;

        // wczytywanie row po wierszu
        Scanner sc = new Scanner(new File(path_to_file)).useDelimiter("\n");
        for (int row = 0; row < row_count; row++) {
            current_row = sc.next().toCharArray();

            if (current_row.length != column_count) {
                Guard.endProgramWithAMessage("Row lengths vary");
            }

            for (int column = 0; column < column_count; column++) {
                if (!" x".contains(current_row[column] + "")) {
                    Guard.endProgramWithAMessage("Forbidden " +
                            "char in board's representation in file: " +
                            path_to_file);
                }

                fields[row][column] = current_row[column] == 'x'
                        ? new FieldWithFood(row, column)
                        : new EmptyField(row, column);
            }
        }
        sc.close();
    }

    /**
     * Opens the file, read first line's char count and sets an appropriate
     * attribute (column_count)
     *
     * @param path_to_file : path to a file with the board's representation
     * @throws FileNotFoundException : if the path is not valid
     */
    private void computeColumnsCount(String path_to_file) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(path_to_file)).useDelimiter("\n");
        column_count = sc.next().length();
        sc.close();
    }

    /**
     * Opens a file, reads the number of rows and sets  row_count.
     *
     * @param path_to_file : path to a file with the board's representation
     * @throws FileNotFoundException : if the path is not valid
     */
    private void computeRowCount(String path_to_file) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(path_to_file)).useDelimiter("\n");
        int counter;
        for (counter = 0; sc.hasNext(); counter++) {
            sc.next();
        }
        sc.close();
        row_count = counter;
    }

    /**
     * Randomly samples as many pairs of row and column, as many robs there
     * should exist in the beginning of the simulations. For each such pair
     * it creates a new Rob with given parameters.
     *
     * @param parameters : simulation parameters
     */
    private void createAndPlaceRobs(Parameters parameters) {
        Random random = new Random();
        int row, column;
        for (int i = 0; i < parameters.getHowManyRobsOnStart(); i++) {
            row = random.nextInt(row_count);
            column = random.nextInt(column_count);

            fields[row][column].placeNextRob(new Rob(parameters,
                    fields[row][column]));
        }
    }

    /**
     * Goes through every field and collects information about it.
     * Updates stats attribute.
     * Updates the field before the next round.
     *
     * @param parameters : simulation parameters (needed to update field with
     *                   food)
     */
    private void collectStatsAndUpdateFields(Parameters parameters) {
        stats.clearTheStats();

        Field current_field;
        for (int row = 0; row < row_count; row++) {
            for (int column = 0; column < column_count; column++) {
                current_field = getField(row, column);
                current_field.updateFieldState(parameters);
                stats.updateByAField(current_field);
            }
        }
    }

    /**
     * Performs a round's actions on every field.
     * Updates round number in stats object.
     *
     * @param parameters : simulation parameters
     */
    private void wykonajTure(Parameters parameters) {
        for (int row = 0; row < row_count; row++) {
            for (int column = 0; column < column_count; column++) {
                getField(row, column).performARound(parameters, this);
            }
        }
        stats.increaseRoundNumber();
    }

    /**
     * Method that executes once every how_often_to_print. Prints out
     * states of all robs in the simulation.
     */
    private void printOutSimulationState() {
        System.out.println("\nSIMULATION STATE");
        System.out.println(
                "-----------------------------------------------------------" +
                        "---------------------------");
        for (int row = 0; row < row_count; row++) {
            for (int column = 0; column < column_count; column++) {
                getField(row, column).printRobsStates();
            }
        }
        System.out.println(
                "-----------------------------------------------------------" +
                        "---------------------------\n");
    }

    /**
     * Method that prints out basic information after each round.
     */
    private void printBasicInfo() {
        System.out.println(stats);
    }

    /**
     * Performs a simulation with given parameters.
     * Creates and places robs on the board and then prepares starting stats
     * and  prints them out. After that performs how_many_rounds rounds.
     * int helper helps to control printing the simulation's state after
     * every how_often_to_print rounds.
     * After each round, the program collects simulation stats and prints
     * basic info.
     *
     * @param parameters : simulation parameters
     */
    public void performTheSimulation(Parameters parameters) {
        createAndPlaceRobs(parameters);
        int helper = 1;
        collectStatsAndUpdateFields(parameters);
        printOutSimulationState();
        for (int i = 0; i < parameters.getHowManyRounds(); i++, helper++) {
            wykonajTure(parameters);

            collectStatsAndUpdateFields(parameters);
            printBasicInfo();

            if (helper == parameters.getHowOftenToPrint()) {
                printOutSimulationState();
                helper = 0;
            }
        }
        if (helper != 1) {
            printOutSimulationState();
        }
    }

    /**
     * @return board's row_count
     */
    public int getRowCount() {
        return this.row_count;
    }

    /**
     * @return board's column_count
     */
    public int getColumnCount() {
        return this.column_count;
    }

    /**
     * Returns a reference to a particular field from the board.
     * CAUTION: this method returns a particular object, not it's copy.
     *
     * @param row    : fields x coordinate
     * @param column : fields y coordinate
     * @return a field with given coordinates
     */
    public Field getField(int row, int column) {
        return fields[row][column];
    }
}
