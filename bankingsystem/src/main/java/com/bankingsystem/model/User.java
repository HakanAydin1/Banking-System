package com.bankingsystem.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * User class representing system users
 */
public class User {
    private String id;
    private String username;
    private String passwordHash;
    private String role; // "admin" or "customer"

    public User() {
        this.id = UUID.randomUUID().toString();
    }

    public User(String username, String passwordHash, String role) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}