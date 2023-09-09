package requests;

import java.io.Serializable;

import util.Food;

public class AddFood implements Serializable {
    public Food food;

    public AddFood(Food food) {
        this.food = food;
    }

}
