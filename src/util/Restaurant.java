package util;

import java.util.ArrayList;
import java.util.List;

public class Restaurant implements java.io.Serializable {
    private int id;
    private String name;
    private double score;
    private String price;
    private String zipcode;
    private String category1;
    private String category2;
    private String category3;
    private double maxPriceOfFood;

    private List<Food> menu = new ArrayList<Food>();
    private List<Order> orders = new ArrayList<Order>();
    private List<Order> newOrders = new ArrayList<Order>();

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

    public List<Food> showFoodByName(String name) {
        List<Food> foods = new ArrayList<Food>();
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

    public List<Food> showFoodByPrice(double minPrice, double maxPrice) {
        List<Food> foods = new ArrayList<Food>();
        for (Food food : menu) {
            if (minPrice <= food.getPrice() && food.getPrice() <= maxPrice) {
                foods.add(food);
            }
        }

        return foods;
    }

    public List<Food> showCostliestFood() {
        List<Food> costliestFood = new ArrayList<Food>();
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

    public List<Food> showFoodByCategory(String name) {
        List<Food> foods = new ArrayList<Food>();
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

    public void addOrder(int customerId, String foodName, String foodCategory, Boolean isAccepted) {

        if (isAccepted)
            orders.add(new Order(customerId, searchFood(foodName, foodCategory), isAccepted, id));

        else
            newOrders.add(new Order(customerId, searchFood(foodName, foodCategory), isAccepted, id));
    }
}
