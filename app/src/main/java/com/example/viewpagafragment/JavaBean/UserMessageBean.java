package com.example.viewpagafragment.JavaBean;

public class UserMessageBean {
    private int id;
    private String account;
    private String passwod;

    public UserMessageBean() {
    }

    public UserMessageBean(int id, String account, String passwod) {
        this.id = id;
        this.account = account;
        this.passwod = passwod;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPasswod() {
        return passwod;
    }

    public void setPasswod(String passwod) {
        this.passwod = passwod;
    }
}
