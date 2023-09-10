package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.CustomerUser;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.Food;

public class CustomerHomeController implements Initializable {

    private CustomerUser main;
    public ArrayList<Food> foods = null;

    @FXML
    private HBox byName;

    @FXML
    private HBox byPrice;

    @FXML
    private ListView<String> foodList;

    @FXML
    private TextField foodName;

    @FXML
    private TextField maxPrice;

    @FXML
    private TextField minPrice;

    @FXML
    private VBox foodInfo;

    @FXML
    private VBox foodList1;

    @FXML
    private ChoiceBox<String> searchFoodBy;

    @FXML
    public Button curtButton;

    @FXML
    void searchFood(Event event) {
        try {
            System.out.println("food search length: " + foodName.getText().length());
            new Thread(() -> {
                searchFoodThread();
            }).start();
            // searchFoodThread();

        } catch (Exception e) {
            System.out.println("Error in searchFood UI: " + e);
        }
    }

    @FXML
    void showuser(Event event) throws IOException {
        main.shwoUser();
    }

    @FXML
    void showMyOrder(ActionEvent event) {
        main.showCustomerMyOrder();
    }

    @FXML
    void showCart(ActionEvent event) {
        main.showCustomerCart();
    }

    void searchFoodThread() {
        try {
            int idx = searchFoodBy.getSelectionModel().getSelectedIndex();
            if (idx == 0) {
                if (foodName.getText().length() > 2)
                    foods = main.searchFood(foodName.getText(), "name");

                else
                    return;
            }

            else if (idx == 1) {
                if (foodName.getText().length() > 2)
                    foods = main.searchFood(foodName.getText(), "category");

                else
                    return;
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
                    foodList.getItems().clear();
                    foodList1.getChildren().clear();
                    int i = 0;
                    for (Food food : foods) {
                        foodList.getItems().add(food.getName());
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
                    System.out.println(e);
                }
            });
            System.out.println("list updated");

        } catch (Exception e) {
            System.out.println("Error in searchFood UI: " + e);
        }
    }

    public void addToCart(Food food) {
        main.cart.add(0, food);
        main.totalCost += food.getPrice();
        curtButton.setText(main.cart.size() + "");

    }

    public void setMain(CustomerUser main) {
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