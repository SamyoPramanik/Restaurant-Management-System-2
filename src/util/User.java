package util;

public class User implements java.io.Serializable {
    String username;
    String password;
    String type;
    int id;

    public User(String username, String password, String type, int id) {
        this.username = username;
        this.password = password;
        this.type = type;
        this.id = id;
    }

    public User() {
        this.username = "";
        this.password = "";
        this.type = "";
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }
}
