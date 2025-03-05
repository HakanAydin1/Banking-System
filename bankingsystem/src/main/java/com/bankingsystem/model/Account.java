package com.bankingsystem.model;

import java.util.UUID;

/**
 * Account class representing user bank accounts
 */
public class Account {
    private String id;
    private String userId;
    private double balance;
    private String accountType; // "checking", "savings", etc.

    public Account() {
        this.id = UUID.randomUUID().toString();
    }

    public Account(String userId, double balance, String accountType) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.balance = balance;
        this.accountType = accountType;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", balance=" + balance +
                ", accountType='" + accountType + '\'' +
                '}';
    }
}