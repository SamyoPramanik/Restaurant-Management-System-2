package controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import util.Food;

public class FoodController {

    Object main;
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
    public Button addButton;

    @FXML
    public Button removeButton;

    @FXML
    void addToCart(Event event) {
        ((CustomerHomeController) main).addToCart(food);
    }

    @FXML
    void removeFromCart(ActionEvent event) {
        ((CartController) main).removeFromCart(food);
    }

    public void setMain(Object main) {
        this.main = main;
    }

    public void set(Food food, String name, String category, String price, String restaurant) {
        this.food = food;
        foodName.setText(name);
        foodCategory.setText(category);
        foodPrice.setText("$" + price);
        foodRestaurant.setText(restaurant);
    }

}
