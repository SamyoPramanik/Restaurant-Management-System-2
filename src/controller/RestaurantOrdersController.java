package controller;

import java.util.ArrayList;

import client.RestaurantUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import util.Food;
import util.Order;

public class RestaurantOrdersController {

    private RestaurantUser main;

    @FXML
    private VBox orderList1;

    @FXML
    public Label newOrderCount;

    @FXML
    void showHome(ActionEvent event) {
        main.showRestaurantHome();
    }

    @FXML
    void showuser(ActionEvent event) {
        main.showRestaurantUser();

    }

    public void deliverOrder(Order order) {
        main.deliverOrder(order);
    }

    public void loadOrders(ArrayList<Order> orders) {
        try {
            orderList1.getChildren().clear();
            for (Order order : orders) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/food.fxml"));
                Pane pane = loader.load();
                FoodController foodController = loader.getController();
                foodController.setMain(this);

                foodController.set(order);
                orderList1.getChildren().add(pane);
            }
        } catch (Exception e) {
            System.out.println("Error in load orders: " + e);
        }
    }

    public void setMain(RestaurantUser main) {
        this.main = main;
    }
}
