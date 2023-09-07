package requests;

import java.io.Serializable;

public class SearchFood implements Serializable {
    public String str;
    public double minScore;
    public double maxScore;
    public String by;

    public SearchFood(String str, String by) {
        this.str = str;
        this.by = by;
    }

    public SearchFood(double minScore, double maxScore, String by) {
        this.minScore = minScore;
        this.maxScore = maxScore;
        this.by = by;
    }

}
