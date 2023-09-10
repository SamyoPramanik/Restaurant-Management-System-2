package controller;

import client.AdminUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import util.RestaurantInfo;

public class RestaurantAddEditController {

    private AdminUser main;
    private RestaurantInfo res;

    @FXML
    private Button addFoodButton;

    @FXML
    public Label foodMsg;

    @FXML
    private TextField resCategory1;

    @FXML
    private TextField resCategory2;

    @FXML
    private TextField resCategory3;

    @FXML
    private TextField resName;

    @FXML
    private TextField resZipCode;

    @FXML
    private TextField resPassword;

    @FXML
    private TextField resUsername;

    @FXML
    private Label passwordLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private Button updateFoodButton;

    @FXML
    void addRestaurant(ActionEvent event) {
        if (resName.getText().isEmpty() || resCategory1.getText().isEmpty() || resZipCode.getText().isEmpty()
                || resUsername.getText().isEmpty() || resPassword.getText().isEmpty()) {
            foodMsg.setText("Please fill all the fields");
            return;
        }

        main.addRestaurant(new RestaurantInfo(0, resName.getText(), 0, resZipCode.getText(), resCategory1.getText(),
                resCategory2.getText(),
                resCategory3.getText(), resUsername.getText(), resPassword.getText()));
    }

    @FXML
    void updateRestaurant(ActionEvent event) {
        if (resName.getText().isEmpty() || resCategory1.getText().isEmpty() || resZipCode.getText().isEmpty()) {
            foodMsg.setText("Please fill all the fields");
            return;
        }

        main.updateRestaurant(res,
                new RestaurantInfo(0, resName.getText(), 0, resZipCode.getText(), resCategory1.getText(),
                        resCategory2.getText(),
                        resCategory3.getText(), resUsername.getText(), resPassword.getText()));
    }

    public void setMain(AdminUser main) {
        this.main = main;
    }

    public void set(RestaurantInfo res) {

        if (res != null) {
            this.res = res;
            resName.setText(res.resName);
            resCategory1.setText(res.category1);
            resCategory2.setText(res.category2);
            resCategory3.setText(res.category3);
            resZipCode.setText(res.zipcode);

            addFoodButton.setVisible(false);
            addFoodButton.setManaged(false);
            resUsername.setVisible(false);
            resUsername.setManaged(false);
            resPassword.setVisible(false);
            resPassword.setManaged(false);
            usernameLabel.setVisible(false);
            usernameLabel.setManaged(false);
            passwordLabel.setVisible(false);
            passwordLabel.setManaged(false);
        }

        else {
            updateFoodButton.setVisible(false);
            updateFoodButton.setManaged(false);
        }
    }

}
