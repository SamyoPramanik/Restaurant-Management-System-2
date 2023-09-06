package threads;

import java.util.List;

import requests.Login;
import requests.Register;
import requests.SearchRestaurant;
import server.RestaurantManagement;
import util.NetworkUtil;
import util.Response;
import util.Restaurant;
import util.SocketWrapper;

public class ServerRequestThread implements Runnable {
    private RestaurantManagement res;
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
                        List<Restaurant> r = res.searchRestaurantByName(search.str);

                        Response response = new Response("found", r);
                        nu.write(response);

                    }

                    else if (search.by.equals("category")) {
                        List<Restaurant> r = res.searchRestaurantByCategory(search.str);

                        Response response = new Response("found", r);
                        nu.write(response);
                    }

                    else if (search.by.equals("price")) {
                        List<Restaurant> r = res.searchRestaurantByPrice(search.str);

                        Response response = new Response("found", r);
                        nu.write(response);
                    }

                    else if (search.by.equals("zipcode")) {
                        List<Restaurant> r = res.searchRestaurantByZipCode(search.str);

                        Response response = new Response("found", r);
                        nu.write(response);
                    }

                    else if (search.by.equals("score")) {
                        List<Restaurant> r = res.searchRestaurantByScore(search.minScore,
                                search.maxScore);

                        Response response = new Response("found", r);
                        nu.write(response);
                    }

                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
