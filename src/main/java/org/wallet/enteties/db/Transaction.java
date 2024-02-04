package org.wallet.enteties.db;

import jakarta.persistence.*;
import org.wallet.enteties.TransactionSummary;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String senderId;
    private String receiverId;
    private double amount;
    private String timestamp;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public Transaction() {

    }

    public Transaction(final String senderId, final String receiverId, final double amount, final String timestamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public TransactionSummary toTransactionSummary(String accountId) {
        if (getSenderId().equals("deposit")) {
            return new TransactionSummary(getTimestamp(), ": deposit. Amount:", getAmount());
        } else if (getReceiverId().equals("withdraw")) {
            return new TransactionSummary(getTimestamp(), ": withdraw. Amount: ", getAmount());
        } else if (getSenderId().equals(accountId)) {
            return new TransactionSummary(getTimestamp(), String.format(": transferred to account: %s. Amount:", getReceiverId()), getAmount());
        } else {
            return new TransactionSummary(getTimestamp(), String.format(": got a transfer from account: %s. Amount:", getSenderId()), getAmount());
        }
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public double getAmount() {
        return amount;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setAccount(final Account account) {
        this.account = account;
    }
}
