package com.bankingsystem;

import com.bankingsystem.model.Account;
import com.bankingsystem.model.Transaction;
import com.bankingsystem.model.User;
import com.bankingsystem.repository.AccountRepository;
import com.bankingsystem.repository.TransactionRepository;
import com.bankingsystem.repository.UserRepository;
import com.bankingsystem.service.AccountService;
import com.bankingsystem.service.AuthenticationService;
import com.bankingsystem.service.TransactionService;
import com.bankingsystem.service.UserService;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main Banking System Application with CLI
 */
public class BankingSystem {
    private static final Scanner scanner = new Scanner(System.in);
    private static AuthenticationService authService;
    private static AccountService accountService;
    private static TransactionService transactionService;
    private static UserService userService;
    private static ExecutorService executorService;

    public static void main(String[] args) {
        // Initialize data directory
        initializeDataDirectory();

        // Initialize repositories
        UserRepository userRepository = new UserRepository();
        AccountRepository accountRepository = new AccountRepository();
        TransactionRepository transactionRepository = new TransactionRepository();

        // Initialize services
        authService = new AuthenticationService(userRepository);
        accountService = new AccountService(accountRepository);
        transactionService = new TransactionService(transactionRepository, accountRepository);
        userService = new UserService(userRepository);

        // Create thread pool for concurrent transactions
        executorService = Executors.newFixedThreadPool(10);

        // Create default admin if none exists
        createDefaultAdmin();

        // Start the CLI
        startCLI();

        // Shutdown thread pool when done
        executorService.shutdown();
    }

    private static void initializeDataDirectory() {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }

    private static void createDefaultAdmin() {
        if (userService.getUserByUsername("admin").isEmpty()) {
            authService.registerUser("admin", "admin123", "admin");
            System.out.println("Default admin user created:");
            System.out.println("Username: admin");
            System.out.println("Password: admin123");
        }
    }

    private static void startCLI() {
        boolean running = true;

        while (running) {
            if (!authService.isLoggedIn()) {
                printAuthMenu();
                int choice = getIntInput("Enter your choice: ");

                switch (choice) {
                    case 1:
                        login();
                        break;
                    case 2:
                        register();
                        break;
                    case 3:
                        running = false;
                        System.out.println("Exiting Banking System. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } else {
                if (authService.isAdmin()) {
                    printAdminMenu();
                } else {
                    printCustomerMenu();
                }

                int choice = getIntInput("Enter your choice: ");

                if (authService.isAdmin()) {
                    processAdminChoice(choice);
                } else {
                    processCustomerChoice(choice);
                }
            }
        }
    }

    private static void printAuthMenu() {
        System.out.println("\n==== Banking System ====");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
    }

    private static void printCustomerMenu() {
        User currentUser = authService.getCurrentUser();
        System.out.println("\n==== Banking System - Customer Menu ====");
        System.out.println("Welcome, " + currentUser.getUsername() + "!");
        System.out.println("1. View My Accounts");
        System.out.println("2. Create New Account");
        System.out.println("3. Deposit Money");
        System.out.println("4. Withdraw Money");
        System.out.println("5. Transfer Money");
        System.out.println("6. View Transaction History");
        System.out.println("7. Logout");
    }

    private static void printAdminMenu() {
        User currentUser = authService.getCurrentUser();
        System.out.println("\n==== Banking System - Admin Menu ====");
        System.out.println("Welcome, " + currentUser.getUsername() + " (Admin)!");
        System.out.println("1. View All Users");
        System.out.println("2. View All Accounts");
        System.out.println("3. View All Transactions");
        System.out.println("4. User Management");
        System.out.println("5. Logout");
    }

    private static void login() {
        System.out.println("\n==== Login ====");
        String username = getStringInput("Enter username: ");
        String password = getStringInput("Enter password: ");

        if (authService.login(username, password)) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }

    private static void register() {
        System.out.println("\n==== Register ====");
        String username = getStringInput("Enter username: ");
        String password = getStringInput("Enter password: ");

        if (authService.registerUser(username, password, "customer")) {
            System.out.println("Registration successful! You can now login.");
        } else {
            System.out.println("Username already exists. Please choose another username.");
        }
    }

    private static void processCustomerChoice(int choice) {
        switch (choice) {
            case 1:
                viewMyAccounts();
                break;
            case 2:
                createNewAccount();
                break;
            case 3:
                depositMoney();
                break;
            case 4:
                withdrawMoney();
                break;
            case 5:
                transferMoney();
                break;
            case 6:
                viewTransactionHistory();
                break;
            case 7:
                authService.logout();
                System.out.println("Logged out successfully.");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void processAdminChoice(int choice) {
        switch (choice) {
            case 1:
                viewAllUsers();
                break;
            case 2:
                viewAllAccounts();
                break;
            case 3:
                viewAllTransactions();
                break;
            case 4:
                userManagement();
                break;
            case 5:
                authService.logout();
                System.out.println("Logged out successfully.");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void viewMyAccounts() {
        System.out.println("\n==== My Accounts ====");
        User currentUser = authService.getCurrentUser();
        List<Account> userAccounts = accountService.getAccountsByUserId(currentUser.getId());

        if (userAccounts.isEmpty()) {
            System.out.println("You don't have any accounts yet.");
        } else {
            for (Account account : userAccounts) {
                System.out.println("Account ID: " + account.getId());
                System.out.println("Type: " + account.getAccountType());
                System.out.println("Balance: $" + String.format("%.2f", account.getBalance()));
                System.out.println("--------------------");
            }
        }
    }

    private static void createNewAccount() {
        System.out.println("\n==== Create New Account ====");
        User currentUser = authService.getCurrentUser();
        String accountType = getStringInput("Enter account type (checking/savings): ");
        double initialDeposit = getDoubleInput("Enter initial deposit amount: $");

        if (initialDeposit < 0) {
            System.out.println("Initial deposit cannot be negative.");
            return;
        }

        Account newAccount = accountService.createAccount(currentUser.getId(), accountType, initialDeposit);
        System.out.println("Account created successfully!");
        System.out.println("Account ID: " + newAccount.getId());
        System.out.println("Type: " + newAccount.getAccountType());
        System.out.println("Balance: $" + String.format("%.2f", newAccount.getBalance()));
    }

    private static void depositMoney() {
        System.out.println("\n==== Deposit Money ====");
        User currentUser = authService.getCurrentUser();
        List<Account> userAccounts = accountService.getAccountsByUserId(currentUser.getId());

        if (userAccounts.isEmpty()) {
            System.out.println("You don't have any accounts yet. Please create an account first.");
            return;
        }

        displayAccountList(userAccounts);
        String accountId = getStringInput("Enter the account ID to deposit to: ");

        Optional<Account> accountOpt = userAccounts.stream()
                .filter(a -> a.getId().equals(accountId))
                .findFirst();

        if (accountOpt.isPresent()) {
            double amount = getDoubleInput("Enter deposit amount: $");

            if (amount <= 0) {
                System.out.println("Deposit amount must be positive.");
                return;
            }

            // Use ExecutorService to handle the transaction
            executorService.submit(() -> {
                boolean success = transactionService.deposit(accountId, amount);
                if (success) {
                    System.out.println("Deposit of $" + String.format("%.2f", amount) + " completed successfully.");
                } else {
                    System.out.println("Deposit failed. Please try again.");
                }
            });
        } else {
            System.out.println("Invalid account ID.");
        }
    }

    private static void withdrawMoney() {
        System.out.println("\n==== Withdraw Money ====");
        User currentUser = authService.getCurrentUser();
        List<Account> userAccounts = accountService.getAccountsByUserId(currentUser.getId());

        if (userAccounts.isEmpty()) {
            System.out.println("You don't have any accounts yet. Please create an account first.");
            return;
        }

        displayAccountList(userAccounts);
        String accountId = getStringInput("Enter the account ID to withdraw from: ");

        Optional<Account> accountOpt = userAccounts.stream()
                .filter(a -> a.getId().equals(accountId))
                .findFirst();

        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            double amount = getDoubleInput("Enter withdrawal amount: $");

            if (amount <= 0) {
                System.out.println("Withdrawal amount must be positive.");
                return;
            }

            if (amount > account.getBalance()) {
                System.out.println("Insufficient funds. Your balance is $" +
                        String.format("%.2f", account.getBalance()));
                return;
            }

            // Use ExecutorService to handle the transaction
            executorService.submit(() -> {
                boolean success = transactionService.withdraw(accountId, amount);
                if (success) {
                    System.out.println("Withdrawal of $" + String.format("%.2f", amount) + " completed successfully.");
                } else {
                    System.out.println("Withdrawal failed. Please try again.");
                }
            });
        } else {
            System.out.println("Invalid account ID.");
        }
    }

    private static void transferMoney() {
        System.out.println("\n==== Transfer Money ====");
        User currentUser = authService.getCurrentUser();
        List<Account> userAccounts = accountService.getAccountsByUserId(currentUser.getId());

        if (userAccounts.isEmpty()) {
            System.out.println("You don't have any accounts yet. Please create an account first.");
            return;
        }

        System.out.println("Your accounts:");
        displayAccountList(userAccounts);

        String fromAccountId = getStringInput("Enter the account ID to transfer from: ");

        Optional<Account> fromAccountOpt = userAccounts.stream()
                .filter(a -> a.getId().equals(fromAccountId))
                .findFirst();

        if (fromAccountOpt.isEmpty()) {
            System.out.println("Invalid source account ID.");
            return;
        }

        Account fromAccount = fromAccountOpt.get();
        String toAccountId = getStringInput("Enter the account ID to transfer to: ");

        if (fromAccountId.equals(toAccountId)) {
            System.out.println("Cannot transfer to the same account.");
            return;
        }

        Optional<Account> toAccountOpt = accountService.getAccountById(toAccountId);

        if (toAccountOpt.isEmpty()) {
            System.out.println("Invalid destination account ID.");
            return;
        }

        double amount = getDoubleInput("Enter transfer amount: $");

        if (amount <= 0) {
            System.out.println("Transfer amount must be positive.");
            return;
        }

        if (amount > fromAccount.getBalance()) {
            System.out.println("Insufficient funds. Your balance is $" +
                    String.format("%.2f", fromAccount.getBalance()));
            return;
        }

        // Use ExecutorService to handle the transaction
        executorService.submit(() -> {
            boolean success = transactionService.transfer(fromAccountId, toAccountId, amount);
            if (success) {
                System.out.println("Transfer of $" + String.format("%.2f", amount) + " completed successfully.");
            } else {
                System.out.println("Transfer failed. Please try again.");
            }
        });
    }

    private static void viewTransactionHistory() {
        System.out.println("\n==== Transaction History ====");
        User currentUser = authService.getCurrentUser();
        List<Account> userAccounts = accountService.getAccountsByUserId(currentUser.getId());

        if (userAccounts.isEmpty()) {
            System.out.println("You don't have any accounts yet.");
            return;
        }

        displayAccountList(userAccounts);
        String accountId = getStringInput("Enter the account ID to view transactions: ");

        Optional<Account> accountOpt = userAccounts.stream()
                .filter(a -> a.getId().equals(accountId))
                .findFirst();

        if (accountOpt.isPresent()) {
            List<Transaction> transactions = transactionService.getTransactionHistory(accountId);

            if (transactions.isEmpty()) {
                System.out.println("No transactions found for this account.");
            } else {
                System.out.println("Transaction History:");
                for (Transaction tx : transactions) {
                    System.out.println("Transaction ID: " + tx.getId());
                    System.out.println("Type: " + tx.getType());
                    System.out.println("Amount: $" + String.format("%.2f", tx.getAmount()));
                    System.out.println("Date: " + tx.getTimestamp());

                    if ("transfer".equals(tx.getType())) {
                        if (accountId.equals(tx.getFromAccountId())) {
                            System.out.println("To Account: " + tx.getToAccountId());
                        } else {
                            System.out.println("From Account: " + tx.getFromAccountId());
                        }
                    }

                    System.out.println("Status: " + tx.getStatus());
                    System.out.println("--------------------");
                }
            }
        } else {
            System.out.println("Invalid account ID.");
        }
    }

    private static void viewAllUsers() {
        System.out.println("\n==== All Users ====");
        List<User> allUsers = userService.getAllUsers();

        if (allUsers.isEmpty()) {
            System.out.println("No users found.");
        } else {
            for (User user : allUsers) {
                System.out.println("User ID: " + user.getId());
                System.out.println("Username: " + user.getUsername());
                System.out.println("Role: " + user.getRole());

                // Get user's accounts
                List<Account> userAccounts = accountService.getAccountsByUserId(user.getId());
                System.out.println("Number of Accounts: " + userAccounts.size());

                double totalBalance = userAccounts.stream()
                        .mapToDouble(Account::getBalance)
                        .sum();

                System.out.println("Total Balance: $" + String.format("%.2f", totalBalance));
                System.out.println("--------------------");
            }
        }
    }

    private static void viewAllAccounts() {
        System.out.println("\n==== All Accounts ====");
        List<Account> allAccounts = accountService.getAllAccounts();

        if (allAccounts.isEmpty()) {
            System.out.println("No accounts found.");
        } else {
            for (Account account : allAccounts) {
                System.out.println("Account ID: " + account.getId());

                Optional<User> userOpt = userService.getUserById(account.getUserId());
                userOpt.ifPresent(user -> System.out.println("Owner: " + user.getUsername()));

                System.out.println("Type: " + account.getAccountType());
                System.out.println("Balance: $" + String.format("%.2f", account.getBalance()));
                System.out.println("--------------------");
            }
        }
    }

    private static void viewAllTransactions() {
        System.out.println("\n==== All Transactions ====");
        List<Transaction> allTransactions = transactionService.getAllTransactions();

        if (allTransactions.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (Transaction tx : allTransactions) {
                System.out.println("Transaction ID: " + tx.getId());
                System.out.println("Type: " + tx.getType());
                System.out.println("Amount: $" + String.format("%.2f", tx.getAmount()));
                System.out.println("Date: " + tx.getTimestamp());

                if (tx.getFromAccountId() != null) {
                    System.out.println("From Account: " + tx.getFromAccountId());
                    Optional<Account> fromAccountOpt = accountService.getAccountById(tx.getFromAccountId());
                    fromAccountOpt.ifPresent(account -> {
                        Optional<User> userOpt = userService.getUserById(account.getUserId());
                        userOpt.ifPresent(user -> System.out.println("From User: " + user.getUsername()));
                    });
                }

                if (tx.getToAccountId() != null) {
                    System.out.println("To Account: " + tx.getToAccountId());
                    Optional<Account> toAccountOpt = accountService.getAccountById(tx.getToAccountId());
                    toAccountOpt.ifPresent(account -> {
                        Optional<User> userOpt = userService.getUserById(account.getUserId());
                        userOpt.ifPresent(user -> System.out.println("To User: " + user.getUsername()));
                    });
                }

                System.out.println("Status: " + tx.getStatus());
                System.out.println("--------------------");
            }
        }
    }

    private static void userManagement() {
        System.out.println("\n==== User Management ====");
        System.out.println("1. Create Admin User");
        System.out.println("2. Back to Admin Menu");

        int choice = getIntInput("Enter your choice: ");

        if (choice == 1) {
            createAdminUser();
        }
    }

    private static void createAdminUser() {
        System.out.println("\n==== Create Admin User ====");
        String username = getStringInput("Enter username: ");
        String password = getStringInput("Enter password: ");

        if (authService.registerUser(username, password, "admin")) {
            System.out.println("Admin user created successfully!");
        } else {
            System.out.println("Username already exists. Please choose another username.");
        }
    }

    private static void displayAccountList(List<Account> accounts) {
        System.out.println("Available accounts:");
        for (Account account : accounts) {
            System.out.println("ID: " + account.getId() +
                    " | Type: " + account.getAccountType() +
                    " | Balance: $" + String.format("%.2f", account.getBalance()));
        }
    }

    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
}