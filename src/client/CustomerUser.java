package client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import controller.CartController;
import controller.CustomerHomeController;
import controller.CustomerMyOrderController;
import controller.UserController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import requests.GetCustomerOrder;
import requests.SearchFood;
import threads.*;
import util.*;

public class CustomerUser {
    NetworkUtil nu;
    public Customer customer;
    volatile Response response;
    volatile boolean isOrderCheckingRunning;
    CustomerHomeController controller;
    CartController cartController;
    public double totalCost = 0;
    public int orderCount = 0;
    Stage stg;
    Stage cartStage;
    volatile public ArrayList<Order> cart = new ArrayList<>();

    public CustomerUser(NetworkUtil nu, Customer customer) throws ClassNotFoundException, IOException {
        this.nu = nu;
        this.customer = customer;
        isOrderCheckingRunning = true;
        stg = new Stage();
        showCustomerHome();
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }

    public void showCustomerHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/customerHome.fxml"));
            Parent root = loader.load();
            controller = loader.getController();

            controller.setMain(this);
            controller.curtButton.setText(orderCount + "");

            stg.setScene(new Scene(root));
            stg.setTitle("Home - " + customer.getName());
            stg.setResizable(false);
            stg.show();
        } catch (Exception e) {
            System.out.println("Error: in customerHome " + e);
        }
    }

    public void showCustomerMyOrder() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/customerMyOrder.fxml"));
            Parent root = loader.load();
            CustomerMyOrderController controller = loader.getController();

            nu.write(new GetCustomerOrder(customer.getId()));
            Object o = nu.read();
            Response response = (Response) o;
            System.out.println(response.getMessage());
            ArrayList<Order> orders = (ArrayList<Order>) response.getData();
            controller.setMain(this);
            controller.loadOrders(orders);

            stg.setScene(new Scene(root));
            stg.setTitle("My Order - " + customer.getName());
            stg.setResizable(false);
            stg.show();
        } catch (Exception e) {
            System.out.println("Error: in customer myorder " + e);
        }

    }

    public void showCustomerCart() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/cart.fxml"));
            Parent root = loader.load();
            cartController = loader.getController();

            cartController.setMain(this);
            cartController.loadCart(cart);
            cartController.orderCount.setText("Cart: " + orderCount + " items");
            cartController.totalCost.setText("Total cost: $" + String.format("%.2f", totalCost));

            cartStage = new Stage();
            cartStage.setScene(new Scene(root));
            cartStage.setTitle("Cart");
            cartStage.setResizable(false);
            cartStage.initModality(Modality.APPLICATION_MODAL);
            cartStage.showAndWait();

        } catch (Exception e) {
            System.out.println("Error: in customer myorder " + e);
        }
    }

    public void shwoUser() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/user.fxml"));
            Parent root = loader.load();
            UserController userController = loader.getController();

            userController.setMain(this);
            userController.set(customer.getName(), "Customer");

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(customer.getName());
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (Exception e) {
            System.out.println("Error: in customerHome " + e);
        }
    }

    public void addToCart(Food food) {
        for (Order order : cart) {
            if (order.getFood().getResId() == food.getResId() && order.getFood().getName()
                    .equals(food.getName()) && order.getFood().getCategory().equals(food.getCategory())) {
                order.increaseCount();
                totalCost += food.getPrice();
                orderCount++;
                controller.curtButton.setText(orderCount + "");
                return;
            }
        }

        cart.add(new Order(customer.getId(), food, 1, false));
        totalCost += food.getPrice();
        orderCount++;
        controller.curtButton.setText(orderCount + "");
    }

    public void removeFromCart(Order order) {
        try {
            cart.remove(order);
            totalCost -= order.getFood().getPrice() * order.getOrderCount();
            orderCount -= order.getOrderCount();
            cartController.orderCount.setText("Cart: " + orderCount + " items");
            cartController.totalCost.setText("Total cost: $" + String.format("%.2f", totalCost));
            controller.curtButton.setText(orderCount + "");
            cartController.loadCart(cart);
        } catch (Exception e) {
            System.out.println("Error: in removeFromCart " + e);
        }
    }

    synchronized public ArrayList<Food> searchFood(String foodName, String by) throws Exception {
        nu.write(new SearchFood(foodName, by));
        isOrderCheckingRunning = false;

        System.out.println("order checking");
        while (isOrderCheckingRunning)
            wait();

        System.out.println("reading food...");
        Object o = nu.read();
        System.out.println("food readed");

        if (o instanceof Response)
            response = (Response) o;

        isOrderCheckingRunning = true;

        if (response != null && response.getMessage().equals("found")) {
            ArrayList<Food> foods = (ArrayList<Food>) response.getData();

            // showOrderFood(foods);
            return foods;
        }
        return null;
    }

    synchronized public ArrayList<Food> searchFood(double minPrice, double maxPrice, String by) throws Exception {
        nu.write(new SearchFood(minPrice, maxPrice, by));
        isOrderCheckingRunning = false;

        System.out.println("order checking");
        while (isOrderCheckingRunning)
            wait();

        System.out.println("reading food...");
        Object o = nu.read();
        System.out.println("food readed");

        if (o instanceof Response)
            response = (Response) o;

        isOrderCheckingRunning = true;

        if (response != null && response.getMessage().equals("found")) {
            ArrayList<Food> foods = (ArrayList<Food>) response.getData();

            // showOrderFood(foods);
            return foods;
        }
        return null;
    }

    public void orderFood() throws Exception {
        try {
            if (cart.size() > 0) {
                isOrderCheckingRunning = false;
                totalCost = 0;
                controller.curtButton.setText("0");
                cartStage.close();

                for (Order order : cart) {
                    nu.write(order);
                    Object o = nu.read();
                }
                cart.clear();
                orderCount = 0;
                totalCost = 0;
            }
        } catch (Exception e) {
            System.out.println("Error: in orderFood " + e);
        }
    }
}
