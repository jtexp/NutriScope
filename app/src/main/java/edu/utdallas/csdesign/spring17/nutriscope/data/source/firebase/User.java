package edu.utdallas.csdesign.spring17.nutriscope.data.source.firebase;

/**
 * Created by john on 3/18/17.
 */

public class User {

    private String uid;
    private String email;

    public User() {

    }

    public User(String uid, String email) {
        this.uid = uid;
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
