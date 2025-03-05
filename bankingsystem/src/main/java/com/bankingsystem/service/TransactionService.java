package com.bankingsystem.service;

import com.bankingsystem.model.Account;
import com.bankingsystem.model.Transaction;
import com.bankingsystem.repository.AccountRepository;
import com.bankingsystem.repository.TransactionRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Service for handling banking transactions
 */
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final Lock transactionLock = new ReentrantLock();

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    // Deposit money into an account
    public boolean deposit(String accountId, double amount) {
        if (amount <= 0) {
            return false;
        }

        transactionLock.lock();
        try {
            Optional<Account> accountOpt = accountRepository.findById(accountId);

            if (accountOpt.isPresent()) {
                Account account = accountOpt.get();
                double newBalance = account.getBalance() + amount;
                account.setBalance(newBalance);
                accountRepository.updateAccount(account);

                // Record transaction
                Transaction transaction = new Transaction(null, accountId, amount, "deposit");
                transaction.setStatus("completed");
                transactionRepository.addTransaction(transaction);

                return true;
            }

            return false;
        } finally {
            transactionLock.unlock();
        }
    }

    // Withdraw money from an account
    public boolean withdraw(String accountId, double amount) {
        if (amount <= 0) {
            return false;
        }

        transactionLock.lock();
        try {
            Optional<Account> accountOpt = accountRepository.findById(accountId);

            if (accountOpt.isPresent()) {
                Account account = accountOpt.get();

                // Check if account has sufficient funds
                if (account.getBalance() < amount) {
                    return false;
                }

                double newBalance = account.getBalance() - amount;
                account.setBalance(newBalance);
                accountRepository.updateAccount(account);

                // Record transaction
                Transaction transaction = new Transaction(accountId, null, amount, "withdrawal");
                transaction.setStatus("completed");
                transactionRepository.addTransaction(transaction);

                return true;
            }

            return false;
        } finally {
            transactionLock.unlock();
        }
    }

    // Transfer money between accounts
    public boolean transfer(String fromAccountId, String toAccountId, double amount) {
        if (amount <= 0 || fromAccountId.equals(toAccountId)) {
            return false;
        }

        transactionLock.lock();
        try {
            Optional<Account> fromAccountOpt = accountRepository.findById(fromAccountId);
            Optional<Account> toAccountOpt = accountRepository.findById(toAccountId);

            if (fromAccountOpt.isPresent() && toAccountOpt.isPresent()) {
                Account fromAccount = fromAccountOpt.get();
                Account toAccount = toAccountOpt.get();

                // Check if sender has sufficient funds
                if (fromAccount.getBalance() < amount) {
                    return false;
                }

                // Update balances
                fromAccount.setBalance(fromAccount.getBalance() - amount);
                toAccount.setBalance(toAccount.getBalance() + amount);

                accountRepository.updateAccount(fromAccount);
                accountRepository.updateAccount(toAccount);

                // Record transaction
                Transaction transaction = new Transaction(fromAccountId, toAccountId, amount, "transfer");
                transaction.setStatus("completed");
                transactionRepository.addTransaction(transaction);

                return true;
            }

            return false;
        } finally {
            transactionLock.unlock();
        }
    }

    // Get transaction history for an account
    public List<Transaction> getTransactionHistory(String accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    // Get all transactions (admin function)
    public List<Transaction> getAllTransactions() {
        return transactionRepository.getAllTransactions();
    }

    // Get a specific transaction
    public Optional<Transaction> getTransactionById(String transactionId) {
        return transactionRepository.findById(transactionId);
    }
}