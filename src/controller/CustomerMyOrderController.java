package controller;

import java.util.ArrayList;

import client.CustomerUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import util.Food;
import util.Order;

public class CustomerMyOrderController {

    private CustomerUser main;

    @FXML
    private VBox orderlist1;

    @FXML
    void showHome(ActionEvent event) {
        main.showCustomerHome();
    }

    @FXML
    void showuser(ActionEvent event) {
        main.shwoUser();
    }

    public void setMain(CustomerUser main) {
        this.main = main;
    }

    public void loadOrders(ArrayList<Order> orders) {
        try {
            orderlist1.getChildren().clear();
            for (Order order : orders) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/food.fxml"));
                Pane pane = loader.load();
                FoodController foodController = loader.getController();
                foodController.setMain(this);

                foodController.set(order);
                orderlist1.getChildren().add(0, pane);
            }
        } catch (Exception e) {
            System.out.println("Error in loadOrders: " + e);
        }
    }

}
