package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

import util.Admin;
import util.Customer;
import util.Food;
import util.Order;
import util.ResCategory;
import util.Response;
import util.Restaurant;
import util.User;

public class RestaurantManagement {
    private String menuFile;
    private String restaurantFile;
    private String usersFile;
    private int updateCount = 0;

    static Scanner scn = new Scanner(System.in);
    volatile private ArrayList<Restaurant> restaurantList = new ArrayList<Restaurant>();
    volatile private ArrayList<ResCategory> categories = new ArrayList<ResCategory>();
    volatile private ArrayList<Food> foodList = new ArrayList<Food>();
    volatile private ArrayList<User> userList = new ArrayList<User>();
    volatile private ArrayList<Customer> customerList = new ArrayList<Customer>();
    private Admin admin = new Admin("admin");

    synchronized private void increaseUpdate() {
        System.out.println("update count: " + updateCount);
        updateCount++;
        System.out.println("update count: " + updateCount);
    }

    public int getUpdateCount() {
        return updateCount;
    }

    private boolean isContains(String s1, String s2) {
        String ss1 = s1.toLowerCase();
        String ss2 = s2.toLowerCase();

        int idx = ss1.indexOf(ss2);

        if (idx == 0)
            return true;

        else if (idx == -1)
            return false;

        return (ss1.charAt(idx - 1) == ' ');
    }

    public void setMenuFile(String menuFile) {
        this.menuFile = menuFile;
    }

    public void setRestaurantFile(String restaurantFile) {
        this.restaurantFile = restaurantFile;
    }

    public void setUsersFile(String usersFile) {
        this.usersFile = usersFile;
    }

    void insertRestaurant(String name, double score, String price, String zipcode, String category1,
            String category2, String category3) {

        String category[] = { category1, category2, category3 };

        restaurantList
                .add(new Restaurant(restaurantList.size() + 1, name, score, price, zipcode, category[0], category[1],
                        category[2]));

        for (String c : category) {
            if (c.isEmpty())
                continue;
            boolean found = false;
            for (ResCategory rc : categories) {
                if (rc.getName().equalsIgnoreCase(c)) {
                    found = true;
                    rc.addRestaurant(name);
                }
            }

            if (!found) {
                ResCategory rc = new ResCategory(c);
                rc.addRestaurant(name);
                categories.add(rc);
            }
        }
    }

    void insertFood(int restaurantId, String category, String name, double price) {
        String restaurantName = "";
        for (Restaurant r : restaurantList) {
            if (r.getId() == restaurantId) {
                restaurantName = r.getName();
                r.addFood(category, name, price);
                break;
            }
        }

        foodList.add(new Food(restaurantId, restaurantName, category, name, price));
    }

    public Response insertUser(String name, String username, String password, String type) {
        int id = userList.size() + 1;
        userList.add(new User(username, password, type, id));
        customerList.add(new Customer(id, name));
        increaseUpdate();
        return new Response("User added", id);
    }

    public Response searchUser(String username, String password) {
        for (User u : userList) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                System.out.println(u.getUsername() + " " + u.getPassword());
                System.out.println("user found");
                if (u.getType().equals("customer")) {
                    for (Customer c : customerList) {
                        if (c.getId() == u.getId()) {
                            Response response = new Response("customer", c.getId());
                            return response;
                        }
                    }
                }

                else if (u.getType().equals("restaurant")) {
                    for (Restaurant r : restaurantList) {
                        if (r.getId() == u.getId()) {
                            Response response = new Response("restaurant", r.getId());
                            return response;
                        }
                    }
                }

                else if (u.getType().equals("admin")) {
                    Response response = new Response("admin", admin);
                    return response;
                }
            }

        }
        return new Response("Invalid username password", null);

    }

    public Response addOrder(int customerId, Food food, int restaurantId, boolean isAccepted) {
        Boolean orderInRestaurant = false, orderInCustomer = false;
        for (Restaurant r : restaurantList) {
            if (r.getId() == restaurantId) {
                r.addOrder(customerId, food.getName(), food.getCategory(), isAccepted);
                food = r.searchFood(food.getName(), food.getCategory());
                orderInRestaurant = true;
                break;
            }
        }

        for (Customer c : customerList) {
            if (c.getId() == customerId) {
                c.addOrder(new Order(customerId, food, isAccepted));
                orderInCustomer = true;
                break;
            }
        }

        if (orderInRestaurant && orderInCustomer) {
            increaseUpdate();
            return new Response("Order added", null);
        }

        return new Response("Order not added", null);
    }

    public Response addNewOrder(int customerId, Food food, int restaurantId, boolean isAccepted) {
        Boolean orderInRestaurant = false, orderInCustomer = false;
        for (Restaurant r : restaurantList) {
            if (r.getId() == restaurantId) {
                r.addNewOrder(customerId, food.getName(), food.getCategory(), isAccepted);
                food = r.searchFood(food.getName(), food.getCategory());
                orderInRestaurant = true;
                break;
            }
        }

        for (Customer c : customerList) {
            if (c.getId() == customerId) {
                c.addOrder(new Order(customerId, food, isAccepted));
                orderInCustomer = true;
                break;
            }
        }

        if (orderInRestaurant && orderInCustomer) {
            increaseUpdate();
            return new Response("Order added", null);
        }

        return new Response("Order not added", null);
    }

    public ArrayList<Restaurant> searchRestaurantByName(String name) {
        ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
        for (Restaurant r : restaurantList) {
            if (isContains(r.getName(), name)) {
                restaurants.add(r);
            }
        }

        return restaurants;
    }

    public Restaurant searchRestaurantById(int id) {
        for (Restaurant r : restaurantList) {
            if (r.getId() == id) {
                return r;
            }
        }

        return null;
    }

    public ArrayList<Restaurant> searchRestaurantByScore(double minScore, double maxScore) {
        ArrayList<Restaurant> res = new ArrayList<Restaurant>();

        for (Restaurant r : restaurantList) {
            if (minScore <= r.getScore() && r.getScore() <= maxScore) {
                res.add(r);
            }
        }
        return res;
    }

    public ArrayList<Restaurant> searchRestaurantByPrice(String price) {
        ArrayList<Restaurant> res = new ArrayList<Restaurant>();

        for (Restaurant r : restaurantList) {
            if (r.getPrice().equalsIgnoreCase(price)) {
                res.add(r);
            }
        }

        return res;
    }

    public ArrayList<Restaurant> searchRestaurantByZipCode(String zipcode) {
        ArrayList<Restaurant> res = new ArrayList<Restaurant>();

        for (Restaurant r : restaurantList) {
            if (r.getZipcode().equalsIgnoreCase(zipcode)) {
                res.add(r);
            }
        }
        return res;
    }

    public ArrayList<Restaurant> searchRestaurantByCategory(String category) {
        ArrayList<Restaurant> res = new ArrayList<Restaurant>();

        for (Restaurant r : restaurantList) {
            if (r.hasCategory(category)) {
                res.add(r);
            }
        }
        System.out.println(res.size());
        return res;
    }

    public int getNewOrders(int resId) {
        Restaurant r = searchRestaurantById(resId);
        int order = r.getNewOrderCount();
        r.resetOrder();

        System.out.println("number of new Orders in " + r.getName() + ": " + order);

        return order;
    }

    public ArrayList<Order> getOrders(int resId) {
        Restaurant r = searchRestaurantById(resId);
        ArrayList<Order> orders = r.getOrders();
        System.out.println("number of Orders in " + r.getName() + ": " + orders.size());
        return orders;
    }

    public ArrayList<ResCategory> showAll() {
        ArrayList<ResCategory> rcs = new ArrayList<ResCategory>();

        for (ResCategory rc : categories) {
            rcs.add(rc);
        }

        return rcs;
    }

    public ArrayList<Food> searchFoodByName(String name) {
        ArrayList<Food> foods = new ArrayList<Food>();

        for (Food food : foodList) {
            if (isContains(food.getName(), name)) {
                foods.add(food);
            }
        }

        return foods;
    }

    public ArrayList<Food> searchFoodByNameGivenRestaurant(String foodName, String resName) {
        ArrayList<Food> foods = new ArrayList<Food>();

        for (Restaurant r : restaurantList) {
            if (isContains(r.getName(), resName)) {
                foods = r.showFoodByName(foodName);
                break;
            }
        }

        return foods;
    }

    public ArrayList<Food> searchFoodByCategory(String category) {
        ArrayList<Food> foods = new ArrayList<Food>();

        for (Food food : foodList) {
            if (isContains(food.getCategory(), category)) {
                foods.add(food);
            }
        }

        return foods;
    }

    public Food searchFood(String name, String category) {
        for (Food food : foodList) {
            if (food.getName().equalsIgnoreCase(name) && food.getCategory().equalsIgnoreCase(category)) {
                return food;
            }
        }

        return null;
    }

    public ArrayList<Food> searchFoodByCategoryGivenRestaurant(String category, String resName) {
        ArrayList<Food> foods = new ArrayList<Food>();

        for (Restaurant r : restaurantList) {
            if (isContains(r.getName(), resName)) {
                foods = r.showFoodByCategory(category);
                break;
            }
        }

        return foods;
    }

    public ArrayList<Food> searchFoodByPriceRange(double minPrice, double maxPrice) {
        ArrayList<Food> foods = new ArrayList<Food>();

        for (Food food : foodList) {
            if (minPrice <= food.getPrice() && food.getPrice() <= maxPrice) {
                foods.add(food);
            }
        }

        return foods;
    }

    public ArrayList<Food> searchFoodByPriceRangeGivenRestaurant(double minPrice, double maxPrice, String resName) {
        ArrayList<Food> foods = new ArrayList<Food>();

        for (Restaurant r : restaurantList) {
            if (r.getName().equalsIgnoreCase(resName)) {
                foods = r.showFoodByPrice(minPrice, maxPrice);
                break;
            }
        }

        return foods;
    }

    public ArrayList<Food> searchCostliestFood(String resName) {
        ArrayList<Food> foods = new ArrayList<Food>();
        for (Restaurant r : restaurantList) {
            if (r.getName().equalsIgnoreCase(resName)) {
                foods = r.showCostliestFood();
                break;
            }
        }
        System.out.println("search costliest");
        return foods;
    }

    boolean hasRestaurant(String name) {
        for (Restaurant r : restaurantList) {
            if (r.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
    }

    boolean hasRestaurant(int id) {
        for (Restaurant r : restaurantList) {
            if (r.getId() == id) {
                return true;
            }
        }

        return false;
    }

    void searchFood() {
        while (true) {
            System.out.println("\nFood Item Searching Options:\r\n" + //
                    "1) By Name\r\n" + //
                    "2) By Name in a Given Restaurant\r\n" + //
                    "3) By Category\r\n" + //
                    "4) By Category in a Given Restaurant\r\n" + //
                    "5) By Price Range\r\n" + //
                    "6) By Price Range in a Given Restaurant\r\n" + //
                    "7) Costliest Food Item(s) on the Menu in a Given Restaurant\r\n" + //
                    "8) ArrayList of Restaurants and Total Food Item on the Menu\r\n" + //
                    "9) Back to Main Menu\n");

            int choice = Integer.parseInt(scn.nextLine());

            switch (choice) {
                case 1: {
                    System.out.println("Food name: ");
                    String name = scn.nextLine();

                    ArrayList<Food> foods = searchFoodByName(name);

                    if (foods.isEmpty()) {
                        System.out.println("\nNo such food item with this name\n");
                        break;
                    }

                    System.out.println();
                    for (Food food : foods) {
                        food.show();
                        System.out.println();
                    }

                    break;
                }

                case 2: {
                    System.out.println("Food name: ");
                    String foodName = scn.nextLine();

                    System.out.println("Restaurant name: ");
                    String resName = scn.nextLine();

                    if (hasRestaurant(resName) == false) {
                        System.out.println("\nNo such restaurant with this name\n");
                        break;
                    }

                    ArrayList<Food> foods = searchFoodByNameGivenRestaurant(foodName, resName);

                    if (foods.isEmpty()) {
                        System.out.println("\nNo such food in " + resName + "\n");
                        break;
                    }

                    System.out.println();
                    for (Food food : foods) {
                        food.show();
                        System.out.println();
                    }

                    break;
                }

                case 3: {
                    System.out.println("Food category: ");
                    String category = scn.nextLine();

                    ArrayList<Food> foods = searchFoodByCategory(category);

                    if (foods.isEmpty()) {
                        System.out.println("\nNo food in this category\n");
                        break;
                    }

                    System.out.println();
                    for (Food food : foods) {
                        food.show();
                        System.out.println();
                    }

                    break;

                }

                case 4: {
                    System.out.println("Food category: ");
                    String category = scn.nextLine();

                    System.out.println("Restaurant name: ");
                    String resName = scn.nextLine();

                    if (hasRestaurant(resName) == false) {
                        System.out.println("\nNo such restaurant with this name\n");
                        break;
                    }

                    ArrayList<Food> foods = searchFoodByCategoryGivenRestaurant(category, resName);

                    if (foods.isEmpty()) {
                        System.out.println("\nNo food in this category\n");

                        break;
                    }

                    System.out.println();
                    for (Food food : foods) {
                        food.show();
                        System.out.println();
                    }

                    break;

                }

                case 5: {
                    System.out.println("Min price:");
                    double minPrice = Double.parseDouble(scn.nextLine());

                    System.out.println("Max price:");
                    double maxPrice = Double.parseDouble(scn.nextLine());

                    ArrayList<Food> foods = searchFoodByPriceRange(minPrice, maxPrice);

                    if (foods.isEmpty()) {
                        System.out.println("\nNo food in this category\n");
                        break;
                    }

                    System.out.println();
                    for (Food food : foods) {
                        food.show();
                        System.out.println();
                    }
                    break;
                }

                case 6: {
                    System.out.println("Min price:");
                    double minPrice = Double.parseDouble(scn.nextLine());

                    System.out.println("Max price:");
                    double maxPrice = Double.parseDouble(scn.nextLine());

                    System.out.println("Restaurant name: ");
                    String resName = scn.nextLine();

                    if (hasRestaurant(resName) == false) {
                        System.out.println("\nNo such restaurant with this name\n");
                        break;
                    }

                    ArrayList<Food> foods = searchFoodByPriceRangeGivenRestaurant(minPrice, maxPrice, resName);

                    if (foods.isEmpty()) {
                        System.out.println("\nNo food in this price range\n");
                        break;
                    }

                    System.out.println();
                    for (Food food : foods) {
                        food.show();
                        System.out.println();
                    }
                    break;
                }

                case 7: {
                    System.out.println("Restaurant name: ");
                    String resName = scn.nextLine();

                    if (hasRestaurant(resName) == false) {
                        System.out.println("\nNo such restaurant with this name\n");
                        break;
                    }

                    ArrayList<Food> foods = searchCostliestFood(resName);

                    if (foods.size() == 0) {
                        System.out.println("\nNo food in this restaurant\n");
                        break;
                    }

                    System.out.println();
                    for (Food food : foods) {
                        food.show();
                        System.out.println();
                    }

                    break;
                }

                case 8: {
                    System.out.println();

                    for (Restaurant r : restaurantList) {
                        System.out.println(r.getName() + ": " + r.getNumberOfFood());
                    }
                    System.out.println();

                    break;
                }

                case 9: {
                    return;
                }

                default: {
                    System.out.println("Invalid choice");
                    break;
                }
            }
        }
    }

    synchronized public void saveAll() throws Exception {
        System.out.println("saving files");
        BufferedWriter br = new BufferedWriter(new FileWriter(restaurantFile));

        for (Restaurant r : restaurantList) {
            br.write(r.getId() + "," + r.getName() + "," + r.getScore() + "," + r.getPrice() + "," + r.getZipcode()
                    + "," + r.getCategory1() + "," + r.getCategory2() + "," + r.getCategory3() + "\n");
        }

        br.close();

        br = new BufferedWriter(new FileWriter(menuFile));

        for (Food f : foodList) {
            br.write(f.getResId() + "," + f.getCategory() + "," + f.getName() + "," + f.getPrice() + "\n");
        }

        br.close();

        br = new BufferedWriter(new FileWriter(usersFile));
        for (User u : userList) {
            br.write(u.getUsername() + "," + u.getPassword() + "," + u.getType() + "," + u.getId() + "\n");
        }
        br.close();

        br = new BufferedWriter(new FileWriter("customer.txt"));
        for (Customer c : customerList) {
            br.write(c.getId() + "," + c.getName() + "\n");
        }
        br.close();

        br = new BufferedWriter(new FileWriter("orders.txt"));
        for (Customer c : customerList) {
            for (Order o : c.getOrders()) {
                br.write(o.getCustomerId() + "," + o.getFood().getName() + "," + o.getFood().getCategory() + ","
                        + o.getResId() + "," + (o.isAccepted() ? 1 : 0) + "\n");
            }
        }
        br.close();

        updateCount = 0;
    }

    void loadFiles() throws Exception {

        BufferedReader br = new BufferedReader(new FileReader(restaurantFile));
        while (true) {
            String line = br.readLine();

            if (line == null)
                break;

            String[] parts = line.split(",", -1);

            int id = Integer.parseInt(parts[0]);
            String name = parts[1];
            double score = Double.parseDouble(parts[2]);
            String price = parts[3];
            String zipcode = parts[4];
            String category1 = parts[5];
            String category2 = parts[6];
            String category3 = parts[7];

            restaurantList.add(new Restaurant(id, name, score, price, zipcode, category1, category2, category3));
        }
        br.close();

        System.out.println("All restaurant loaded");

        br = new BufferedReader(new FileReader(menuFile));
        while (true) {
            String line = br.readLine();

            if (line == null)
                break;

            String[] parts = line.split(",(?! )", -1);

            int restaurantId = Integer.parseInt(parts[0]);
            String category = parts[1];
            String name = parts[2];
            double price = Double.parseDouble(parts[3]);

            insertFood(restaurantId, category, name, price);

        }
        br.close();
        System.out.println("All food loaded");

        br = new BufferedReader(new FileReader(usersFile));
        while (true) {
            String line = br.readLine();

            if (line == null)
                break;

            String[] parts = line.split(",");

            String username = parts[0];
            String password = parts[1];
            String type = parts[2];
            int id = Integer.parseInt(parts[3]);

            userList.add(new User(username, password, type, id));
        }
        br.close();
        System.out.println("All users loaded");

        br = new BufferedReader(new FileReader("customer.txt"));
        while (true) {
            String line = br.readLine();

            if (line == null)
                break;

            String[] parts = line.split(",", -1);

            int id = Integer.parseInt(parts[0]);
            String name = parts[1];

            customerList.add(new Customer(id, name));
        }
        br.close();
        System.out.println("All customer loaded");

        br = new BufferedReader(new FileReader("orders.txt"));
        while (true) {
            String line = br.readLine();
            if (line == null)
                break;

            String[] parts = line.split(",", -1);

            int customerId = Integer.parseInt(parts[0]);
            String foodName = parts[1];
            String foodCategory = parts[2];
            int restaurantId = Integer.parseInt(parts[3]);
            int isAccepted = Integer.parseInt(parts[4]);

            for (Restaurant r : restaurantList)
                if (r.getId() == restaurantId) {
                    r.addOrder(customerId, foodName, foodCategory, isAccepted == 1 ? true : false);
                    break;
                }

            for (Customer c : customerList)
                if (c.getId() == customerId) {
                    c.addOrder(new Order(customerId, searchFood(foodName, foodCategory),
                            isAccepted == 1 ? true : false));
                    break;
                }
        }
        br.close();
        System.out.println("All customer loaded");

    }

}
