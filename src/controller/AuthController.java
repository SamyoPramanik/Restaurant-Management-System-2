package controller;

import client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AuthController {

    private Client main;

    @FXML
    private Label loginMsg;

    @FXML
    private Label registerMsg;

    @FXML
    private PasswordField passwordLogin;

    @FXML
    private TextField registerName;

    @FXML
    private PasswordField registerPassword;

    @FXML
    private TextField registerUsername;

    @FXML
    private TextField usernameLogin;

    @FXML
    void login(ActionEvent event) {
        try {
            String username = usernameLogin.getText();
            String password = passwordLogin.getText();
            main.loginRequest(username, password);

        } catch (Exception e) {
            System.out.println("Error: vulval " + e.getMessage());
        }
    }

    @FXML
    void register(ActionEvent event) {
        try {
            if (registerName.getText().equals("") || registerUsername.getText().equals("")
                    || registerPassword.getText().equals("")) {
                showRegMsg("Please fill up all the fields");
                return;
            }
            String name = registerName.getText();
            String username = registerUsername.getText();
            String password = registerPassword.getText();
            main.registerRequest(name, username, password);

        } catch (Exception e) {
            System.out.println("Error: vulval " + e.getMessage());
        }
    }

    @FXML
    void registerReset(ActionEvent event) {
        registerName.setText("");
        registerUsername.setText("");
        registerPassword.setText("");
    }

    @FXML
    void resetLogin(ActionEvent event) {
        usernameLogin.setText("");
        passwordLogin.setText("");
    }

    public void setMain(Client main) {
        this.main = main;
    }

    public void showLoginMsg(String msg) {
        loginMsg.setText(msg);
        loginMsg.setVisible(true);
    }

    public void showRegMsg(String msg) {
        registerMsg.setText(msg);
        registerMsg.setVisible(true);
    }

}
