package requests;

import java.io.Serializable;

public class SearchRestaurant implements Serializable {
    public String str;

    public String by;

    public SearchRestaurant(String str, String by) {
        this.str = str;
        this.by = by;
    }
}
