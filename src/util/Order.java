package util;

public class Order implements java.io.Serializable {
    private int id;
    private int customerId;
    private String customerName;
    private Food food;
    Boolean isAccepted;

    public Order(int customerId, Food food, Boolean isAccepted) {
        this.customerId = customerId;
        this.food = food;
        this.isAccepted = isAccepted;
    }

    public Order(int customerId, String customerName, Food food, Boolean isAccepted) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.food = food;
        this.isAccepted = isAccepted;
    }

    public Order(int id, int customerId, Food food, Boolean isAccepted) {
        this.id = id;
        this.customerId = customerId;
        this.food = food;
        this.isAccepted = isAccepted;
    }

    public Order(int id, int customerId, String customerName, Food food, Boolean isAccepted) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.food = food;
        this.isAccepted = isAccepted;
    }

    public Order() {
        this.customerId = 0;
        this.food = new Food();
        isAccepted = false;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public void setAccepted(Boolean accepted) {
        isAccepted = accepted;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Food getFood() {
        return food;
    }

    public int getResId() {
        return food.getResId();
    }

    public boolean isAccepted() {
        return isAccepted;
    }
}
