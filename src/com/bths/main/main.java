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

        // 1. Nạp dữ liệu từ file khi khởi động
        com.bths.util.FileService.loadCustomers(customerManagement, "customers.txt");
        com.bths.util.FileService.loadTransactions(transactionManagement, "transactions.txt");

        // 2. Nếu danh sách trống thì mới khởi tạo dữ liệu mẫu (chỉ chạy lần đầu)
        if (customerManagement.getCustomers().isEmpty()) {
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

            // Lưu dữ liệu mẫu vào file để các lần chạy sau không bị trống
            com.bths.util.FileService.saveCustomers(customerManagement, "customers.txt");
        }

        Scanner sc = new Scanner(System.in);
        int choice;
        do {

            System.out.println(
                    "\n===== BANKING SYSTEM ====="
            );

            System.out.println("1. Staff");
            System.out.println("2. Customer");
            System.out.println("0. Exit");

            Integer input = readIntOrCancel(sc, "Your choice: ");
            choice = (input == null) ? -1 : input;

            switch (choice) {

                case 1:
                    staffMenu(sc, staffService, transactionManagement);
                    break;

                case 2:
                    customerMenu(sc, customerService, bankingService);
                    break;
            }

        } while (choice != 0);

        //  Sao lưu lại danh sách khách hàng trước khi thoát chương trình
        com.bths.util.FileService.saveCustomers(customerManagement, "customers.txt");
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
            System.out.println("0. Back (or type Q)");

            Integer input = readIntOrCancel(sc, "Your choice: ");

            if (input == null) {
                // Q -> thoát menu này, quay lại menu trước đó
                return;
            }

            choice = input;

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
                    String txId = readLineOrCancel(sc, "Enter transaction ID (or Q to cancel): ");

                    if (txId == null) {
                        System.out.println("Cancelled.");
                        break;
                    }

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

                    Integer typeChoice
                            = readIntOrCancel(sc, "Your choice (or Q to cancel): ");

                    if (typeChoice == null) {
                        System.out.println("Cancelled.");
                        break;
                    }

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
                    } else {
                        System.out.println("Invalid type choice.");
                    }

                    break;

                case 6:

                    String account = readLineOrCancel(sc, "Enter account number (or Q to cancel): ");

                    if (account == null) {
                        System.out.println("Cancelled.");
                        break;
                    }

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

                    String faultyId = readLineOrCancel(sc, "Enter transaction ID (or Q to cancel): ");

                    if (faultyId == null) {
                        System.out.println("Cancelled.");
                        break;
                    }

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

        String accountNumber = readLineOrCancel(sc, "Enter Account Number (or Q to cancel): ");

        if (accountNumber == null) {
            System.out.println("Cancelled.");
            return;
        }

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
                    "7. View My Transactions (Newest)"
            );

            System.out.println(
                    "0. Back (or type Q)"
            );

            Integer input = readIntOrCancel(sc, "Your choice: ");

            if (input == null) {
                // Q -> thoát menu này, quay lại menu trước đó
                return;
            }

            choice = input;

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

                    Double depositAmount
                            = readDoubleOrCancel(sc, "Enter amount (or Q to cancel): ");

                    if (depositAmount == null) {
                        System.out.println("Cancelled.");
                        break;
                    }

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

                    Double withdrawAmount
                            = readDoubleOrCancel(sc, "Enter amount (or Q to cancel): ");

                    if (withdrawAmount == null) {
                        System.out.println("Cancelled.");
                        break;
                    }

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

                    String receiver = readLineOrCancel(sc, "Enter receiver account (or Q to cancel): ");

                    if (receiver == null) {
                        System.out.println("Cancelled.");
                        break;
                    }

                    Double transferAmount
                            = readDoubleOrCancel(sc, "Enter amount (or Q to cancel): ");

                    if (transferAmount == null) {
                        System.out.println("Cancelled.");
                        break;
                    }

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

                case 7:

                    customerService
                            .viewMyTransactionsReversed(
                                    accountNumber
                            );

                    break;
            }

        } while (choice != 0);
    }

    // Đọc số nguyên an toàn. Gõ "Q" để hủy thao tác / quay lại menu trước đó.
    // Trả về null nếu người dùng gõ Q -> nơi gọi phải tự xử lý trường hợp null.
    private static Integer readIntOrCancel(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("Q")) {
                return null;
            }

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number, or Q to cancel.");
            }
        }
    }

    // Đọc số thực an toàn. Gõ "Q" để hủy thao tác / quay lại menu trước đó.
    private static Double readDoubleOrCancel(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("Q")) {
                return null;
            }

            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number, or Q to cancel.");
            }
        }
    }

    // Đọc chuỗi. Gõ "Q" để hủy thao tác / quay lại menu trước đó (trả về null).
    private static String readLineOrCancel(Scanner sc, String prompt) {
        System.out.print(prompt);
        String input = sc.nextLine().trim();

        if (input.equalsIgnoreCase("Q")) {
            return null;
        }

        return input;
    }
}
