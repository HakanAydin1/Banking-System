package com.bankingsystem.service;

import com.bankingsystem.model.Account;
import com.bankingsystem.repository.AccountRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service for account management
 */
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // Create a new account for a user
    public Account createAccount(String userId, String accountType, double initialBalance) {
        Account account = new Account(userId, initialBalance, accountType);
        accountRepository.addAccount(account);
        return account;
    }

    // Get an account by ID
    public Optional<Account> getAccountById(String accountId) {
        return accountRepository.findById(accountId);
    }

    // Get all accounts for a user
    public List<Account> getAccountsByUserId(String userId) {
        return accountRepository.findByUserId(userId);
    }

    // Update an account's balance (synchronized for thread safety)
    public synchronized boolean updateBalance(String accountId, double newBalance) {
        Optional<Account> accountOpt = accountRepository.findById(accountId);

        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            account.setBalance(newBalance);
            accountRepository.updateAccount(account);
            return true;
        }

        return false;
    }

    // Get all accounts (admin function)
    public List<Account> getAllAccounts() {
        return accountRepository.getAllAccounts();
    }
}