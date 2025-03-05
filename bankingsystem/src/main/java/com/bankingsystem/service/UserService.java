package com.bankingsystem.service;

import com.bankingsystem.model.User;
import com.bankingsystem.repository.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service for user management
 */
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Get a user by ID
    public Optional<User> getUserById(String userId) {
        return userRepository.findById(userId);
    }

    // Get a user by username
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Get all users (admin function)
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }
}