package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import util.RestaurantInfo;

public class RestaurantUserController {

    @FXML
    private Label resCategory;

    @FXML
    private Label resName;

    @FXML
    private Label zipCode;

    @FXML
    void signOut(ActionEvent event) {

    }

    public void set(RestaurantInfo restaurantInfo) {
        resName.setText(restaurantInfo.resName);
        resCategory.setText(
                restaurantInfo.category1 + " | " + restaurantInfo.category2 + " | " + restaurantInfo.category3);
        zipCode.setText("Zip code: " + restaurantInfo.zipcode);
    }

}
