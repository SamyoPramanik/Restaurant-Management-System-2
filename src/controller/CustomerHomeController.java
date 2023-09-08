package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.CustomerUser;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import util.Food;

public class CustomerHomeController implements Initializable {

    private CustomerUser main;
    public ArrayList<Food> cart;

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
    private ChoiceBox<String> searchFoodBy;

    @FXML
    void searchFood(Event event) {
        try {
            new Thread(() -> searchFoodThread()).start();

        } catch (Exception e) {
            System.out.println("Error in searchFood UI: " + e);
        }
    }

    void searchFoodThread() {
        try {
            ArrayList<Food> foods = null;
            int idx = searchFoodBy.getSelectionModel().getSelectedIndex();
            if (idx == 0)
                foods = main.searchFood(foodName.getText(), "name");
            else if (idx == 1)
                foods = main.searchFood(foodName.getText(), "category");
            else if (idx == 2) {
                try {
                    double min = Double.parseDouble(minPrice.getText());
                    double max = Double.parseDouble(maxPrice.getText());

                    foods = main.searchFood(min, max, "price");
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            foodList.getItems().clear();
            for (Food food : foods)
                foodList.getItems().add(food.getName());

        } catch (Exception e) {
            System.out.println("Error in searchFood UI: " + e);
        }
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