package client;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.NetworkChannel;
import java.util.Scanner;

import requests.*;
import util.*;

public class Client {
    Response response;
    NetworkUtil nu;

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
            Admin admin = (Admin) response.getData();
            System.out.println(admin.getName());
            showAdminHome(admin);
        }

        else if (response.getMessage().equals("customer")) {
            Customer customer = (Customer) response.getData();
        }

        else if (response.getMessage().equals("restaurant")) {
            Restaurant restaurant = (Restaurant) response.getData();
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
    }

    public void showAdminHome(Admin admin) {

    }

    public void logOut() {

    }

    public static void main(String[] args) {
        new Client();
    }
}
