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
    public String getTransactionId() { return transactionId; }
    public String getAccountNumber() { return accountNumber; }
    public double getAmount() { return amount; }
    public String getType() { return type; }
    public String getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format("| %-10s | %-12s | %-10.2f | %-10s | %-20s |", 
                transactionId, accountNumber, amount, type, timestamp);
    }
}
