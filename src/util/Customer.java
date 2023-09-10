package util;

import java.util.ArrayList;

public class Customer implements java.io.Serializable {
    private int id;
    private String name;
    ArrayList<Order> myOrders;

    public Customer(int id, String name) {
        this.id = id;
        this.name = name;
        myOrders = new ArrayList<Order>();
    }

    synchronized public void addOrder(Order order) {
        myOrders.add(order);
    }

    public void removeOrder(Order order) {
        myOrders.remove(order);
    }

    public void setOrders(ArrayList<Order> orders) {
        myOrders = orders;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Order> getOrders() {
        return myOrders;
    }

    public void deliverOrder(Order order) {
        for (Order o : myOrders)
            if (o.getId() == order.getId()) {
                o.setAccepted(true);
                break;
            }
        for (Order o : myOrders) {
            System.out.print(o.isAccepted() + " ");
        }
        System.out.println();
    }

}
