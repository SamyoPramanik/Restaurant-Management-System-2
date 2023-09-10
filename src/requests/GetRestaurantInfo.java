package requests;

import java.io.Serializable;

public class GetRestaurantInfo implements Serializable {
    public int resId;

    public GetRestaurantInfo(int resId) {
        this.resId = resId;
    }

}
