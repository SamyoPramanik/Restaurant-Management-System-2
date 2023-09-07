package client;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import requests.CheckNewOrder;
import requests.RestaurantGetOrder;
import threads.RestaurantCheckOrder;
import threads.SendRequest;
import util.NetworkUtil;
import util.Order;
import util.Response;

public class RestaurantUser {
    public NetworkUtil nu;
    int resId;
    volatile public boolean isOrderChecking;
    volatile Response response;
    volatile Response newOrder;

    public RestaurantUser(NetworkUtil nu, int resId) {
        this.nu = nu;
        this.resId = resId;
        isOrderChecking = false;
        new RestaurantCheckOrder(this);

        showMain();
    }

    public int getId() {
        return resId;
    }

    public void showMain() {
        Scanner sc = new Scanner(System.in);
        try {
            while (true) {
                System.out.println("1. Search food");
                System.out.println("2. Show order");
                System.out.println("3. Exit");

                int choice = Integer.parseInt(sc.nextLine());

                if (choice == 2) {
                    nu.write(new RestaurantGetOrder(resId));
                    isOrderChecking = false;
                    SendRequest request = new SendRequest(this);
                    request.join();

                    if (response != null && response.getMessage().equals("orders")) {
                        Object data = response.getData();
                        List<Order> orders = (List) data;

                        for (int i = 1; i <= orders.size(); i++) {
                            System.out.println(i + ". " + orders.get(i - 1).getFood().getName());
                        }
                    }
                }

            }
        } catch (Exception e) {
            System.out.println("Error: in RestaurantUser showMain " + e);
        }
    }

    synchronized public void sendRequest() throws Exception {
        System.out.println("waiting for response...");
        while (isOrderChecking)
            wait();

        Object o = nu.read();
        if (o instanceof Response) {
            response = (Response) o;
            List<Order> orders = (List) response.getData();

            System.out.println("orders: " + orders.size());
        }
        // isOrderChecking = true;
        // notifyAll();
    }

    synchronized public void showNewOrder() throws Exception {
        try {
            while (true) {
                if (!isOrderChecking)
                    wait();

                System.out.println("searching for new order");

                nu.write(new CheckNewOrder(resId));
                Object o = nu.read();

                if (o instanceof Response) {
                    newOrder = (Response) o;
                }
                if (newOrder.getMessage().equals("no new order") == false)
                    System.out.println(newOrder.getMessage());

                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("Error: in RestaurantUser showNewOrder " + e);
        }
    }

}