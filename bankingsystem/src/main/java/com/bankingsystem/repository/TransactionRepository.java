package com.bankingsystem.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.bankingsystem.model.Transaction;
import com.bankingsystem.util.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * Repository for Transaction data operations
 */
public class TransactionRepository {
    private static final String TRANSACTIONS_FILE = "data/transactions.json";
    private final List<Transaction> transactions;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public TransactionRepository() {
        // Load transactions from file
        transactions = JsonUtils.readListFromFile(TRANSACTIONS_FILE, new TypeReference<List<Transaction>>() {
        });
    }

    // Save all transactions to file
    public void saveTransactions() {
        lock.writeLock().lock();
        try {
            JsonUtils.writeListToFile(transactions, TRANSACTIONS_FILE);
        } finally {
            lock.writeLock().unlock();
        }
    }

    // Add a new transaction
    public void addTransaction(Transaction transaction) {
        lock.writeLock().lock();
        try {
            transactions.add(transaction);
            saveTransactions();
        } finally {
            lock.writeLock().unlock();
        }
    }

    // Find a transaction by ID
    public Optional<Transaction> findById(String id) {
        lock.readLock().lock();
        try {
            return transactions.stream()
                    .filter(transaction -> transaction.getId().equals(id))
                    .findFirst();
        } finally {
            lock.readLock().unlock();
        }
    }

    // Find all transactions for an account (either as sender or receiver)
    public List<Transaction> findByAccountId(String accountId) {
        lock.readLock().lock();
        try {
            return transactions.stream()
                    .filter(t -> accountId.equals(t.getFromAccountId()) || accountId.equals(t.getToAccountId()))
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    // Update a transaction
    public void updateTransaction(Transaction updatedTransaction) {
        lock.writeLock().lock();
        try {
            for (int i = 0; i < transactions.size(); i++) {
                if (transactions.get(i).getId().equals(updatedTransaction.getId())) {
                    transactions.set(i, updatedTransaction);
                    break;
                }
            }
            saveTransactions();
        } finally {
            lock.writeLock().unlock();
        }
    }

    // Get all transactions
    public List<Transaction> getAllTransactions() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(transactions);
        } finally {
            lock.readLock().unlock();
        }
    }
}