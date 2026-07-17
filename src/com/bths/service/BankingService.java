package com.bths.service;

import com.bths.entity.Customer;
import com.bths.entity.Transaction;
import com.bths.entity.TransactionStatus;
import com.bths.entity.TransactionType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BankingService {

    private CustomerManagement customerManagement;
    private TransactionManagement transactionManagement;
    private static int nextTransactionId = 1;

    public BankingService(CustomerManagement customerManagement,
            TransactionManagement transactionManagement) {
        this.customerManagement = customerManagement;
        this.transactionManagement = transactionManagement;
    }

    private String generateTransactionId() {
        return String.format("TX%03d", nextTransactionId++);
    }

    public static void updateNextTransactionId(int id) {
        if (id >= nextTransactionId) {
            nextTransactionId = id + 1;
        }
    }

    private String getCurrentTimestamp() {
        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return LocalDateTime.now().format(formatter);
    }

    private Transaction recordTransaction(String fromAccount, String toAccount, double amount, TransactionType type, TransactionStatus status) {
        Transaction transaction = new Transaction(
                generateTransactionId(),
                fromAccount,
                toAccount,
                amount,
                type,
                getCurrentTimestamp(),
                status
        );

        transactionManagement.addLast(transaction);
        
        // Lưu giao dịch theo thời gian thực
        com.bths.util.FileService.appendTransaction(transaction, "transactions.txt");
        // Cập nhật file danh sách khách hàng vì số dư có thể thay đổi
        com.bths.util.FileService.saveCustomers(customerManagement, "customers.txt");
        
        return transaction;
    }

    private void recordFaultyTransaction(String fromAccount, String toAccount, double amount, TransactionType type) {
        Transaction transaction = recordTransaction(
                fromAccount,
                toAccount,
                amount,
                type,
                TransactionStatus.FAULTY
        );

        System.out.println(
                "System error found at transaction ID: "
                + transaction.getTransactionId()
        );
    }

    public String transfer(String fromAccount, String toAccount, double amount, boolean systemHealthy) {

        Customer sender = customerManagement.findCustomer(fromAccount);
        Customer receiver = customerManagement.findCustomer(toAccount);

        // kiểm tra tài khoản tồn tại
        if (sender == null || receiver == null) {
            return "Account not found!";
        }

        if (fromAccount.equalsIgnoreCase(toAccount)) {
            return "Cannot transfer to the same account!";
        }

        if (amount <= 0) {
            return "Amount must be greater than 0!";
        }

        // Simulate system failure
        if (!systemHealthy) {
            recordFaultyTransaction(fromAccount, toAccount, amount, TransactionType.TRANSFER);
            return "System error occurred!";
        }

        if (!sender.withdraw(amount)) {
            return "Insufficient balance!";
        }

        receiver.deposit(amount);

        // ghi lịch sử giao dịch
        recordTransaction(
                fromAccount,
                toAccount,
                amount,
                TransactionType.TRANSFER,
                TransactionStatus.COMPLETED
        );

        return "Transfer successfully!";
    }

    public boolean withdraw(String accountNumber, double amount, boolean systemHealthy) {
        Customer account = customerManagement.findCustomer(accountNumber);

        if (account == null) {
            return false;
        }

        if (!systemHealthy) {
            recordFaultyTransaction(accountNumber, null, amount, TransactionType.WITHDRAWAL);
            return false;
        }

        if (!account.withdraw(amount)) {
            return false;
        }

        recordTransaction(accountNumber, null, amount, TransactionType.WITHDRAWAL, TransactionStatus.COMPLETED);
        return true;
    }

    public boolean deposit(String accountNumber, double amount, boolean systemHealthy) {
        Customer account = customerManagement.findCustomer(accountNumber);

        if (account == null) {
            return false;
        }

        if (amount <= 0) {
            return false;
        }

        if (!systemHealthy) {
            recordFaultyTransaction(null, accountNumber, amount, TransactionType.DEPOSIT);
            return false;
        }

        account.deposit(amount);
        recordTransaction(null, accountNumber, amount, TransactionType.DEPOSIT, TransactionStatus.COMPLETED);
        return true;
    }
}
