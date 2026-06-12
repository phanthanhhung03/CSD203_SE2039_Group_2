package com.bths.entity;

public class TransactionNode {

    private Transaction data;
    public TransactionNode next;

    public TransactionNode(Transaction data) {
        this.data = data;
        this.next = null; 
    }

    public Transaction getData() {
        return data;
    }

    public TransactionNode getNext(){
        return next;
    }
    
    public void setData(Transaction data) {
        this.data = data;
    }
    
}
