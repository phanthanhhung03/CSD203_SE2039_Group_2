package com.bths.entity;

public class Transaction {

    private String transactionId;
    private String accountNumber;
    private double amount;
    private String type; // "DEPOSIT" hoặc "WITHDRAWAL"
    private String timestamp;

    // Constructor khởi tạo thông tin giao dịch
    public Transaction(String transactionId, String accountNumber, double amount, String type, String timestamp) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.type = type;
        this.timestamp = timestamp;
    }

    // Các hàm Getter để hỗ trợ tìm kiếm và hiển thị dữ liệu sau này
    public final String getTransactionId() {
        return transactionId;
    }

    public final String getAccountNumber() {
        return accountNumber;
    }

    public final double getAmount() {
        return amount;
    }

    public final String getType() {
        return type;
    }

    public final String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("ID: %s | Account: %s | Amount: %,.2f | Type: %s | Time: %s",
                transactionId, accountNumber, amount, type, timestamp);
    }
}
