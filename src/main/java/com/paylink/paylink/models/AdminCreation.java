package com.paylink.paylink.models;

public class AdminCreation {
    public AdminCreation(String username, String password) {
        Model.getInstance().getDatabaseDriver().createNewAdmin(username, password);
    }
}
