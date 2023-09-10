package util;

public class Food implements java.io.Serializable {
    private int resId;
    private String resName;
    private String category;
    private String name;
    private double price;

    public Food() {
    }

    public Food(int resId, String resName, String category, String name, double price) {
        this.resId = resId;
        this.resName = resName;
        this.category = category;
        this.name = name;
        this.price = price;
    }

    public int getResId() {
        return resId;
    }

    public String getResName() {
        return resName;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        if (price < 0)
            this.price = 0;
        else
            this.price = price;
    }

    public void show() {
        System.out.println("Name: " + name);
        System.out.println("Category: " + category);
        System.out.println("Restaurant: " + resName);
        System.out.println("Price: " + price);
    }

    public void showWithoutResName() {
        System.out.println("Name: " + name);
        System.out.println("Category: " + category);
        System.out.println("Price: " + price);
    }
}
