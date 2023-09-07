package client;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.NetworkChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import requests.*;
import util.*;

public class Client {
    Response response;
    NetworkUtil nu;
    Customer customer;
    Restaurant restaurant;
    Admin admin;

    public Client() {
        try {
            nu = new NetworkUtil("127.0.0.1", 3000);
            while (true) {
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                Scanner sc = new Scanner(System.in);
                int choice = Integer.parseInt(sc.nextLine());
                if (choice == 1)
                    showRegister();

                if (choice == 2)
                    showLogin();
            }

        } catch (Exception e) {
            System.out.println("Error: vulval " + e);
        }
    }

    private void showLogin() throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter username: ");
        String username = sc.nextLine();

        System.out.println("Enter password: ");
        String password = sc.nextLine();

        nu.write(new Login(username, password));
        Response response = (Response) nu.read();

        if (response.getMessage().equals("admin")) {
            admin = (Admin) response.getData();
            System.out.println(admin.getName());
            showAdminHome(admin);
        }

        else if (response.getMessage().equals("customer")) {
            int customer = (int) response.getData();
            new CustomerUser(nu, customer);
        }

        else if (response.getMessage().equals("restaurant")) {
            int restaurant = (int) response.getData();
            new RestaurantUser(nu, restaurant);
        }

        else
            System.out.println(response.getMessage());
    }

    private void showRegister() throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter name: ");
        String name = sc.nextLine();

        System.out.println("Enter username: ");
        String username = sc.nextLine();

        System.out.println("Enter password: ");
        String password = sc.nextLine();

        nu.write(new Register(name, username, password, "customer"));
        Response response = (Response) nu.read();
        System.out.println(response.getMessage());
        int customer = (int) response.getData();

        new CustomerUser(nu, customer);
    }

    public void showAdminHome(Admin admin) {

    }

    public void logOut() {

    }

    public static void main(String[] args) {
        new Client();
    }
}
