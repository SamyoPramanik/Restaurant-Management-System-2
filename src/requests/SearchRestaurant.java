package requests;

import java.io.Serializable;

public class SearchRestaurant implements Serializable {
    public String str;
    public double minScore;
    public double maxScore;
    public String by;

    public SearchRestaurant(String str, String by) {
        this.str = str;
        this.by = by;
    }

    public SearchRestaurant(double minScore, double maxScore, String by) {
        this.minScore = minScore;
        this.maxScore = maxScore;
        this.by = by;
    }

}
