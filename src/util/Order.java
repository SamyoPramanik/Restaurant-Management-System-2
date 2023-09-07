package util;

public class Order implements java.io.Serializable {
    private int customerId;
    private Food food;
    Boolean isAccepted;

    public Order(int customerId, Food food, Boolean isAccepted) {
        this.customerId = customerId;
        this.food = food;
        this.isAccepted = isAccepted;
    }

    public Order() {
        this.customerId = 0;
        this.food = new Food();
        isAccepted = false;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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

    public Food getFood() {
        return food;
    }

    public int getResId() {
        return food.getResId();
    }

    public boolean isAccepted() {
        return false;
    }
}
