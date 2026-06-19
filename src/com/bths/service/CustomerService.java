package com.bths.service;

import com.bths.dsa.TransactionNode;
import com.bths.entity.Customer;
import com.bths.entity.Transaction;
import com.bths.entity.TransactionStatus;

public class CustomerService {

    private CustomerManagement customerManagement;
    private TransactionManagement transactionManagement;

    public CustomerService(CustomerManagement customerManagement,
            TransactionManagement transactionManagement) {

        this.customerManagement = customerManagement;
        this.transactionManagement = transactionManagement;
    }

    public Customer viewProfile(String accountNumber) {
        return customerManagement.findCustomer(accountNumber);
    }

    public double checkBalance(String accountNumber) {

        Customer customer = customerManagement.findCustomer(accountNumber);

        if (customer == null) {
            return -1;
        }

        return customer.getBalance();
    }

    public void viewMyTransactions(String accountNumber) {

        TransactionNode current = transactionManagement.getHead();

        boolean found = false;

        while (current != null) {

            Transaction transaction = current.getData();

            if ((transaction.getFromAccount() != null
                    && transaction.getFromAccount().equalsIgnoreCase(accountNumber))
                    || (transaction.getToAccount() != null
                    && transaction.getToAccount().equalsIgnoreCase(accountNumber))
                    && transaction.getStatus() == TransactionStatus.COMPLETED) {

                System.out.println(transaction);
                found = true;
            }

            current = current.getNext();
        }

        if (!found) {
            System.out.println(
                    "No transaction found."
            );
        }
    }

    public int countMyTransactions(String accountNumber) {

        TransactionNode current = transactionManagement.getHead();
        int count = 0;

        while (current != null) {

            Transaction transaction  = current.getData();
            if ((accountNumber.equalsIgnoreCase(transaction.getFromAccount()))
                    || (accountNumber.equalsIgnoreCase(transaction.getToAccount()))) {

                count++;
            }
            current = current.getNext();
        }

        return count;
    }

}
