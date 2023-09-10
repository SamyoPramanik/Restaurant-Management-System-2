package client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import controller.AdminHomeController;
import controller.AdminRestaurantsController;
import controller.FoodAddEditController;
import controller.RestaurantAddEditController;
import controller.RestaurantHomeController;
import controller.RestaurantOrdersController;
import controller.RestaurantUserController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import requests.AddFood;
import requests.AddRestaurant;
import requests.CheckNewOrder;
import requests.DeliverOrder;
import requests.GetAllCount;
import requests.GetRestaurantInfo;
import requests.RestaurantGetOrder;
import requests.SearchFood;
import requests.SearchFoodGivenRestaurant;
import requests.SearchRestaurant;
import requests.UpdateFood;
import requests.UpdateRestaurant;
import threads.RestaurantCheckOrder;
import util.Admin;
import util.Food;
import util.NetworkUtil;
import util.Order;
import util.Response;
import util.RestaurantInfo;

public class AdminUser {
    public NetworkUtil nu;
    Admin admin;
    AdminHomeController controller;
    AdminRestaurantsController adminRestaurantsController;
    RestaurantAddEditController restaurantAddEditController;
    Stage newRestaurantStg;
    Stage stg;

    public AdminUser(NetworkUtil nu, Admin admin) {
        this.nu = nu;
        this.admin = admin;
        stg = new Stage();
        newRestaurantStg = new Stage();

        showAdminHome();
    }

    public void showAdminHome() {
        try {
            System.out.println("hi i am admin");
            controller = null;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/adminHome.fxml"));
            Parent root = loader.load();
            controller = loader.getController();
            controller.setMain(this);

            nu.write(new GetAllCount());
            Object o = nu.read();
            Response response = (Response) o;

            System.out.println(response.getMessage());

            if (response.getMessage().equals("count")) {
                GetAllCount counts = (GetAllCount) response.getData();
                controller.setCount(counts);

                System.out.println(counts.cusCount + " " + counts.resCount + " " + counts.foodCount);
            }

            stg.setScene(new Scene(root));
            stg.setTitle("Home - Admin");
            stg.setResizable(false);
            stg.show();

        } catch (

        Exception e) {
            System.out.println("Error: in adminUser showMain " + e);
        }
    }

    public void showAdminRestaurants() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/adminRestaurants.fxml"));
            Parent root = loader.load();
            adminRestaurantsController = loader.getController();
            adminRestaurantsController.setMain(this);

            stg.setScene(new Scene(root));
            stg.setTitle("Restaurants - Admin");
            stg.setResizable(false);
            stg.show();

        } catch (

        Exception e) {
            System.out.println("Error: in adminUser showRestaurants " + e);
        }
    }

    public void showAddNewRestaurant() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/restaurantAddEdit.fxml"));
            Parent root = loader.load();
            restaurantAddEditController = loader.getController();
            restaurantAddEditController.setMain(this);
            restaurantAddEditController.set(null);

            newRestaurantStg = new Stage();
            newRestaurantStg.setScene(new Scene(root));
            newRestaurantStg.setTitle("Add New Restaurant");
            newRestaurantStg.setResizable(false);
            newRestaurantStg.initModality(Modality.APPLICATION_MODAL);
            newRestaurantStg.show();
        } catch (Exception e) {
            System.out.println("Error: in showAddNewFood " + e);
        }
    }

    public ArrayList<RestaurantInfo> searchRestaurant(String str, String by) {
        try {
            nu.write(new SearchRestaurant(str, by));
            Object o = nu.read();
            Response response = (Response) o;

            if (response.getMessage().equals("found")) {
                ArrayList<RestaurantInfo> restaurants = (ArrayList<RestaurantInfo>) response.getData();
                return restaurants;
            }

        } catch (Exception e) {
            System.out.println("Error: in adminUser searchRestaurant " + e);
        }
        return null;
    }

    public void addRestaurant(RestaurantInfo res) {
        try {
            new Thread(() -> {
                addRestaurantThread(res);
            }).start();

        } catch (Exception e) {
            System.out.println("Error: in adminUser addRestaurant " + e);
        }
    }

    public void addRestaurantThread(RestaurantInfo res) {
        try {
            nu.write(new AddRestaurant(res));
            Object o = nu.read();
            Response response = (Response) o;

            Platform.runLater(() -> {
                if (response.getMessage().equals("added")) {
                    System.out.println("Restaurant added");
                    newRestaurantStg.close();
                }

                else {
                    restaurantAddEditController.foodMsg.setText(response.getMessage());
                }
            });

        } catch (Exception e) {
            System.out.println("Error: in adminUser addRestaurant " + e);
        }
    }

    public void showEditRestaurant(RestaurantInfo res) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/restaurantAddEdit.fxml"));
            Parent root = loader.load();
            restaurantAddEditController = loader.getController();
            restaurantAddEditController.setMain(this);
            restaurantAddEditController.set(res);

            newRestaurantStg = new Stage();
            newRestaurantStg.setScene(new Scene(root));
            newRestaurantStg.setTitle("Edit Restaurant - " + res.resName);
            newRestaurantStg.setResizable(false);
            newRestaurantStg.initModality(Modality.APPLICATION_MODAL);
            newRestaurantStg.show();
        } catch (Exception e) {
            System.out.println("Error: in showEditRestaurant " + e);
        }
    }

    public void updateRestaurant(RestaurantInfo oldRes, RestaurantInfo newRes) {
        try {
            new Thread(() -> {
                updateRestaurantThread(oldRes, newRes);
            }).start();

        } catch (Exception e) {
            System.out.println("Error: in adminUser updateRestaurant " + e);
        }

    }

    public void updateRestaurantThread(RestaurantInfo oldRes, RestaurantInfo newRes) {
        try {
            System.out.println("update restaurant thread");
            nu.write(new UpdateRestaurant(oldRes, newRes));
            Object o = nu.read();
            Response response = (Response) o;

            Platform.runLater(() -> {
                if (response.getMessage().equals("Restaurant updated")) {
                    System.out.println("Restaurant updated");
                    newRestaurantStg.close();
                }

                else {
                    restaurantAddEditController.foodMsg.setText(response.getMessage());
                }
            });

        } catch (Exception e) {
            System.out.println("Error: in adminUser updateRestaurant " + e);
        }

    }

}