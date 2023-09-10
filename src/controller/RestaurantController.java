package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import util.Restaurant;
import util.RestaurantInfo;

public class RestaurantController {

    AdminRestaurantsController main;
    RestaurantInfo res;

    @FXML
    private Button editButton;

    @FXML
    private Label resCategory;

    @FXML
    private Label resName;

    @FXML
    private Label resZipCode;

    @FXML
    void editRestaurant(ActionEvent event) {
        main.editRestaurant(res);
    }

    public void setMain(AdminRestaurantsController main) {
        this.main = main;
    }

    public void set(RestaurantInfo res) {
        this.res = res;
        resName.setText(res.resName);
        resCategory.setText(res.category1 + " | " + res.category2 + " | " + res.category3);
        resZipCode.setText(res.zipcode);
    }

}
