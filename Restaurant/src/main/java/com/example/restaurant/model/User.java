package com.example.restaurant.model;

public class User {
    private String username;
    private String password;
    private String usr_nama;
    private int usr_role;
    private int usr_status;

    public User(String username, String password, String usr_nama, int usr_role, int usr_status) {
        this.username = username;
        this.password = password;
        this.usr_nama = usr_nama;
        this.usr_role = usr_role;
        this.usr_status = usr_status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsr_nama() {
        return usr_nama;
    }

    public void setUsr_nama(String usr_nama) {
        this.usr_nama = usr_nama;
    }

    public int getUsr_role() {
        return usr_role;
    }

    public void setUsr_role(int usr_role) {
        this.usr_role = usr_role;
    }

    public int getUsr_status() {
        return usr_status;
    }

    public void setUsr_status(int usr_status) {
        this.usr_status = usr_status;
    }
}
