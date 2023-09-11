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
    private Label orderCount;

    @FXML
    private Label isDelivered;

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
        ((CartController) main).removeFromCart(order);
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
        isDelivered.setText("Delivered");
        isDelivered.setVisible(true);
        isDelivered.setManaged(true);

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
        isDelivered.setVisible(false);
        isDelivered.setManaged(false);
        orderCount.setVisible(false);
        orderCount.setManaged(false);

        if (main instanceof CartController) {
            orderCount.setVisible(true);
            orderCount.setManaged(true);
            removeButton.setVisible(true);
            removeButton.setManaged(true);
        }

        else if (main instanceof CustomerHomeController) {
            addButton.setVisible(true);
            addButton.setManaged(true);
        }

        else if (main instanceof CustomerMyOrderController) {
            orderCount.setVisible(true);
            orderCount.setManaged(true);
            isDelivered.setVisible(true);
            isDelivered.setManaged(true);

            if (order.isAccepted()) {
                isDelivered.setText("Delivered");
            }

            else {
                isDelivered.setText("Pending");
            }
        }

        else if (main instanceof RestaurantHomeController) {
            editButton.setVisible(true);
            editButton.setManaged(true);
        }

        else if (main instanceof RestaurantOrdersController) {
            if (order.isAccepted()) {
                isDelivered.setText("Delivered");
                deliverButton.setVisible(false);
                deliverButton.setManaged(false);
                isDelivered.setVisible(true);
                isDelivered.setManaged(true);
            }

            else {
                deliverButton.setVisible(true);
                deliverButton.setManaged(true);
            }

            orderCount.setVisible(true);
            orderCount.setManaged(true);
            foodRestaurant.setText("Order by: " + order.getCustomerName());
        }
    }

    public void set(Food food) {
        this.food = food;
        foodName.setText(food.getName());
        foodCategory.setText(food.getCategory());
        foodPrice.setText("$" + food.getPrice());
        foodRestaurant.setText(food.getResName());

        buttonVisibility();
    }

    public void set(Order order) {
        this.order = order;
        this.food = order.getFood();
        foodName.setText(food.getName());
        foodCategory.setText(food.getCategory());
        orderCount.setText(order.getOrderCount() + "");
        foodPrice.setText("$" + String.format("%.2f", food.getPrice() * order.getOrderCount()));
        foodRestaurant.setText(food.getResName());

        buttonVisibility();
    }
}
