package util;

public class Admin implements java.io.Serializable {
    private String name;

    public Admin(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
