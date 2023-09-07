package client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import requests.SearchFood;
import threads.*;
import util.*;

public class CustomerUser {
    NetworkUtil nu;
    int id;
    volatile Response response;
    volatile boolean isOrderCheckingRunning;

    public CustomerUser(NetworkUtil nu, int id) throws ClassNotFoundException, IOException {
        this.nu = nu;
        this.id = id;
        isOrderCheckingRunning = true;
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
            while (true) {
                System.out.println("1.Search Food");
                System.out.println("2.Logout");

                Scanner sc = new Scanner(System.in);
                int choice = Integer.parseInt(sc.nextLine());
                if (choice == 1) {
                    System.out.println("Enter Food Name: ");
                    String foodName = sc.nextLine();

                    nu.write(new SearchFood(foodName, "name"));
                    isOrderCheckingRunning = false;
                    SendRequest SearchFoodThread = new SendRequest(this);
                    SearchFoodThread.join();

                    if (response != null && response.getMessage().equals("found")) {
                        ArrayList<Food> foods = (ArrayList<Food>) response.getData();

                        for (int i = 1; i <= foods.size(); i++)
                            System.out.println(i + ". " + foods.get(i - 1).getName());
                        showOrderFood(foods);
                    }

                }
            }
        } catch (Exception e) {
            System.out.println("Error: in search Food " + e);
        }
    }

    private void showOrderFood(ArrayList<Food> foods) throws Exception {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("1. Order Food");
            System.out.println("2. Back");
            int choice = Integer.parseInt(sc.nextLine());

            if (choice == 1) {
                System.out.println("Enter Food Numbers to order seperated by space: ");
                String foodNumbers = sc.nextLine();
                orderFood(foodNumbers, foods);
            }

            else
                return;
        }

    }

    private void orderFood(String foodNumbers, ArrayList<Food> foods) throws Exception {
        if (foodNumbers.length() == 0)
            return;
        String[] foodIntNumbers = foodNumbers.split(" ");

        for (String number : foodIntNumbers) {
            int foodNumber = Integer.parseInt(number);
            Food food = foods.get(foodNumber - 1);
            nu.write(new Order(id, food, false));

            isOrderCheckingRunning = false;
            SendRequest sendRequest = new SendRequest(this);
            sendRequest.join();

            System.out.println(response.getMessage());
        }

    }

    synchronized public void sendRequest() throws Exception {
        System.out.println("order checking");
        while (isOrderCheckingRunning)
            wait();

        System.out.println("reading food...");
        Object o = nu.read();
        System.out.println("food readed");
        if (o instanceof Response)
            response = (Response) o;

        isOrderCheckingRunning = true;
    }

}
