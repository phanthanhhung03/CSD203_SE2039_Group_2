package com.bths.service;

import com.bths.dsa.TransactionNode;
import com.bths.entity.Transaction;

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
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    public int getSize() {
        return size;
    }

    public TransactionNode getHead() {
        return head;
    }

    // Find transaction by id
    public Transaction findTransaction(String transactionId) {
        TransactionNode curr = head;
        while (curr != null) {
            if (curr.getData().getTransactionId().equalsIgnoreCase(transactionId)) {
                return curr.getData();
            }
            curr = curr.next;
        }
        return null;
    }

    
    // Reverse transaction
    public void traverseBackward(){
        TransactionNode currentNode = tail;
        while(currentNode != null){
            System.out.println(currentNode.getData().toString());
            currentNode = currentNode.getPrev();
        }
    }
    // Computing Net Balance
}
