package app.com.example.android.UBaS;

/**
 * Created by MandipSilwal on 11/25/16.
 */

public class User {

    public String fullname;
    public String email;
    public String password;
    public String utaId;

    public String getUtaId() {
        return utaId;
    }

    public void setUtaId(String utaId) {
        this.utaId = utaId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User() {

    }

    public User(String name, String email, String password, String utaId) {
        this.fullname = name;
        this.email = email;
        this.password = password;
        this.utaId = utaId;
    }
}
