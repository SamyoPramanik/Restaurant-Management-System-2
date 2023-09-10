package requests;

import java.io.Serializable;

import util.Food;

public class UpdateFood implements Serializable {
    public Food food, updateFood;

    public UpdateFood(Food food, Food updateFood) {
        this.food = food;
        this.updateFood = updateFood;
    }

}
