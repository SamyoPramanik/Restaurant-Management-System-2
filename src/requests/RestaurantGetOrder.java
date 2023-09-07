package requests;

import java.io.Serializable;

public class RestaurantGetOrder implements Serializable {
    public int resId;

    public RestaurantGetOrder(int resId) {
        this.resId = resId;
    }
}
