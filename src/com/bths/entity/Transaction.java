package com.bths.entity;

public class Transaction {

    private String transactionId;
    private String fromAccount;
    private String toAccount;
    private double amount;
    private TransactionType type;
    private String timestamp; // yyyy-MM-dd HH:mm:ss

    public Transaction(String transactionId,
            String fromAccount,
            String toAccount,
            double amount,
            TransactionType type,
            String timestamp) {

        this.transactionId = transactionId;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.type = type;
        this.timestamp = timestamp;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public double getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format(
                "%-8s | %-8s | %-8s | %12.2f | %-12s | %-19s",
                transactionId,
                fromAccount == null ? "-" : fromAccount,
                toAccount == null ? "-" : toAccount,
                amount,
                type,
                timestamp
        );
    }

    public void tableHeader() {
        
    }
}
