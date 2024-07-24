package com.paylink.paylink.models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class SavingsAccount extends Account{

    //The withdrawal limit from the savings
    private final DoubleProperty withdrawLimit;

    public SavingsAccount(String owner, String accountNumber, double balance, double withdrawLimit) {
        super(owner, accountNumber, balance);
        this.withdrawLimit = new SimpleDoubleProperty(this, "Withdraw Limit", withdrawLimit);
    }

    public DoubleProperty withdrawLimitProperty() {
        return withdrawLimit;
    }
}
