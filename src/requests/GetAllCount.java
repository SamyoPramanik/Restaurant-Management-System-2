package requests;

public class GetAllCount implements java.io.Serializable {
    public int resCount;
    public int cusCount;
    public int foodCount;

    public GetAllCount() {
    }

    public GetAllCount(int resCount, int cusCount, int foodCount) {
        this.resCount = resCount;
        this.cusCount = cusCount;
        this.foodCount = foodCount;
    }
}
