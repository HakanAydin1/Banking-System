package com.bankingsystem.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Transaction class representing money transfers
 */
public class Transaction {
    private String id;
    private String fromAccountId;
    private String toAccountId;
    private double amount;
    private String type; // "deposit", "withdrawal", "transfer"
    private LocalDateTime timestamp;
    private String status; // "pending", "completed", "failed"

    public Transaction() {
        this.id = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.status = "pending";
    }

    public Transaction(String fromAccountId, String toAccountId, double amount, String type) {
        this.id = UUID.randomUUID().toString();
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.type = type;
        this.timestamp = LocalDateTime.now();
        this.status = "pending";
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", fromAccountId='" + fromAccountId + '\'' +
                ", toAccountId='" + toAccountId + '\'' +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", timestamp=" + timestamp +
                ", status='" + status + '\'' +
                '}';
    }
}