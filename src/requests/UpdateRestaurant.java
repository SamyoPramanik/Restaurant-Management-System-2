package requests;

import java.io.Serializable;

import util.RestaurantInfo;

public class UpdateRestaurant implements Serializable {
    public RestaurantInfo restaurant, updateRestaurant;

    public UpdateRestaurant(RestaurantInfo restaurant, RestaurantInfo updateRestaurant) {
        this.restaurant = restaurant;
        this.updateRestaurant = updateRestaurant;
    }

}
