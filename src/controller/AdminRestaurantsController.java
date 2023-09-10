package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.AdminUser;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import util.Food;
import util.RestaurantInfo;

public class AdminRestaurantsController implements Initializable {

    private AdminUser main;
    ArrayList<RestaurantInfo> restaurants;

    @FXML
    private HBox byName;

    @FXML
    private VBox foodInfo;

    @FXML
    private ListView<String> resList;

    @FXML
    private VBox resList1;

    @FXML
    private TextField resStr;

    @FXML
    private ChoiceBox<String> searchResBy;

    @FXML
    void searchRestaurant(Event event) {
        try {
            System.out.println("Restaurant search length: " + resStr.getText().length());
            new Thread(() -> {
                searchRestaurantThread();
            }).start();
            // searchRestaurantThread();

        } catch (Exception e) {
            System.out.println("Error in search Restaurant UI: " + e);
        }
    }

    @FXML
    void showAddNewRestaurant(ActionEvent event) {
        main.showAddNewRestaurant();
    }

    @FXML
    void showHome(ActionEvent event) {
        main.showAdminHome();
    }

    @FXML
    void showuser(ActionEvent event) {

    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        searchResBy.getItems().add(" By Name");
        searchResBy.getItems().add(" By Category");
        searchResBy.getItems().add(" By Zip Code");
        searchResBy.getSelectionModel().select(0);
        resStr.setPromptText("Restaurant Name");

        searchResBy.getSelectionModel().selectedItemProperty().addListener((nullable, oldValue, newValue) -> {

            if (newValue.equals(" By Name")) {
                byName.setVisible(true);
                byName.setManaged(true);
                resStr.setPromptText("Restaurant Name");
            }

            else if (newValue.equals(" By Category")) {
                byName.setVisible(true);
                byName.setManaged(true);
                resStr.setPromptText("Restaurant Category");

            }

            else if (newValue.equals(" By Zip Code")) {
                byName.setVisible(true);
                byName.setManaged(true);
                resStr.setPromptText("Zip Code");

            }
        });

    }

    public void setMain(AdminUser main) {
        this.main = main;
    }

    void searchRestaurantThread() {
        try {
            System.out.println("food search length: " + resStr.getText().length());
            int idx = searchResBy.getSelectionModel().getSelectedIndex();
            if (idx == 0) {
                restaurants = main.searchRestaurant(resStr.getText(), "name");
            }

            else if (idx == 1) {
                restaurants = main.searchRestaurant(resStr.getText(), "category");
            }

            else if (idx == 2) {
                restaurants = main.searchRestaurant(resStr.getText(), "zipcode");
            }
            Platform.runLater(() -> {
                try {
                    resList.getItems().clear();
                    resList1.getChildren().clear();
                    int i = 0;
                    for (RestaurantInfo r : restaurants) {
                        resList.getItems().add(r.resName);
                        System.out.println(++i + ". " + r.resName);

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/restaurant.fxml"));
                        Pane pane = loader.load();
                        RestaurantController resController = loader.getController();
                        resController.setMain(this);

                        resController.set(r);
                        resList1.getChildren().add(pane);
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

    public void editRestaurant(RestaurantInfo r) {
        main.showEditRestaurant(r);
    }

}
