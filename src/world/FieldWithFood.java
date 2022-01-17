package world;

import inhabitants_of_the_world.Rob;

/**
 * Subclass representing a field with food
 *
 * @author Adam Al-Hosam
 */
public class FieldWithFood extends Field {
    // controls how many rounds passed since the last person ate something on
    // this field
    private int is_food_growing;
    private boolean is_food_ready;

    /**
     * Constructor that creates an empty field with food with given coordinates.
     *
     * @param row    : field's x coordinate
     * @param column : field's y coordinate
     */
    public FieldWithFood(int row, int column) {
        super(row, column);
        is_food_growing = 0;
        is_food_ready = true;
    }

    /**
     * Gets called after a Rob eats on this field.
     */
    public void startGrowingFood() {
        is_food_ready = false;
        is_food_growing = 0;
    }

    /**
     * Extends a method from superclass by updating the state of food growth.
     *
     * @param p : simulation parameters
     */
    public void updateFieldsState(Parameters p) {
        super.updateFieldState(p);
        if (is_food_growing == p.getHowLongDoesFoodGrow()) {
            is_food_ready = true;
            is_food_growing = 0;
        } else {
            is_food_growing++;
        }
    }

    /**
     * Extends a method from superclass by action that should be done if
     * there is food on the field.
     *
     * @param rob_to_accept : rob that wants to go on this field
     * @param parameters    : simulation parameters
     */
    @Override
    public void acceptARob(Rob rob_to_accept, Parameters parameters) {
        super.acceptARob(rob_to_accept, parameters);
        if (doesHaveFood()) {
            rob_to_accept.eat(parameters);
            startGrowingFood();
        }
    }

    /**
     * @return is there food on the field
     */
    @Override
    public boolean doesHaveFood() {
        return is_food_ready;
    }
}
