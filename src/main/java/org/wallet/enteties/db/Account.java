package org.wallet.enteties.db;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import org.wallet.helpers.DateTimeHelper;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Account {
    @Id
    private String accountId;
    private double balance;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
    private List<Transaction> transactions;

    public Account(final String accountId, final double amount) {
        this.accountId = accountId;
        transactions = new ArrayList<>();
        if (amount > 0.0) {
            deposit("deposit", amount);
        }
    }

    public Account() {
    }

    public void withdraw(final String receiverId, final double amount) {
        balance -= amount;
        String timestamp = DateTimeHelper.createTimeStamp();
        Transaction transaction = new Transaction(accountId, receiverId, amount, timestamp);
        transaction.setAccount(this);
        transactions.add(transaction);
    }

    public void deposit(final String senderId, final double amount) {
        balance += amount;
        String timestamp = DateTimeHelper.createTimeStamp();
        Transaction transaction = new Transaction(senderId, accountId, amount, timestamp);
        transaction.setAccount(this);
        transactions.add(transaction);
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public boolean checkBalance(final double amount) {
        return balance < amount;
    }
}
