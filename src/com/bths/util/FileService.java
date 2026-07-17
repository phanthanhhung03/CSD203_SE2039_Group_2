package com.bths.util;

import com.bths.entity.Customer;
import com.bths.entity.Transaction;
import com.bths.entity.TransactionStatus;
import com.bths.entity.TransactionType;
import com.bths.service.BankingService;
import com.bths.service.CustomerManagement;
import com.bths.service.TransactionManagement;
import java.io.*;
import java.util.Map;

public class FileService {

    /**
     * Ghi danh sách khách hàng ra file txt
     */
    public static void saveCustomers(CustomerManagement customerManagement, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            Map<String, Customer> customers = customerManagement.getCustomers();
            for (Customer c : customers.values()) {
                writer.write(c.getFullName() + "," + c.getPhoneNumber() + "," + c.getAccountNumber() + "," + c.getBalance());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi lưu danh sách khách hàng: " + e.getMessage());
        }
    }

    /**
     * Đọc danh sách khách hàng từ file txt
     */
    public static void loadCustomers(CustomerManagement customerManagement, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    Customer c = new Customer(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]));
                    customerManagement.addCustomer(c);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Lỗi khi đọc danh sách khách hàng: " + e.getMessage());
        }
    }

    /**
     * Ghi thêm một giao dịch mới vào cuối file (lưu theo thời gian thực)
     */
    public static void appendTransaction(Transaction t, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(t.getTransactionId() + "," + t.getFromAccount() + "," + t.getToAccount() + "," + 
                         t.getAmount() + "," + t.getType() + "," + t.getTimestamp() + "," + t.getStatus());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Lỗi khi ghi thêm giao dịch: " + e.getMessage());
        }
    }

    /**
     * Ghi toàn bộ danh sách giao dịch ra file txt
     */
    public static void saveAllTransactions(TransactionManagement transactionManagement, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            com.bths.dsa.TransactionNode curr = transactionManagement.getHead();
            while (curr != null) {
                Transaction t = curr.getData();
                writer.write(t.getTransactionId() + "," + t.getFromAccount() + "," + t.getToAccount() + "," + 
                             t.getAmount() + "," + t.getType() + "," + t.getTimestamp() + "," + t.getStatus());
                writer.newLine();
                curr = curr.getNext();
            }
        } catch (IOException e) {
            System.out.println("Lỗi khi lưu danh sách giao dịch: " + e.getMessage());
        }
    }

    /**
     * Đọc danh sách giao dịch từ file txt
     */
    public static void loadTransactions(TransactionManagement transactionManagement, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 7) {
                    Transaction t = new Transaction(parts[0], 
                            parts[1].equals("null") ? null : parts[1], 
                            parts[2].equals("null") ? null : parts[2], 
                            Double.parseDouble(parts[3]), 
                            TransactionType.valueOf(parts[4]), 
                            parts[5], 
                            TransactionStatus.valueOf(parts[6]));
                    transactionManagement.addLast(t);
                    
                    // Cập nhật nextTransactionId trong BankingService
                    try {
                        int id = Integer.parseInt(parts[0].replace("TX", ""));
                        BankingService.updateNextTransactionId(id);
                    } catch (NumberFormatException ex) {
                        // Bỏ qua nếu parse không thành công
                    }
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Lỗi khi đọc danh sách giao dịch: " + e.getMessage());
        }
    }
}
