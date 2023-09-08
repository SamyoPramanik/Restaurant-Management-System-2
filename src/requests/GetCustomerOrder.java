package requests;

import java.io.Serializable;

public class GetCustomerOrder implements Serializable {
    public int customerId;

    public GetCustomerOrder(int customerId) {
        this.customerId = customerId;
    }
}
