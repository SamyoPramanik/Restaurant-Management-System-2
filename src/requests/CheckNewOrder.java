package requests;

import java.io.Serializable;

public class CheckNewOrder implements Serializable {
    public int resId;

    public CheckNewOrder(int resId) {
        this.resId = resId;
    }
}
