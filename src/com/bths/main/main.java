package com.bths.main;

import com.bths.dsa.TransactionManagement;
import com.bths.entity.Transaction;


public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TransactionManagement ledger = new TransactionManagement();

        System.out.println("=== STAGE 1: REAL-TIME TRANSACTION INGESTION ===");
        // Giả lập luồng nạp dữ liệu liên tục đổ về SLL
        ledger.addLast(new Transaction("TX001", "102345", 500000.0, "DEPOSIT", "2026-06-12 10:00:00"));
        ledger.addLast(new Transaction("TX002", "987654", 200000.0, "WITHDRAWAL", "2026-06-12 10:15:00"));
        ledger.addLast(new Transaction("TX003", "102345", 1500000.0, "DEPOSIT", "2026-06-12 11:30:00"));
        ledger.addLast(new Transaction("TX004", "111222", 350000.0, "DEPOSIT", "2026-06-12 12:00:00"));
        
        System.out.println("Current ledger size: " + ledger.getSize());

        // Test tính năng lọc theo số tài khoản
        //ledger.filterByAccountNumber("102345");

        //System.out.println("\n=== STAGE 2: DATA ERROR CORRECTION ===");
        // Giả lập phát hiện giao dịch TX002 bị lỗi hệ thống và tiến hành ngắt kết nối vật lý
        System.out.println("Deleting faulty transaction TX002...");
        boolean isDeleted = ledger.deleteFaultyTransaction("TX002");
        System.out.println("Deletion status: " + (isDeleted ? "SUCCESS" : "FAILED"));
        System.out.println("Ledger size after deletion: " + ledger.getSize());

        System.out.println("\n=== STAGE 3: END-OF-DAY ANALYTICS SNAPSHOT ===");
        // Đóng băng bộ con trỏ phân tán sang một khối ô nhớ liên tiếp trên mảng tĩnh
        Transaction[] daySnapshot = ledger.toArraySnapshot();
        
        // Tính toán các chỉ số thống kê tài chính tận dụng cơ chế tối ưu CPU Cache Locality của mảng
        double totalVolume = 0;
        int depositCount = 0;
        int withdrawalCount = 0;

        System.out.println("\n--- PRINTING FROZEN STATIC ARRAY RECORDS ---");
        for (Transaction tx : daySnapshot) {
            System.out.println(tx);
            totalVolume += tx.getAmount();
            if (tx.getType().equals("DEPOSIT")) depositCount++;
            else if (tx.getType().equals("WITHDRAWAL")) withdrawalCount++;
        }

        System.out.println("\n--- FINAL FINANCIAL AUDIT REPORT ---");
        System.out.printf("Total Transaction Volume Evaluated: %,.2f VND\n", totalVolume);
        System.out.println("Total Successful Deposits: " + depositCount);
        System.out.println("Total Successful Withdrawals: " + withdrawalCount);
    }

}
