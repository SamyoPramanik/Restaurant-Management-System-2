package threads;

import java.util.ArrayList;
import java.util.List;

import client.CustomerUser;
import server.RestaurantManagement;
import util.Food;
import util.Response;

public class ShowFoodThread implements Runnable {
    CustomerUser customerUser;
    public Thread t;

    public ShowFoodThread(CustomerUser customerUser) {
        this.customerUser = customerUser;
        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (customerUser.getResponse() != null) {
                    Response response = customerUser.getResponse();
                    if (response.getMessage().equals("found")) {
                        Object data = response.getData();
                        List<Food> foods = new ArrayList<Food>();
                        foods = (List<Food>) data;

                        for (int i = 1; i <= foods.size(); i++)
                            System.out.println(i + ". " + foods.get(i - 1).getName());
                    }

                    break;
                }

            }
        } catch (Exception e) {
            System.out.println("Error: show food thread " + e);
        }
    }

}
