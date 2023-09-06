package requests;

import java.io.Serializable;

public class Register implements Serializable {
    public String name;
    public String username;
    public String password;
    public String type;

    public Register() {
        name = "";
        username = "";
        password = "";
        type = "";
    }

    public Register(String name, String username, String password, String type) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.type = type;
    }
}
