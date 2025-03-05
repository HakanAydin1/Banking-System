package com.bankingsystem.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.bankingsystem.model.User;
import com.bankingsystem.util.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Repository for User data operations
 */
public class UserRepository {
    private static final String USERS_FILE = "data/users.json";
    private static final String SALT_FILE = "data/user_salts.json";
    private final List<User> users;
    private final List<UserSalt> userSalts;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    
    public UserRepository() {
        // Load users from file
        users = JsonUtils.readListFromFile(USERS_FILE, new TypeReference<List<User>>() {});
        
        // Load salts from file
        userSalts = JsonUtils.readListFromFile(SALT_FILE, new TypeReference<List<UserSalt>>() {});
    }
    
    // Save all users to file
    public void saveUsers() {
        lock.writeLock().lock();
        try {
            JsonUtils.writeListToFile(users, USERS_FILE);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    // Save all salt data to file
    public void saveSalts() {
        lock.writeLock().lock();
        try {
            JsonUtils.writeListToFile(userSalts, SALT_FILE);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    // Add a new user
    public void addUser(User user, String salt) {
        lock.writeLock().lock();
        try {
            users.add(user);
            userSalts.add(new UserSalt(user.getId(), salt));
            saveUsers();
            saveSalts();
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    // Find a user by username
    public Optional<User> findByUsername(String username) {
        lock.readLock().lock();
        try {
            return users.stream()
                    .filter(user -> user.getUsername().equals(username))
                    .findFirst();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    // Find a user by ID
    public Optional<User> findById(String id) {
        lock.readLock().lock();
        try {
            return users.stream()
                    .filter(user -> user.getId().equals(id))
                    .findFirst();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    // Get all users
    public List<User> getAllUsers() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(users);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    // Get salt for a user
    public Optional<String> getSaltByUserId(String userId) {
        lock.readLock().lock();
        try {
            return userSalts.stream()
                    .filter(userSalt -> userSalt.getUserId().equals(userId))
                    .map(UserSalt::getSalt)
                    .findFirst();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    // User Salt inner class for storing salt data
    private static class UserSalt {
        private String userId;
        private String salt;
        
        public UserSalt() {
        }
        
        public UserSalt(String userId, String salt) {
            this.userId = userId;
            this.salt = salt;
        }
        
        public String getUserId() {
            return userId;
        }
        
        public void setUserId(String userId) {
            this.userId = userId;
        }
        
        public String getSalt() {
            return salt;
        }
        
        public void setSalt(String salt) {
            this.salt = salt;
        }
    }
}