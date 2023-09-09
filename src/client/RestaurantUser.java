package client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import controller.FoodAddEditController;
import controller.RestaurantHomeController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import requests.AddFood;
import requests.CheckNewOrder;
import requests.RestaurantGetOrder;
import requests.SearchFood;
import threads.RestaurantCheckOrder;
import threads.SendRequest;
import util.Food;
import util.NetworkUtil;
import util.Order;
import util.Response;

public class RestaurantUser {
    public NetworkUtil nu;
    int resId;
    volatile public boolean isNewOrderChecking;
    volatile Response response;
    volatile Response newOrder;
    Stage stg;
    Stage newFoodStg;
    RestaurantHomeController controller;
    FoodAddEditController foodAddEditController;
    int newOrderCount = 0;

    public RestaurantUser(NetworkUtil nu, int resId) {
        this.nu = nu;
        this.resId = resId;
        stg = new Stage();
        isNewOrderChecking = true;

        Thread newOrderThread = new Thread(() -> {
            checkNewOrder();
        });
        newOrderThread.start();

        showRestaurantHome();
    }

    public int getId() {
        return resId;
    }

    public void showRestaurantHome() {
        try {
            // while (true) {
            // System.out.println("1. Search food");
            // System.out.println("2. Show order");
            // System.out.println("3. Exit");

            // int choice = Integer.parseInt(sc.nextLine());

            // if (choice == 2) {
            // nu.write(new RestaurantGetOrder(resId));
            // // isOrderChecking = false;
            // // SendRequest request = new SendRequest(this);
            // // request.join();
            // Object o = nu.read();
            // if (o instanceof Response) {
            // response = (Response) o;
            // }

            // if (response != null && response.getMessage().equals("orders")) {
            // Object data = response.getData();
            // ArrayList<Order> orders = (ArrayList<Order>) data;
            // response = null;

            // for (int i = 1; i <= orders.size(); i++) {
            // System.out.println(i + ". " + orders.get(i - 1).getFood().getName());
            // }
            // }
            // }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/restaurantHome.fxml"));
            Parent root = loader.load();
            controller = loader.getController();
            controller.setMain(this);

            stg.setScene(new Scene(root));
            stg.setResizable(false);
            stg.show();

        } catch (

        Exception e) {
            System.out.println("Error: in RestaurantUser showMain " + e);
        }
    }

    public void showAddNewFood() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/foodAddEdit.fxml"));
            Parent root = loader.load();
            foodAddEditController = loader.getController();
            foodAddEditController.setMain(this);
            foodAddEditController.set(null, getId());

            newFoodStg = new Stage();
            newFoodStg.setScene(new Scene(root));
            newFoodStg.setResizable(false);
            newFoodStg.initModality(Modality.APPLICATION_MODAL);
            newFoodStg.show();
        } catch (Exception e) {
            System.out.println("Error: in showAddNewFood " + e);
        }
    }

    public void addNewFood(Food food) {
        isNewOrderChecking = false;
        new Thread(() -> {
            addNewFoodThread(food);
        }).start();
    }

    synchronized void addNewFoodThread(Food food) {
        try {
            nu.write(new AddFood(food));
            Thread.sleep(10000);
            Object o = new Response("failed", null);// nu.read();
            if (o instanceof Response) {
                response = (Response) o;
            }
            if (response != null && response.getMessage().equals("added")) {
                System.out.println("food added");
                newFoodStg.close();
            }

            else {
                Platform.runLater(() -> {
                    foodAddEditController.foodMsg.setText(response.getMessage());
                });
            }

            isNewOrderChecking = true;
            notifyAll();

        } catch (Exception e) {
            System.out.println("Error: in addNewFoodThread " + e);
        }
    }

    synchronized public ArrayList<Food> searchFood(String foodName, String by) throws Exception {
        nu.write(new SearchFood(foodName, by));

        System.out.println("order checking");
        System.out.println("reading food...");
        Object o = nu.read();
        System.out.println("food readed");

        if (o instanceof Response)
            response = (Response) o;

        isNewOrderChecking = true;
        notifyAll();

        if (response != null && response.getMessage().equals("found")) {
            ArrayList<Food> foods = (ArrayList<Food>) response.getData();

            // showOrderFood(foods);
            return foods;
        }
        return null;
    }

    synchronized public ArrayList<Food> searchFood(double minPrice, double maxPrice, String by) throws Exception {
        nu.write(new SearchFood(minPrice, maxPrice, by));

        System.out.println("order checking");
        System.out.println("reading food...");
        Object o = nu.read();
        System.out.println("food readed");

        if (o instanceof Response)
            response = (Response) o;

        isNewOrderChecking = true;
        notifyAll();

        if (response != null && response.getMessage().equals("found")) {
            ArrayList<Food> foods = (ArrayList<Food>) response.getData();

            // showOrderFood(foods);
            return foods;
        }
        return null;
    }

    synchronized public void checkNewOrder() {
        try {
            while (true) {
                if (!isNewOrderChecking)
                    wait();

                System.out.println("searching for new order");

                nu.write(new CheckNewOrder(resId));
                Object o = nu.read();

                if (o instanceof Response) {
                    newOrder = (Response) o;
                }
                if (newOrder.getMessage().equals("no new order") == false) {
                    System.out.println(newOrder.getMessage());

                    Platform.runLater(() -> {
                        controller.newOrderCount.setText(newOrder.getMessage());
                    });
                }
                response = null;
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("Error: in RestaurantUser showNewOrder " + e);
        }
    }

}