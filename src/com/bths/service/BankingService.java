package com.bths.service;

import com.bths.entity.Customer;
import com.bths.entity.Transaction;
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

    private String getCurrentTimestamp() {
        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return LocalDateTime.now().format(formatter);
    }

    private void recordTransaction(String fromAccount, String toAccount, double amount, TransactionType type) {

        Transaction transaction = new Transaction(
                generateTransactionId(),
                fromAccount,
                toAccount,
                amount,
                type,
                getCurrentTimestamp()
        );

        transactionManagement.addLast(transaction);
    }

    public boolean transfer(String fromAccount, String toAccount, double amount) {

        Customer sender = customerManagement.findCustomer(fromAccount);
        Customer receiver = customerManagement.findCustomer(toAccount);

        // kiểm tra tài khoản tồn tại
        if (sender == null || receiver == null) {
            return false;
        }

        if (fromAccount.equalsIgnoreCase(toAccount)) {
            return false;
        }

        if (amount <= 0) {
            return false;
        }

        if (!sender.withdraw(amount)) {
            return false;
        }

        receiver.deposit(amount);

        // ghi lịch sử giao dịch
        recordTransaction(
                fromAccount,
                toAccount,
                amount,
                TransactionType.TRANSFER
        );

        return true;
    }

    public boolean withdraw(String accountNumber, double amount) {
        Customer account = customerManagement.findCustomer(accountNumber);

        if (account == null) {
            return false;
        }

        if (!account.withdraw(amount)) {
            return false;
        }

        recordTransaction(accountNumber, null, amount, TransactionType.WITHDRAWAL);
        return true;
    }

    public boolean deposit(String accountNumber, double amount) {
        Customer account = customerManagement.findCustomer(accountNumber);

        if (account == null) {
            return false;
        }

        if (amount <= 0) {
            return false;
        }

        account.deposit(amount);
        recordTransaction(null, accountNumber, amount, TransactionType.DEPOSIT);
        return true;
    }
}
