package com.bankingsystem.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.bankingsystem.model.Account;
import com.bankingsystem.util.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * Repository for Account data operations
 */
public class AccountRepository {
    private static final String ACCOUNTS_FILE = "data/accounts.json";
    private final List<Account> accounts;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public AccountRepository() {
        // Load accounts from file
        accounts = JsonUtils.readListFromFile(ACCOUNTS_FILE, new TypeReference<List<Account>>() {
        });
    }

    // Save all accounts to file
    public void saveAccounts() {
        lock.writeLock().lock();
        try {
            JsonUtils.writeListToFile(accounts, ACCOUNTS_FILE);
        } finally {
            lock.writeLock().unlock();
        }
    }

    // Add a new account
    public void addAccount(Account account) {
        lock.writeLock().lock();
        try {
            accounts.add(account);
            saveAccounts();
        } finally {
            lock.writeLock().unlock();
        }
    }

    // Find an account by ID
    public Optional<Account> findById(String id) {
        lock.readLock().lock();
        try {
            return accounts.stream()
                    .filter(account -> account.getId().equals(id))
                    .findFirst();
        } finally {
            lock.readLock().unlock();
        }
    }

    // Find all accounts by user ID
    public List<Account> findByUserId(String userId) {
        lock.readLock().lock();
        try {
            return accounts.stream()
                    .filter(account -> account.getUserId().equals(userId))
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    // Update an account
    public void updateAccount(Account updatedAccount) {
        lock.writeLock().lock();
        try {
            for (int i = 0; i < accounts.size(); i++) {
                if (accounts.get(i).getId().equals(updatedAccount.getId())) {
                    accounts.set(i, updatedAccount);
                    break;
                }
            }
            saveAccounts();
        } finally {
            lock.writeLock().unlock();
        }
    }

    // Get all accounts
    public List<Account> getAllAccounts() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(accounts);
        } finally {
            lock.readLock().unlock();
        }
    }
}