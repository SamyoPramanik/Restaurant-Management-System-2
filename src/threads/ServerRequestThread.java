package threads;

import java.util.ArrayList;

import requests.*;
import server.RestaurantManagement;
import util.*;

public class ServerRequestThread implements Runnable {
    volatile private RestaurantManagement res;
    private NetworkUtil nu;

    public ServerRequestThread(RestaurantManagement res, NetworkUtil nu) {

        this.res = res;
        this.nu = nu;

        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {

            while (true) {
                Object request = nu.read();

                if (request instanceof String) {
                    String str = (String) request;
                    System.out.println(str);
                }

                if (request instanceof Login) {
                    System.out.println("login request");
                    Login login = (Login) request;
                    Response response = res.searchUser(login.getUsername(), login.getPassword());
                    System.out.println(response.getMessage());
                    nu.write(response);
                    System.out.println("login response sent");
                }

                else if (request instanceof Register) {
                    System.out.println("Register request");
                    Register register = (Register) request;
                    Response response = res.insertUser(register.name, register.username,
                            register.password, register.type);

                    System.out.println(response.getMessage());
                    nu.write(response);
                    System.out.println("Register response sent");
                }

                else if (request instanceof SearchRestaurant) {
                    SearchRestaurant search = (SearchRestaurant) request;

                    if (search.by.equals("name")) {
                        ArrayList<Restaurant> r = res.searchRestaurantByName(search.str);

                        Response response = new Response("found", r);
                        nu.write(response);

                    }

                    else if (search.by.equals("category")) {
                        ArrayList<Restaurant> r = res.searchRestaurantByCategory(search.str);

                        Response response = new Response("found", r);
                        nu.write(response);
                    }

                    else if (search.by.equals("price")) {
                        ArrayList<Restaurant> r = res.searchRestaurantByPrice(search.str);

                        Response response = new Response("found", r);
                        nu.write(response);
                    }

                    else if (search.by.equals("zipcode")) {
                        ArrayList<Restaurant> r = res.searchRestaurantByZipCode(search.str);

                        Response response = new Response("found", r);
                        nu.write(response);
                    }

                    else if (search.by.equals("score")) {
                        ArrayList<Restaurant> r = res.searchRestaurantByScore(search.minScore,
                                search.maxScore);

                        Response response = new Response("found", r);
                        nu.write(response);
                    }

                }

                else if (request instanceof SearchFood) {
                    System.out.println("Search Food request");

                    SearchFood search = (SearchFood) request;

                    if (search.by.equals("name")) {
                        ArrayList<Food> foods = res.searchFoodByName(search.str);
                        sendSearchFoodResponse(foods);

                    }

                    else if (search.by.equals("category")) {
                        ArrayList<Food> foods = res.searchFoodByCategory(search.str);
                        sendSearchFoodResponse(foods);

                    }

                    else if (search.by.equals("price")) {
                        ArrayList<Food> foods = res.searchFoodByPriceRange(search.minScore, search.maxScore);
                        sendSearchFoodResponse(foods);
                    }
                }

                else if (request instanceof Order) {
                    Order order = (Order) request;
                    Response response = res.addNewOrder(order.getCustomerId(), order.getFood(), order.getResId(),
                            order.isAccepted());
                    nu.write(response);
                }

                else if (request instanceof CheckNewOrder) {
                    CheckNewOrder search = (CheckNewOrder) request;

                    int newOrders = res.getNewOrders(search.resId);
                    Response response;

                    if (newOrders > 0)
                        response = new Response(newOrders + " new orders", null);
                    else
                        response = new Response("no new order", null);

                    nu.write(response);

                }

                else if (request instanceof RestaurantGetOrder) {
                    RestaurantGetOrder order = (RestaurantGetOrder) request;

                    ArrayList<Order> orders = res.getOrders(order.resId);
                    System.out.println("orders: " + orders.size());
                    Response response = new Response("orders", orders);
                    nu.write(response);
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    private void sendSearchFoodResponse(ArrayList<Food> foods) throws Exception {
        System.out.println("serch food response sending...");
        Response response = new Response("found", foods);
        if (foods.size() == 0)
            response.setMessage("not found");

        nu.write(response);
    }
}
