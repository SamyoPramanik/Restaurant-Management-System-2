package controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import util.Food;

public class FoodController {

    CustomerHomeController main;
    Food food;

    @FXML
    private Label foodCategory;

    @FXML
    private Label foodName;

    @FXML
    private Label foodPrice;

    @FXML
    private Label foodRestaurant;

    @FXML
    void addToCart(Event event) {
        main.addToCart(food);
    }

    public void setMain(CustomerHomeController main) {
        this.main = main;
    }

    public void set(Food food, String name, String category, String price, String restaurant) {
        this.food = food;
        foodName.setText(name);
        foodCategory.setText(category);
        foodPrice.setText("$ " + price);
        foodRestaurant.setText(restaurant);
    }

}
