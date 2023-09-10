package util;

public class RestaurantInfo implements java.io.Serializable {
    public int resId;
    public String resName;
    public double score;
    public String zipcode;
    public String category1;
    public String category2;
    public String category3;
    public String username;
    public String password;

    public RestaurantInfo(int resId, String resName, double score, String zipcode, String category1, String category2,
            String category3) {
        this.resId = resId;
        this.resName = resName;
        this.score = score;
        this.zipcode = zipcode;
        this.category1 = category1;
        this.category2 = category2;
        this.category3 = category3;
    }

    public RestaurantInfo(int resId, String resName, double score, String zipcode, String category1, String category2,
            String category3, String username, String password) {
        this.resId = resId;
        this.resName = resName;
        this.score = score;
        this.zipcode = zipcode;
        this.category1 = category1;
        this.category2 = category2;
        this.category3 = category3;
        this.username = username;
        this.password = password;
    }

}
