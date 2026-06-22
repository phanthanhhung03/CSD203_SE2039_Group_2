package com.bths.main;

import com.bths.entity.Customer;
import com.bths.entity.Transaction;
import com.bths.entity.TransactionStatus;
import com.bths.entity.TransactionType;
import com.bths.service.BankingService;
import com.bths.service.CustomerManagement;
import com.bths.service.CustomerService;
import com.bths.service.StaffService;
import com.bths.service.TransactionManagement;
import java.util.Scanner;

public class main {

    public static void main(String[] args) {

        CustomerManagement customerManagement = new CustomerManagement();

        TransactionManagement transactionManagement = new TransactionManagement();

        BankingService bankingService = new BankingService(customerManagement, transactionManagement);

        StaffService staffService = new StaffService(transactionManagement);

        CustomerService customerService = new CustomerService(customerManagement, transactionManagement);

        Customer c1 = new Customer("Nguyen Van A", "0901234567", 10000);
        Customer c2 = new Customer("Tran Thi B", "0902345678", 8000);
        Customer c3 = new Customer("Le Van C", "0903456789", 5000);

        customerManagement.addCustomer(c1);
        customerManagement.addCustomer(c2);
        customerManagement.addCustomer(c3);

        bankingService.deposit("BK001", 2000, true);
        bankingService.withdraw("BK002", 1000, true);
        bankingService.transfer("BK001", "BK003", 1500, true);

        bankingService.transfer("BK001", "BK002", 500, false);
        bankingService.deposit("BK003", 1000, false);
        bankingService.withdraw("BK001", 300, false);

        Scanner sc = new Scanner(System.in);
        int choice;
        do {

            System.out.println(
                    "\n===== BANKING SYSTEM ====="
            );

            System.out.println("1. Staff");
            System.out.println("2. Customer");
            System.out.println("0. Exit");

            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {

                case 1:
                    staffMenu(sc, staffService, transactionManagement);
                    break;

                case 2:
                    customerMenu(sc, customerService, bankingService);
                    break;
            }

        } while (choice != 0);
    }

    public static void staffMenu(Scanner sc, StaffService staffService, TransactionManagement transactionManagement) {

        int choice;

        do {
            System.out.println("\n========== STAFF MENU ==========");
            System.out.println("1. View All Transactions");
            System.out.println("2. View Completed Transactions");
            System.out.println("3. View Faulty Transactions");
            System.out.println("4. Search Transaction By ID");
            System.out.println("5. Filter Transaction By Type");
            System.out.println("6. Filter Transaction By Account");
            System.out.println("7. Count Completed Transactions");
            System.out.println("8. Count Faulty Transactions");
            System.out.println("9. Mark Transaction As Faulty");
            System.out.println("10. View Transactions in Reverse Order ");
            System.out.println("0. Back");

            System.out.print("Your choice: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {

                case 1:
                    staffService.displayAllTransaction();
                    break;

                case 2:
                    staffService.displayTransactionByStatus(
                            TransactionStatus.COMPLETED);
                    break;

                case 3:
                    staffService.displayTransactionByStatus(
                            TransactionStatus.FAULTY);
                    break;

                case 4:
                    System.out.print("Enter transaction ID: ");
                    String txId = sc.nextLine();

                    Transaction tx
                            = transactionManagement.findTransaction(txId);

                    if (tx == null) {
                        System.out.println("Transaction not found!");
                    } else {
                        staffService.printHeaderTable();
                        System.out.println(tx);
                    }
                    break;

                case 5:

                    System.out.println("1. Deposit");
                    System.out.println("2. Withdrawal");
                    System.out.println("3. Transfer");

                    int typeChoice
                            = Integer.parseInt(sc.nextLine());

                    TransactionType type = null;

                    switch (typeChoice) {
                        case 1:
                            type = TransactionType.DEPOSIT;
                            break;
                        case 2:
                            type = TransactionType.WITHDRAWAL;
                            break;
                        case 3:
                            type = TransactionType.TRANSFER;
                            break;
                    }

                    if (type != null) {
                        staffService.filterTransactionByType(type);
                    }

                    break;

                case 6:

                    System.out.print("Enter account number: ");
                    String account = sc.nextLine();

                    staffService.filterTransactionByAccountNum(account);

                    break;

                case 7:

                    System.out.println(
                            "Completed Transactions: "
                            + staffService.countTransactionByStatus(
                                    TransactionStatus.COMPLETED)
                    );

                    break;

                case 8:

                    System.out.println(
                            "Faulty Transactions: "
                            + staffService.countTransactionByStatus(
                                    TransactionStatus.FAULTY)
                    );

                    break;

                case 9:

                    System.out.print(
                            "Enter transaction ID: "
                    );

                    String faultyId = sc.nextLine();

                    if (staffService.markTransactionAsFaulty(
                            faultyId)) {

                        System.out.println(
                                "Transaction marked as faulty."
                        );

                    } else {

                        System.out.println(
                                "Cannot mark transaction."
                        );
                    }

                    break;
                case 10:
                    staffService.displayTransactionsInReverseOrder();
                    break;
            }

        } while (choice != 0);
    }

    public static void customerMenu(Scanner sc, CustomerService customerService, BankingService bankingService) {

        System.out.print(
                "Enter Account Number: "
        );

        String accountNumber
                = sc.nextLine();

        if (customerService.viewProfile(
                accountNumber) == null) {

            System.out.println(
                    "Account not found!"
            );

            return;
        }

        int choice;

        do {

            System.out.println(
                    "\n========== CUSTOMER MENU =========="
            );

            System.out.println(
                    "1. View Profile"
            );

            System.out.println(
                    "2. Check Balance"
            );

            System.out.println(
                    "3. Deposit"
            );

            System.out.println(
                    "4. Withdraw"
            );

            System.out.println(
                    "5. Transfer"
            );

            System.out.println(
                    "6. View My Transactions"
            );

            System.out.println(
                    "0. Back"
            );

            System.out.print(
                    "Your choice: "
            );

            choice
                    = Integer.parseInt(sc.nextLine());

            switch (choice) {

                case 1:

                    System.out.println(
                            customerService.viewProfile(
                                    accountNumber)
                    );

                    break;

                case 2:

                    System.out.println(
                            "Current Balance: "
                            + customerService.checkBalance(
                                    accountNumber)
                    );

                    break;

                case 3:

                    System.out.print(
                            "Enter amount: "
                    );

                    double depositAmount
                            = Double.parseDouble(
                                    sc.nextLine());

                    boolean depositResult = bankingService.deposit(
                            accountNumber,
                            depositAmount,
                            true
                    );

                    if (depositResult) {
                        System.out.println("Deposit successfully!");
                    } else {
                        System.out.println("Deposit failed! Amount must be greater than 0.");
                    }

                    break;

                case 4:

                    System.out.print(
                            "Enter amount: "
                    );

                    double withdrawAmount
                            = Double.parseDouble(
                                    sc.nextLine());

                    boolean withdrawResult
                            = bankingService.withdraw(
                                    accountNumber,
                                    withdrawAmount,
                                    true
                            );

                    if (withdrawResult) {
                        System.out.println("Withdraw successfully!");
                    } else {
                        System.out.println("Withdraw failed! Insufficient balance or invalid amount.");
                    }

                    break;

                case 5:

                    System.out.print("Enter receiver account: ");
                    String receiver = sc.nextLine();

                    System.out.print("Enter amount: ");
                    double transferAmount
                            = Double.parseDouble(sc.nextLine());

                    String result = bankingService.transfer(
                            accountNumber,
                            receiver,
                            transferAmount,
                            true
                    );

                    System.out.println(result);

                    break;

                case 6:

                    customerService
                            .viewMyTransactions(
                                    accountNumber
                            );

                    break;
            }

        } while (choice != 0);
    }

}
