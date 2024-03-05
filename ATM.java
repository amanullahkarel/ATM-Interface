/*
Name : KAREL AMANULLAH ASHRAF

Title : ATM Interface

Description:

We have all come across ATMs in our cities and it is built on Java.
This complex project consists of five different classes and is a console-based application.
When the system starts the user is prompted with user id and user pin. On entering the 
details successfully, then ATM functionalities are unlocked. The project allows to perform 
following operations: 

1. Transaction history
2. Withdraw
3. Deposit
4. Transfer
5. Quit*/



package techno;

import java.util.*;

class Transaction {
    private Date date;
    private String description;
    private double amount;

    public Transaction(String description, double amount) {
        this.date = new Date();
        this.description = description;
        this.amount = amount;
    }

    public String toString() {
        return String.format("%s: %s %.2f", date.toString(), description, amount);
    }
}

class Account {
    private double balance;
    private List<Transaction> transactions;

    public Account() {
        this.balance = 50000;
        this.transactions = new ArrayList<>();
    }

    public void deposit(double amount) {
        balance += amount;
        transactions.add(new Transaction("Deposit", amount));
    }

    public void withdraw(double amount) {
        if (amount > balance) {
            System.out.println("Insufficient funds");
            return;
        }
        balance -= amount;
        transactions.add(new Transaction("Withdrawal", -amount));
    }

    public void transfer(Account recipient, double amount) {
        if (amount > balance) {
            System.out.println("Insufficient funds");
            return;
        }
        balance -= amount;
        recipient.balance += amount;
        transactions.add(new Transaction("Transfer to " + recipient.hashCode(), -amount));
        recipient.transactions.add(new Transaction("Transfer from " + hashCode(), amount));
    }

    public void printTransactions() {
        transactions.forEach(System.out::println);
    }

    public double getBalance() {
        return balance;
    }
}

class User {
    private String userId;
    private String pin;
    private Account account;

    public User(String userId, String pin) {
        this.userId = userId;
        this.pin = pin;
        this.account = new Account();
    }

    public String getUserId() {
        return userId;
    }

    public String getPin() {
        return pin;
    }

    public Account getAccount() {
        return account;
    }
}

public class ATM {
    private static Map<String, User> users;
    private static Scanner scanner;

    static {
        users = new HashMap<>();
        users.put("12345", new User("12345", "67890"));
        users.put("24680", new User("24680", "13579"));
        scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        System.out.println("Welcome to the ATM" + "\n");
        boolean loggedIn = false;
        int attempts = 0;
        User user = null;

        while (!loggedIn && attempts < 2) {
            System.out.print("Enter user ID: ");
            String userId = scanner.nextLine();
            System.out.print("Enter PIN: ");
            String pin = scanner.nextLine();

            user = users.get(userId);
            if (user != null && user.getPin().equals(pin)) {
                loggedIn = true;
            } else {
                attempts++;
                if (attempts < 2) {
                    System.out.println("Invalid user ID or PIN. Please try again." + "\n");
                } else {
                    System.out.println("Invalid user ID or PIN. Maximum attempts reached. Transaction terminated.");
                    return;
                }
            }
        }

        Account account = user.getAccount();

        while (true) {
            System.out.println("\n1. Transaction history");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Quit");
            System.out.print("Select an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("\nTransaction history:");
                    account.printTransactions();
                    break;
                case 2:
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    account.withdraw(withdrawAmount);
                    break;
                case 3:
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    account.deposit(depositAmount);
                    break;
                case 4:
                    System.out.print("Enter recipient's user ID: ");
                    String recipientId = scanner.nextLine();
                    User recipient = users.get(recipientId);
                    if (recipient == null) {
                        System.out.println("Recipient not found");
                        break;
                    }
                    System.out.print("Enter amount to transfer: ");
                    double transferAmount = scanner.nextDouble();
                    account.transfer(recipient.getAccount(), transferAmount);
                    break;
                case 5:
                    System.out.println("Thank you for using the ATM");
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}