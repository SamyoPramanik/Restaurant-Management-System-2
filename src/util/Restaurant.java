package util;

import java.util.ArrayList;

public class Restaurant implements java.io.Serializable {
    volatile private int id;
    volatile private String name;
    volatile private double score;
    volatile private String price;
    volatile private String zipcode;
    volatile private String category1;
    volatile private String category2;
    volatile private String category3;
    volatile private double maxPriceOfFood;
    volatile private int newOrderCount = 0;

    volatile private ArrayList<Food> menu = new ArrayList<Food>();
    volatile private ArrayList<Order> orders = new ArrayList<Order>();

    public Restaurant() {
        maxPriceOfFood = 0;
        ;
    }

    public Restaurant(int id, String name, double score, String price, String zipcode, String category1,
            String category2,
            String category3) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.price = price;
        this.zipcode = zipcode;
        this.category1 = category1;
        this.category2 = category2;
        this.category3 = category3;
        this.maxPriceOfFood = 0;
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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getScore() {
        return score;
    }

    public String getPrice() {
        return price;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getCategory1() {
        return category1;
    }

    public String getCategory2() {
        return category2;
    }

    public String getCategory3() {
        return category3;
    }

    public boolean hasCategory(String category) {
        return category1.equalsIgnoreCase(category) || category2.equalsIgnoreCase(category)
                || category3.equalsIgnoreCase(category);
    }

    public void addFood(String category, String name, double price) {
        menu.add(new Food(id, this.name, category, name, price));

        if (price > maxPriceOfFood)
            maxPriceOfFood = price;
    }

    public ArrayList<Food> showFoodByName(String name) {
        ArrayList<Food> foods = new ArrayList<Food>();
        for (Food food : menu) {
            if (isContains(food.getName(), name)) {
                foods.add(food);
            }
        }

        return foods;

    }

    public int getNumberOfFood() {
        return menu.size();
    }

    public ArrayList<Food> showFoodByPrice(double minPrice, double maxPrice) {
        ArrayList<Food> foods = new ArrayList<Food>();
        for (Food food : menu) {
            if (minPrice <= food.getPrice() && food.getPrice() <= maxPrice) {
                foods.add(food);
            }
        }

        return foods;
    }

    public ArrayList<Food> showCostliestFood() {
        ArrayList<Food> costliestFood = new ArrayList<Food>();
        for (Food food : menu) {
            if (food.getPrice() >= maxPriceOfFood) {
                costliestFood.add(food);
            }
        }

        return costliestFood;
    }

    public void showAllFood() {
        for (Food food : menu) {
            System.out.println();
            food.showWithoutResName();
            System.out.println();
        }
    }

    public void show() {
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Score: " + score);
        System.out.println("Zip Code: " + zipcode);
        System.out.println("Categories: ");
        System.out.println("  " + category1);
        System.out.println("  " + category2);
        System.out.println("  " + category3);
    }

    public ArrayList<Food> showFoodByCategory(String name) {
        ArrayList<Food> foods = new ArrayList<Food>();
        for (Food food : menu) {
            if (isContains(food.getCategory(), name)) {
                foods.add(food);
            }
        }

        return foods;
    }

    public Food searchFood(String foodName, String foodCategory) {
        for (Food food : menu) {
            if (food.getName().equalsIgnoreCase(foodName) && food.getCategory().equalsIgnoreCase(foodCategory)) {
                return food;
            }
        }

        return null;
    }

    public void addOrder(Order o) {

        orders.add(o);
    }

    synchronized public void addNewOrder(Order o) {
        orders.add(0, o);
        newOrderCount++;
    }

    public int getNewOrderCount() {
        return newOrderCount;
    }

    public void resetOrder() {
        newOrderCount = 0;
    }

    public ArrayList<Order> getOrders() {
        newOrderCount = 0;
        return orders;
    }

    public void deliverOrder(Order order) {
        System.out.println(order.getFood().getName() + " delivered");

        for (Order o : orders)
            if (o.getId() == order.getId()) {
                o.setAccepted(true);
                break;
            }
        for (Order o : orders) {
            System.out.print(o.isAccepted() + " ");
        }
        System.out.println();
    }

}