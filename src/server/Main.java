package server;

import java.net.ServerSocket;
import java.net.Socket;

import threads.CheckUpdateThread;
import threads.ServerRequestThread;
import util.NetworkUtil;
import util.SocketWrapper;

public class Main {
    public static void main(String[] args) {
        RestaurantManagement res = new RestaurantManagement();
        res.setMenuFile("menu.txt");
        res.setRestaurantFile("restaurant.txt");
        res.setUsersFile("users.txt");

        try {
            res.loadFiles();

            ServerSocket serverSocket = new ServerSocket(3000);
            System.out.println("server started");
            new CheckUpdateThread(res);

            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("new client connected");
                    NetworkUtil nu = new NetworkUtil(socket);
                    new ServerRequestThread(res, nu);

                } catch (Exception e) {
                    System.out.println("Error: vulval " + e);
                }
            }

        } catch (Exception e) {
            System.out.println("Error: vulval2  " + e);
        }
    }
}