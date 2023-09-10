package util;

import java.util.ArrayList;

public class ResCategory {
    private String name;

    private ArrayList<String> list = new ArrayList<String>();

    public ResCategory() {
        name = "";
    }

    public ResCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getRestaurantList() {
        return list;
    }

    public void show() {
        System.out.print(name + ": ");
        int count = 0;
        for (String s : list) {
            if (count++ == 0)
                System.out.print(s);
            else
                System.out.print(", " + s);
        }
        System.out.println();
    }

    public void addRestaurant(String restaurant) {
        list.add(new String(restaurant));
    }

    public boolean hasRestaurant(String restaurant) {
        for (String s : list) {
            if (s.equalsIgnoreCase(restaurant))
                return true;
        }
        return false;
    }

    public void removeRestaurant(String restaurant) {
        list.remove(restaurant);
    }

}
