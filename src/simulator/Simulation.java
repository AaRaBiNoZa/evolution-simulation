package simulator;

import error_handling.Guard;
import world.Board;
import world.Parameters;

import java.io.FileNotFoundException;


public class Simulation {

    public static void main(String[] args) {
        try {
            Board world = new Board(args[0]);
            Parameters world_parameters = new Parameters(args[1]);
            if (!world_parameters.getStartingProgram()
                    .doesOnlyHaveValidInstructions(world_parameters)) {
                Guard.endProgramWithAMessage("There are instructions in the " +
                        "starting program that do not exist in " +
                        "valid_instructions\n");
            }
            world.performTheSimulation(world_parameters);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
