package requests;

import java.io.Serializable;

import util.Order;

public class DeliverOrder implements Serializable {
    public Order order;

    public DeliverOrder(Order order) {
        this.order = order;
    }
}
