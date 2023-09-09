package requests;

import java.io.Serializable;

public class SearchFoodGivenRestaurant implements Serializable {
    public String str;
    public double minScore;
    public double maxScore;
    public String by;
    public int restaurantId;

    public SearchFoodGivenRestaurant(String str, String by, int restaurantId) {
        this.str = str;
        this.by = by;
        this.restaurantId = restaurantId;
    }

    public SearchFoodGivenRestaurant(double minScore, double maxScore, String by, int restaurantId) {
        this.minScore = minScore;
        this.maxScore = maxScore;
        this.by = by;
        this.restaurantId = restaurantId;
    }

}
