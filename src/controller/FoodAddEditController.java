package controller;

import client.RestaurantUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import util.Food;

public class FoodAddEditController {

    private Object main;
    private Food food;
    private int resId;

    @FXML
    private TextField foodCategory;

    @FXML
    private TextField foodName;

    @FXML
    private TextField foodPrice;

    @FXML
    private Button addFoodButton;

    @FXML
    private Button updateFoodButton;

    @FXML
    public Label foodMsg;

    @FXML
    void addFood(ActionEvent event) {
        if (foodName.getText().isEmpty() || foodCategory.getText().isEmpty() || foodPrice.getText().isEmpty()) {
            foodMsg.setText("Please fill all the fields");
            return;
        }

        double foodPriceDouble = 0;

        try {
            foodPriceDouble = Double.parseDouble(foodPrice.getText());
        } catch (Exception e) {
            foodMsg.setText("Please enter a valid price");
            return;
        }

        Food food = new Food(resId, null, foodCategory.getText(), foodName.getText(),
                foodPriceDouble);
        ((RestaurantUser) main).addNewFood(food);
    }

    @FXML
    void updateFood(ActionEvent event) {

    }

    public void setMain(Object main) {
        this.main = main;
    }

    public void set(Food food, int resId) {
        this.resId = resId;

        if (food != null) {
            this.food = food;
            foodName.setText(food.getName());
            foodCategory.setText(food.getCategory());
            foodPrice.setText(food.getPrice() + "");
        }

        if (main instanceof RestaurantUser) {
            updateFoodButton.setVisible(false);
            updateFoodButton.setManaged(false);
        }

        else if (main instanceof FoodController) {
            addFoodButton.setVisible(false);
            addFoodButton.setManaged(false);
        }
    }

}
