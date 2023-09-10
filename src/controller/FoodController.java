package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import util.Food;
import util.Order;

public class FoodController {

    Object main;
    Food food;
    Order order;

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
    private Button editButton;

    @FXML
    private Button deliverButton;

    @FXML
    void addToCart(Event event) {
        ((CustomerHomeController) main).addToCart(food);
    }

    @FXML
    void removeFromCart(ActionEvent event) {
        ((CartController) main).removeFromCart(food);
    }

    @FXML
    void editFood(ActionEvent event) {
        ((RestaurantHomeController) main).editFood(food);
    }

    @FXML
    void deliver(ActionEvent event) {
        ((RestaurantOrdersController) main).deliverOrder(order);
        deliverButton.setVisible(false);
        deliverButton.setManaged(false);
        foodRestaurant.setText("Delivered");

    }

    public void setMain(Object main) {
        this.main = main;
    }

    private void buttonVisibility() {
        addButton.setVisible(false);
        addButton.setManaged(false);
        editButton.setVisible(false);
        editButton.setManaged(false);
        deliverButton.setVisible(false);
        deliverButton.setManaged(false);
        removeButton.setVisible(false);
        removeButton.setManaged(false);

        if (main instanceof CartController) {
            removeButton.setVisible(true);
            removeButton.setManaged(true);
        }

        else if (main instanceof CustomerHomeController) {
            addButton.setVisible(true);
            addButton.setManaged(true);
        }

        else if (main instanceof CustomerMyOrderController) {
        }

        else if (main instanceof RestaurantHomeController) {
            editButton.setVisible(true);
            editButton.setManaged(true);
        }

        else if (main instanceof RestaurantOrdersController) {
            deliverButton.setVisible(true);
            deliverButton.setManaged(true);
        }
    }

    public void set(Food food, String name, String category, String price, String restaurant) {
        this.food = food;
        foodName.setText(name);
        foodCategory.setText(category);
        foodPrice.setText("$" + price);
        foodRestaurant.setText(restaurant);

        buttonVisibility();
    }

    public void set(Order order) {
        this.order = order;
        this.food = order.getFood();
        foodName.setText(food.getName());
        foodCategory.setText(food.getCategory());
        foodPrice.setText("$" + food.getPrice());
        foodRestaurant.setText("Ordered by: " + order.getCustomerName());

        buttonVisibility();

        if (order.isAccepted()) {
            foodRestaurant.setText("Delivered");
            deliverButton.setVisible(false);
            deliverButton.setManaged(false);
        }

    }
}
