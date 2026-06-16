package com.bths.main;

import com.bths.entity.Customer;
import com.bths.service.BankingService;
import com.bths.service.CustomerManagement;
import com.bths.service.TransactionManagement;

public class main {

    public static void main(String[] args) {

        CustomerManagement customerManagement = new CustomerManagement();
        TransactionManagement transactionManagement = new TransactionManagement();

        BankingService bankingService
                = new BankingService(
                        customerManagement,
                        transactionManagement);

        // Create customers
        Customer c1 = new Customer("Nguyen Van A", "0901111111");
        Customer c2 = new Customer("Tran Thi B", "0902222222");
        Customer c3 = new Customer("Le Van C", "0903333333");

        customerManagement.addCustomer(c1);
        customerManagement.addCustomer(c2);
        customerManagement.addCustomer(c3);

        // Deposit
        bankingService.deposit(c1.getAccountNumber(), 10000);

        bankingService.deposit(c2.getAccountNumber(), 5000);

        // Withdraw
        bankingService.withdraw(c1.getAccountNumber(), 2000);

        // Transfer
        bankingService.transfer(c1.getAccountNumber(), c2.getAccountNumber(), 3000);

        // Display customers
        System.out.println("\n=== CUSTOMERS ===");
        customerManagement.displayAllCustomers();

        // Display transactions
        System.out.println("\n=== TRANSACTIONS ===");
        transactionManagement.displayTransaction();
        System.out.println("");
        transactionManagement.displayReverseChronological();

    }
}
