package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.CustomerUser;
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
    public ArrayList<Food> cart = new ArrayList<>();
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
    private Button curtButton;

    @FXML
    void searchFood(Event event) {
        try {
            // new Thread(() -> searchFoodThread()).start();
            searchFoodThread();

        } catch (Exception e) {
            System.out.println("Error in searchFood UI: " + e);
        }
    }

    @FXML
    void showuser(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user.fxml"));
        Parent root = loader.load();
        UserController userController = loader.getController();
        userController.setMain(this);
        userController.set(main.customer.getName(), "Customer");
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("User");
        stage.setResizable(false);
        stage.show();
    }

    synchronized void searchFoodThread() {
        try {
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
            foodList1.getChildren().clear();
            for (Food food : foods) {
                foodList.getItems().add(food.getName());

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/food.fxml"));
                Pane pane = loader.load();
                FoodController foodController = loader.getController();
                foodController.setMain(this);
                foodController.set(food, food.getName(), food.getCategory(), food.getPrice() + "",
                        food.getResName());
                foodList1.getChildren().add(pane);
                System.out.println("list updated");
            }

        } catch (Exception e) {
            System.out.println("Error in searchFood UI: " + e);
        }
    }

    public void addToCart(Food food) {
        cart.add(0, food);
        curtButton.setText(cart.size() + "");

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