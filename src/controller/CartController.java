package controller;

import java.io.IOException;
import java.util.ArrayList;

import client.CustomerUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import util.Food;

public class CartController {

    private CustomerUser main;

    @FXML
    public Label orderCount;

    @FXML
    public Label totalCost;

    @FXML
    private VBox orderList;

    @FXML
    void orderFood(ActionEvent event) {
        try {
            main.orderFood();
        } catch (Exception e) {
            System.out.println("Error in order food:" + e);
        }
    }

    public void setMain(CustomerUser main) {
        this.main = main;
    }

    public void loadCart(ArrayList<Food> cart) throws IOException {
        orderList.getChildren().clear();
        for (Food food : cart) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/food.fxml"));
            Pane pane = loader.load();
            FoodController foodController = loader.getController();

            foodController.setMain(this);
            foodController.set(food, food.getName(), food.getCategory(), food.getPrice() + "",
                    food.getResName());
            foodController.addButton.setVisible(false);
            foodController.addButton.setManaged(false);

            orderList.getChildren().add(pane);
        }
    }

    public void removeFromCart(Food food) {
        main.removeFromCart(food);
    }

}
