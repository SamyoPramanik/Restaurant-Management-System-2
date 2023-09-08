package client;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.NetworkChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import controller.AuthController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import requests.*;
import util.*;

public class Client {
    Response response;
    NetworkUtil nu;
    Customer customer;
    Restaurant restaurant;
    Admin admin;
    Stage stage;
    AuthController authController;

    public Client() {
        try {
            nu = new NetworkUtil("127.0.0.1", 3000);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/auth.fxml"));
            Parent root = loader.load();

            authController = loader.getController();
            authController.setMain(this);

            stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            System.out.println("Error: vulval " + e.getMessage());
        }
    }

    public void registerRequest(String name, String username, String password)
            throws Exception {
        nu.write(new Register(name, username, password, "customer"));
        Response response = (Response) nu.read();

        if (response.getMessage().equals("User added")) {
            System.out.println(response.getMessage());
            Customer customer = (Customer) response.getData();
            stage.close();
            new CustomerUser(nu, customer);
        }

        else {
            System.out.println(response.getMessage());
            authController.showRegMsg(response.getMessage());
        }
    }

    public void showAdminHome(Admin admin) {

    }

    public void logOut() {

    }

    public void loginRequest(String username, String password) throws Exception {
        nu.write(new Login(username, password));
        Response response = (Response) nu.read();

        if (response.getMessage().equals("admin")) {
            admin = (Admin) response.getData();
            System.out.println(admin.getName());
            stage.close();
            showAdminHome(admin);
        }

        else if (response.getMessage().equals("customer")) {
            Customer customer = (Customer) response.getData();
            stage.close();
            new CustomerUser(nu, customer);
        }

        else if (response.getMessage().equals("restaurant")) {
            int restaurant = (int) response.getData();
            stage.close();
            new RestaurantUser(nu, restaurant);
        }

        else {
            System.out.println(response.getMessage());
            authController.showLoginMsg(response.getMessage());
        }
    }

}
