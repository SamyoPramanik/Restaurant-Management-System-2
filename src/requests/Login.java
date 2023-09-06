package requests;

import java.io.Serializable;

public class Login implements Serializable {
    private String username;
    private String password;
    private String response;

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
        response = "";
    }

    public Login() {
        username = "";
        password = "";
        response = "";
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getResponse() {
        return response;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
