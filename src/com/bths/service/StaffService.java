package com.bths.service;

import com.bths.dsa.TransactionNode;
import com.bths.entity.Transaction;
import com.bths.entity.TransactionStatus;
import com.bths.entity.TransactionType;

public class StaffService {

    private TransactionManagement transactionManagement;

    public StaffService(TransactionManagement transactionManagement) {
        this.transactionManagement = transactionManagement;
    }

    // Display Transaction
    public void displayAllTransaction() {
        if (transactionManagement.isEmpty()) {
            return;
        }
        printHeaderTable();
        TransactionNode current = transactionManagement.getHead();

        while (current != null) {
            Transaction transaction = current.getData();
            System.out.println(transaction.toString());
            current = current.getNext();
        }
    }

    // Display COMPLETE Transaction
    public void displayTransactionByStatus(TransactionStatus status) {
        if (transactionManagement.isEmpty()) {
            return;
        }
        printHeaderTable();
        TransactionNode current = transactionManagement.getHead();

        while (current != null) {
            if (current.getData().getStatus() == status) {
                System.out.println(current.getData().toString());
            }
            current = current.getNext();
        }
    }

    // Deleting a transaction if occuring error system 
    public boolean markTransactionAsFaulty(String transactionId) {
        if (transactionManagement.isEmpty()) {
            return false;
        }

        Transaction transaction = transactionManagement.findTransaction(transactionId);

        if (transaction == null) {
            return false;
        }

        if (transaction.getStatus() == TransactionStatus.FAULTY) {
            return false;
        }

        transaction.setStatus(TransactionStatus.FAULTY);
        return true;
    }

    // Count faulty transaction
    public int countTransactionByStatus(TransactionStatus status) {
        TransactionNode curr = transactionManagement.getHead();
        int result = 0;
        while (curr != null) {
            if (curr.getData().getStatus() == status) {
                result += 1;
            }
            curr = curr.getNext();
        }
        return result;
    }

    // Filter Transaction by Type
    public void filterTransactionByType(TransactionType type) {
        TransactionNode currentNode = transactionManagement.getHead();
        boolean found = false;
        printHeaderTable();
        while (currentNode != null) {
            Transaction transaction = currentNode.getData();

            if (transaction.getType() == type) {
                System.out.println(transaction);
                found = true;
            }

            currentNode = currentNode.getNext();
        }

        if (!found) {
            System.out.println("No transaction found with type: " + type);
        }
    }

    // Filter Transaction by Account Number
    public void filterTransactionByAccountNum(String accountNum) {
        TransactionNode currentNode = transactionManagement.getHead();
        boolean found = false;
        printHeaderTable();
        while (currentNode != null) {

            Transaction transaction = currentNode.getData();

            if ((transaction.getFromAccount() != null
                    && transaction.getFromAccount().equalsIgnoreCase(accountNum))
                    || (transaction.getToAccount() != null
                    && transaction.getToAccount().equalsIgnoreCase(accountNum))) {

                System.out.println(transaction);
                found = true;
            }

            currentNode = currentNode.getNext();
        }

        if (!found) {
            System.out.println("No transaction has been done with this account!");
        }
    }

    public void printHeaderTable() {
        System.out.println(
                "------------------------------------------------------------------------------------------------------"
        );

        System.out.printf(
                "%-8s | %-8s | %-8s | %12s | %-12s | %-10s | %-19s%n",
                "ID",
                "FROM",
                "TO",
                "AMOUNT",
                "TYPE",
                "STATUS",
                "TIMESTAMP"
        );

        System.out.println(
                "------------------------------------------------------------------------------------------------------"
        );
    }
}
