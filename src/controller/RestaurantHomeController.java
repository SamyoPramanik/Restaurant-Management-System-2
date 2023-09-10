package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.RestaurantUser;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.Food;

public class RestaurantHomeController implements Initializable {

    private ArrayList<Food> foods = null;
    private RestaurantUser main;
    @FXML
    private HBox byName;

    @FXML
    private HBox byPrice;

    @FXML
    private VBox foodInfo;

    @FXML
    private VBox foodList1;

    @FXML
    private TextField foodName;

    @FXML
    private TextField maxPrice;

    @FXML
    private TextField minPrice;

    @FXML
    public Label newOrderCount;

    @FXML
    private ChoiceBox<String> searchFoodBy;

    @FXML
    void searchFood(Event event) {
        try {
            System.out.println("food search length: " + foodName.getText().length());
            System.out.println("is new order checking: " + main.isNewOrderChecking);
            main.isNewOrderChecking = false;

            new Thread(() -> {
                searchFoodThread();
            }).start();
            // searchFoodThread();

        } catch (Exception e) {
            System.out.println("Error in searchFood UI: " + e);
        }
    }

    @FXML
    void showMyOrder(ActionEvent event) {
        main.showRestaurantOrders();
    }

    @FXML
    void showuser(ActionEvent event) {
        main.showRestaurantUser();
    }

    @FXML
    void showAddNewFood(ActionEvent event) {
        main.showAddNewFood();
    }

    public void editFood(Food food) {
        main.showEditFood(food);
    }

    synchronized void searchFoodThread() {
        try {
            while (main.isNewOrderChecking) {
                wait();
            }
            System.out.println("food search length: " + foodName.getText().length());
            int idx = searchFoodBy.getSelectionModel().getSelectedIndex();
            if (idx == 0) {
                foods = main.searchFood(foodName.getText(), "name");
            }

            else if (idx == 1) {
                foods = main.searchFood(foodName.getText(), "category");
            }

            else if (idx == 2) {
                try {
                    double min = Double.parseDouble(minPrice.getText());
                    double max = Double.parseDouble(maxPrice.getText());

                    foods = main.searchFood(min, max, "price");
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            Platform.runLater(() -> {
                try {
                    foodList1.getChildren().clear();
                    int i = 0;
                    for (Food food : foods) {
                        System.out.println(++i + ". " + food.getName());

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/food.fxml"));
                        Pane pane = loader.load();
                        FoodController foodController = loader.getController();
                        foodController.setMain(this);

                        foodController.set(food, food.getName(), food.getCategory(), food.getPrice()
                                + "",
                                food.getResName());
                        foodList1.getChildren().add(pane);
                    }
                } catch (Exception e) {
                    System.out.println("Error in updating restarurant: " + e);
                }
            });
            System.out.println("list updated");

            main.isNewOrderChecking = true;
            notifyAll();

        } catch (Exception e) {
            System.out.println("Error in searchFood UI: " + e);
        }
    }

    public void setMain(RestaurantUser main) {
        this.main = main;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        searchFoodBy.getItems().add(" By Name");
        searchFoodBy.getItems().add(" By Category");
        searchFoodBy.getItems().add(" By Price");
        searchFoodBy.getSelectionModel().select(0);
        byPrice.setVisible(false);
        byPrice.setManaged(false);
        foodName.setPromptText("Food Name");

        searchFoodBy.getSelectionModel().selectedItemProperty().addListener((nullable, oldValue, newValue) -> {

            if (newValue.equals(" By Name")) {
                byName.setVisible(true);
                byPrice.setVisible(false);
                byPrice.setManaged(false);
                byName.setManaged(true);
                foodName.setPromptText("Food Name");
            } else if (newValue.equals(" By Category")) {
                byName.setVisible(true);
                byPrice.setVisible(false);
                byPrice.setManaged(false);
                byName.setManaged(true);
                foodName.setPromptText("Food Category");

            } else if (newValue.equals(" By Price")) {
                byName.setVisible(false);
                byPrice.setVisible(true);
                byPrice.setManaged(true);
                byName.setManaged(false);

            }
        });

    }

}
