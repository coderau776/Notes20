package android.example.notes20.Data;

import java.io.Serializable;

public class UserInfo implements Serializable
{
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    String username="",userEmail="",mob="";
    UserInfo()
    {

    }
    public UserInfo(String name, String email, String mob)
    {
        username=name;
        userEmail=email;
        this.mob=mob;
    }
}
