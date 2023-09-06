package util;

public class Order implements java.io.Serializable {
    private int customerId;
    private Food food;
    Boolean isAccepted;
    int resId;

    public Order(int customerId, Food food, Boolean isAccepted, int resId) {
        this.customerId = customerId;
        this.food = food;
        this.resId = resId;
        this.isAccepted = isAccepted;
    }

    public Order() {
        this.customerId = 0;
        this.food = new Food();
        this.resId = 0;
        isAccepted = false;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public void setResId(int resId) {
        this.resId = resId;
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
        return resId;
    }

    public int getRestaurantId() {
        return 0;
    }

    public boolean isAccepted() {
        return false;
    }
}
