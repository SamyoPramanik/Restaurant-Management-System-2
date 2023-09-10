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

                    ArrayList<RestaurantInfo> r = new ArrayList<>();
                    ArrayList<Restaurant> restaurants = null;
                    if (search.by.equals("name")) {
                        restaurants = res.searchRestaurantByName(search.str);

                    }

                    else if (search.by.equals("category")) {
                        restaurants = res.searchRestaurantByCategory(search.str);
                    }

                    else if (search.by.equals("zipcode")) {
                        restaurants = res.searchRestaurantByZipCode(search.str);
                    }

                    for (Restaurant restaurant : restaurants) {
                        r.add(new RestaurantInfo(restaurant.getId(), restaurant.getName(), restaurant.getScore(),
                                restaurant.getZipcode(),
                                restaurant.getCategory1(),
                                restaurant.getCategory2(), restaurant.getCategory3()));
                    }

                    System.out.println("total restaurants: " + r.size());

                    Response response = new Response("found", r);
                    nu.write(response);

                }

                else if (request instanceof AddRestaurant) {
                    System.out.println("Add Restaurant request");
                    AddRestaurant addRestaurant = (AddRestaurant) request;
                    Response response = res.insertRestaurant(addRestaurant.restaurant);
                    System.out.println(response.getMessage());
                    nu.write(response);
                    System.out.println("Add Restaurant response sent");
                }

                else if (request instanceof UpdateRestaurant) {
                    System.out.println("Update Restaurant request");
                    UpdateRestaurant updateRestaurant = (UpdateRestaurant) request;
                    Response response = res.updatRestaurant(updateRestaurant.restaurant,
                            updateRestaurant.updateRestaurant);
                    System.out.println(response.getMessage());
                    nu.write(response);
                    System.out.println("Update Restaurant response sent");
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

                else if (request instanceof SearchFoodGivenRestaurant) {
                    SearchFoodGivenRestaurant search = (SearchFoodGivenRestaurant) request;
                    Restaurant r = res.searchRestaurantById(search.restaurantId);

                    if (search.by.equals("name")) {
                        ArrayList<Food> foods = res.searchFoodByNameGivenRestaurant(search.str, r.getName());
                        sendSearchFoodResponse(foods);

                    }

                    else if (search.by.equals("category")) {
                        ArrayList<Food> foods = res.searchFoodByCategoryGivenRestaurant(search.str, r.getName());
                        sendSearchFoodResponse(foods);

                    }

                    else if (search.by.equals("price")) {
                        ArrayList<Food> foods = res.searchFoodByPriceRangeGivenRestaurant(search.minScore,
                                search.maxScore, r.getName());
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

                else if (request instanceof GetCustomerOrder) {
                    GetCustomerOrder getCustomerOrder = (GetCustomerOrder) request;
                    ArrayList<Order> orders = res.getCustomerOrders(getCustomerOrder.customerId);
                    System.out.println("orders: " + orders.size());
                    Response response = new Response(orders.size() + " orders found", orders);
                    nu.write(response);
                }

                else if (request instanceof AddFood) {
                    AddFood addFood = (AddFood) request;
                    Food food = addFood.food;
                    Response response = res.insertFood(food.getResId(), food.getCategory(), food.getName(),
                            food.getPrice());
                    nu.write(response);

                }

                else if (request instanceof UpdateFood) {
                    UpdateFood updateFood = (UpdateFood) request;
                    Food food = updateFood.food;
                    Food updateFood1 = updateFood.updateFood;
                    Response response = res.updateFood(food, updateFood1);
                    nu.write(response);
                }

                else if (request instanceof DeliverOrder) {
                    DeliverOrder deliverOrder = (DeliverOrder) request;
                    Order order = deliverOrder.order;
                    res.deliverFood(order);
                    System.out.println("order delivered");
                }

                else if (request instanceof GetRestaurantInfo) {
                    GetRestaurantInfo getRestaurantInfo = (GetRestaurantInfo) request;
                    Response response = res.getRestaurantInfo(getRestaurantInfo.resId);
                    nu.write(response);
                }

                else if (request instanceof GetAllCount) {
                    Response response = res.getAllCount();
                    nu.write(response);
                }

            }
        } catch (

        Exception e) {
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
