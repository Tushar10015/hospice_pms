package com.hospicebangladesh.pms.utils;

/**
 * Created by Tushar on 1/27/2018.
 */

public class SessionManager {

    private int userId;
    private String mobile;
    private String password;

    public SessionManager(int userId, String mobile, String password) {
        this.userId = userId;
        this.mobile = mobile;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
