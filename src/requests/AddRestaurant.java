package requests;

import java.io.Serializable;

import util.RestaurantInfo;

public class AddRestaurant implements Serializable {
    public RestaurantInfo restaurant;

    public AddRestaurant(RestaurantInfo restaurant) {
        this.restaurant = restaurant;
    }

}
