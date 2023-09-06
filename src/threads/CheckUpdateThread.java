package threads;

import server.RestaurantManagement;

public class CheckUpdateThread implements Runnable {
    private RestaurantManagement res;

    public CheckUpdateThread(RestaurantManagement res) {
        System.out.println("update thread created");
        this.res = res;
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (res.getUpdateCount() > 0) {
                    System.out.println("update found");
                    res.saveAll();
                    System.out.println("files saved");
                }
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println("Error: CheckUpdateThread " + e);
        }
    }

}
