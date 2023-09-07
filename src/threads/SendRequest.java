package threads;

import client.CustomerUser;
import client.RestaurantUser;
import util.Customer;
import util.NetworkUtil;

public class SendRequest implements Runnable {
    Object o;
    Thread t;

    public SendRequest(CustomerUser cu) {
        this.o = cu;

        t = new Thread(this);
        t.start();
    }

    public SendRequest(RestaurantUser ru) {
        this.o = ru;

        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            System.out.println("SendRequest");
            if (o instanceof CustomerUser) {
                CustomerUser cu = (CustomerUser) o;
                cu.sendRequest();
            }

            else if (o instanceof RestaurantUser) {
                RestaurantUser ru = (RestaurantUser) o;
                ru.sendRequest();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void join() throws InterruptedException {
        t.join();
    }
}
