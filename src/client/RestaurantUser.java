package client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import controller.FoodAddEditController;
import controller.RestaurantHomeController;
import controller.RestaurantOrdersController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import requests.AddFood;
import requests.CheckNewOrder;
import requests.DeliverOrder;
import requests.RestaurantGetOrder;
import requests.SearchFood;
import requests.SearchFoodGivenRestaurant;
import requests.UpdateFood;
import threads.RestaurantCheckOrder;
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
    RestaurantOrdersController ordersController;
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
            controller = null;
            foodAddEditController = null;
            ordersController = null;

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

    public void showRestaurantOrders() {
        try {
            controller = null;
            foodAddEditController = null;
            ordersController = null;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/restaurantOrders.fxml"));
            Parent root = loader.load();
            ordersController = loader.getController();

            System.out.println("showing orders");
            ordersController.setMain(this);
            stg.setScene(new Scene(root));
            stg.setResizable(false);
            stg.show();

            isNewOrderChecking = false;
            new Thread(() -> getRestaurantOrders()).start();

        } catch (Exception e) {
            System.out.println("Error: in RestaurantUser showOrders " + e);
        }
    }

    synchronized public void getRestaurantOrders() {
        try {
            nu.write(new RestaurantGetOrder(resId));
            Object o = nu.read();
            if (o instanceof Response) {
                response = (Response) o;
            }

            if (response != null && response.getMessage().equals("orders")) {
                Object data = response.getData();
                ArrayList<Order> orders = (ArrayList<Order>) data;

                Platform.runLater(() -> {
                    try {
                        ordersController.loadOrders(orders);
                    } catch (Exception e) {
                        System.out.println("Error: in RestaurantUser getRestaurantOrders " + e);
                    }
                });
            }
            isNewOrderChecking = true;
            notifyAll();
        } catch (Exception e) {
            System.out.println("Error: in RestaurantUser getRestaurantOrders " + e);
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

    public void showEditFood(Food food) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/foodAddEdit.fxml"));
            Parent root = loader.load();
            foodAddEditController = loader.getController();
            foodAddEditController.setMain(this);
            foodAddEditController.set(food, getId());

            newFoodStg = new Stage();
            newFoodStg.setScene(new Scene(root));
            newFoodStg.setResizable(false);
            newFoodStg.initModality(Modality.APPLICATION_MODAL);
            newFoodStg.show();
        } catch (Exception e) {
            System.out.println("Error: in showEditFood " + e);
        }
    }

    public void updateFood(Food food, Food updateFood) {
        isNewOrderChecking = false;
        new Thread(() -> {
            updateFoodThread(food, updateFood);
        }).start();
    }

    synchronized void updateFoodThread(Food food, Food updateFood) {
        try {
            nu.write(new UpdateFood(food, updateFood));
            // Thread.sleep(10000);
            Object o = nu.read();
            if (o instanceof Response) {
                response = (Response) o;
            }
            if (response != null && response.getMessage().equals("updated")) {
                System.out.println("food updated");

                Platform.runLater(() -> {
                    newFoodStg.close();
                });
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

    public void deliverOrder(Order order) {
        try {
            DeliverOrder deliverOrder = new DeliverOrder(order);
            nu.write(deliverOrder);

        } catch (Exception e) {
            System.out.println("Error: in deliverOrder " + e);
        }
    }

    synchronized void addNewFoodThread(Food food) {
        try {
            while (isNewOrderChecking) {
                wait();
            }

            nu.write(new AddFood(food));
            // Thread.sleep(10000);
            Object o = nu.read();
            if (o instanceof Response) {
                response = (Response) o;
            }
            if (response != null && response.getMessage().equals("added")) {
                System.out.println("food added");

                Platform.runLater(() -> {
                    newFoodStg.close();
                });
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
        nu.write(new SearchFoodGivenRestaurant(foodName, by, resId));

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
        nu.write(new SearchFoodGivenRestaurant(minPrice, maxPrice, by, resId));

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

                    isNewOrderChecking = false;

                    if (ordersController != null)
                        new Thread(() -> getRestaurantOrders()).start();

                    Platform.runLater(() -> {
                        if (controller != null) {
                            controller.newOrderCount.setText(newOrder.getMessage());
                            controller.newOrderCount.setVisible(true);
                            controller.newOrderCount.setManaged(true);
                        }

                        if (ordersController != null) {
                            ordersController.newOrderCount.setText(newOrder.getMessage());
                            ordersController.newOrderCount.setVisible(true);
                            ordersController.newOrderCount.setManaged(true);
                        }
                    });
                }

                else {
                    Platform.runLater(() -> {
                        if (controller != null) {
                            controller.newOrderCount.setText("");
                            controller.newOrderCount.setVisible(false);
                            controller.newOrderCount.setManaged(false);
                        }

                        if (ordersController != null) {
                            ordersController.newOrderCount.setText("");
                            ordersController.newOrderCount.setVisible(false);
                            ordersController.newOrderCount.setManaged(false);
                        }
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