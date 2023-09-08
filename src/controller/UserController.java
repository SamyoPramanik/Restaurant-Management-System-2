package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class UserController {

    private Object main;

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

    public void setMain(Object main) {
        this.main = main;
    }

}
