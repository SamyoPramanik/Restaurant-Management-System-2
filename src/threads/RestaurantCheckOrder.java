package threads;

import client.CustomerUser;
import client.RestaurantUser;
import requests.CheckNewOrder;
import util.Customer;
import util.NetworkUtil;
import util.Response;

public class RestaurantCheckOrder implements Runnable {
    public Thread t;
    RestaurantUser ru;
    NetworkUtil nu;

    public RestaurantCheckOrder(RestaurantUser ru) {
        this.ru = ru;
        this.nu = ru.nu;

        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            ru.showNewOrder();

        } catch (Exception e) {
            System.out.println("Error: in Restaurant CheckOrder " + e);
        }
    }

}
