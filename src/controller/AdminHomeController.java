package controller;

import client.AdminUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import requests.GetAllCount;

public class AdminHomeController {

    private AdminUser main;
    @FXML
    public Label noCustomers;

    @FXML
    public Label noFoods;

    @FXML
    public Label noRestaurants;

    @FXML
    void showRestaurants(ActionEvent event) {
        main.showAdminRestaurants();
    }

    @FXML
    void showuser(ActionEvent event) {

    }

    public void setCount(GetAllCount count) {
        noCustomers.setText(count.cusCount + "");
        noFoods.setText(count.foodCount + "");
        noRestaurants.setText(count.resCount + "");
    }

    public void setMain(AdminUser main) {
        this.main = main;
    }

}
