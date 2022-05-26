package me.alin.passtore;

import androidx.annotation.NonNull;

public class AccountModel {

    private Integer _id;
    public String service;
    public String password;
    public String username;

    @NonNull
    @Override
    public String toString() {
        return "AccountModel{" +
                "service='" + service + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    public AccountModel() {
        service = "";
        password = "";
        username = "";
    }

    public AccountModel(String service, String username, String password) {
        this.service = service;
        this.username = username;
        this.password = password;

    }

    public void setId(Integer id) {
        this._id = id;
    }

    public Integer getId() {
        return this._id;
    }
}
