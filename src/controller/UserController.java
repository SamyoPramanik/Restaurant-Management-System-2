package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class UserController {

    private CustomerHomeController main;

    @FXML
    private Label userName;

    @FXML
    private Label userType;

    @FXML
    void signOut(ActionEvent event) {

    }

    public void set(String name, String type) {
        userName.setText(name);
        userType.setText(type);
    }

    public void setMain(CustomerHomeController main) {
        this.main = main;
    }

}
