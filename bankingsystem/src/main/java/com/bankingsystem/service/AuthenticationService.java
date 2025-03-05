package com.bankingsystem.service;

import com.bankingsystem.model.User;
import com.bankingsystem.repository.UserRepository;
import com.bankingsystem.util.PasswordUtils;

import java.util.Optional;

/**
 * Service for user authentication and authorization
 */
public class AuthenticationService {
    private final UserRepository userRepository;
    private User currentUser;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Register a new user
    public boolean registerUser(String username, String password, String role) {
        // Check if user already exists
        if (userRepository.findByUsername(username).isPresent()) {
            return false;
        }

        // Generate salt and hash password
        String salt = PasswordUtils.generateSalt();
        String passwordHash = PasswordUtils.hashPassword(password, salt);

        // Create and save new user
        User newUser = new User(username, passwordHash, role);
        userRepository.addUser(newUser, salt);

        return true;
    }

    // Authenticate a user
    public boolean login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Optional<String> saltOpt = userRepository.getSaltByUserId(user.getId());

            if (saltOpt.isPresent()) {
                String salt = saltOpt.get();
                boolean verified = PasswordUtils.verifyPassword(password, user.getPasswordHash(), salt);

                if (verified) {
                    this.currentUser = user;
                    return true;
                }
            }
        }

        return false;
    }

    // Logout the current user
    public void logout() {
        this.currentUser = null;
    }

    // Get the current logged-in user
    public User getCurrentUser() {
        return currentUser;
    }

    // Check if a user is logged in
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    // Check if the current user is an admin
    public boolean isAdmin() {
        return isLoggedIn() && "admin".equals(currentUser.getRole());
    }
}