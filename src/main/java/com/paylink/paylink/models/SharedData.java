package com.paylink.paylink.models;

public class SharedData {
    private static SharedData instance = new SharedData();
    private String payee;

    private SharedData(){

    }
    public static SharedData getInstance(){
        return instance;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }
}
