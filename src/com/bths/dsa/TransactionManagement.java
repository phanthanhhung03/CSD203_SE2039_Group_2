package com.bths.dsa;

import com.bths.entity.Customer;
import com.bths.entity.Transaction;
import com.bths.entity.TransactionNode;
import java.util.Map;

public class TransactionManagement {

    private TransactionNode head;
    private TransactionNode tail;
    private int size;

    public TransactionManagement() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    // Kiểm tra danh sách rỗng 
    public boolean isEmpty() {
        return head == null;
    }

    // Thuật toán chèn cuối đạt O(1) 
    public void addLast(Transaction transaction) {
        TransactionNode newNode = new TransactionNode(transaction);

        if (isEmpty()) {
            // Trường hợp biên (Edge Case) xử lý khi danh sách rỗng 
            head = newNode;
            tail = newNode;
        } else {
            // Luồng xử lý thông thường: Nối đuôi cũ vào Node mới và dịch chuyển con trỏ tail 
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    // Hàm chuyển đổi dữ liệu sang mảng tĩnh (Dùng vòng lặp while) 
    public Transaction[] toArraySnapshot() {
        Transaction[] snapshotArray = new Transaction[size];
        TransactionNode current = head;
        int index = 0;

        while (current != null) {
            snapshotArray[index] = current.getData();
            index++;
            current = current.next; // Di chuyển tuần tự sang nút tiếp theo 
        }
        return snapshotArray;

    }

    public int getSize() {
        return size;
    }

    // Display Transaction
    public void displayTransaction() {
        TransactionNode current = head;

        while (current != null) {
            Transaction transaction = current.getData();
            System.out.println(transaction.toString());
            current = current.getNext();
        }
    }

    // Deleting a transaction if occuring error system 
    public boolean deleteFaultyTransaction(String transactionId) {
        if (isEmpty()) {
            return false;
        }

        TransactionNode curr = head;
        TransactionNode prev = null;
        while (curr != null) {
            if (curr.getData().getTransactionId().equalsIgnoreCase(transactionId)) {
                // Head
                if (curr == head) {
                    head = head.next;
                    // Empty list
                    if (head == null) {
                        tail = null;
                    }
                } // Middle
                else {
                    prev.next = curr.next;
                    // Tail
                    if (curr == tail) {
                        tail = prev;
                    }
                }
                size--;
                return true;
            }

            prev = curr;
            curr = curr.next;
        }
        return false;
    }

    // Filter Transaction by Transaction Id
    public void filterTransactionById(String id) {
        TransactionNode currentNode = head;

        while (currentNode != null) {
            Transaction transaction = currentNode.getData();
            if (transaction.getTransactionId().equalsIgnoreCase(id)) {
                System.out.println(transaction.toString());
                return;
            }
            currentNode = currentNode.getNext();
        }

        System.out.println("No transaction found with ID : " + id);

    }

    // Filter Transaction by Type
    public void filterTransactionByType(String type) {
        TransactionNode currentNode = head;
        boolean found = false;

        while (currentNode != null) {
            Transaction transaction = currentNode.getData();
            if (transaction.getType().equalsIgnoreCase(type)) {
                System.out.println(transaction.toString());
                found = true;
            }
            currentNode = currentNode.getNext();
        }

        if (!found) {
            System.out.println("No transaction found with type :" + type);
        }

    }

    // Filter Transaction by Account Number
    public void filterTransactionByAccountNum(String accountNum) {
        TransactionNode currentNode = head;
        boolean found = false;
        while (currentNode != null) {
            Transaction transaction = currentNode.getData();
            if (transaction.getAccountNumber().equalsIgnoreCase(accountNum)) {
                System.out.println(transaction.toString());
                found = true;
            }
            currentNode = currentNode.getNext();
        }

        if (!found) {
            System.out.println("No transaction has been done with this account ! ");
        }
    }

    public void displayReverseChronological() {
        if (isEmpty()) {
            System.out.println("Ledger is empty.");
            return;
        }

        // Chuyển chuỗi liên kết sang mảng tĩnh
        Transaction[] snapshot = toArraySnapshot();

        System.out.println("\n=== BANK LEDGER (REVERSE CHRONOLOGICAL ORDER) ===");
        // Duyệt ngược từ cuối mảng về đầu mảng
        for (int i = snapshot.length - 1; i >= 0; i--) {
            System.out.println(snapshot[i]);
        }
        System.out.println("=================================================");
    }

    // Computing Net Balance
    public void computeNetBalance(Map<String, Customer> customerMap) {
        TransactionNode currentNode = head;
        
        while (currentNode != null) {
            
            Transaction t = currentNode.getData();
            Customer customer = customerMap.get(t.getAccountNumber());

            // If Customer Account Number not found
            if (customer == null) {
                System.out.println("[ERROR] Account not found: " + t.getAccountNumber());
                currentNode = currentNode.getNext();
                continue;
            }

            if (t.getType().equalsIgnoreCase("DEPOSIT")) {
                customer.setBalance(customer.getBalance() + t.getAmount());

            } else if (t.getType().equalsIgnoreCase("WITHDRAWAL")) {

                // If customer account balance don't have enough money
                if (customer.getBalance() < t.getAmount()) {
                    System.out.println("[FAILED] Insufficient balance. " + "Transaction: " + t.getTransactionId());
                    currentNode = currentNode.getNext();
                    continue;
                }

                customer.setBalance(customer.getBalance() - t.getAmount());
            }

            currentNode = currentNode.getNext();
            
        }
        
    }

}
