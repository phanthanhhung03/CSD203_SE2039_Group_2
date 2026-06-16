package com.bths.entity;

public class Customer {

    private static int nextId = 1;

    private String fullName;
    private String phoneNumber;
    private String accountNumber;
    private double balance;

    public Customer() {
    }

    // Tự sinh accountNumber, balance mặc định = 0
    public Customer(String fullName, String phoneNumber) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.accountNumber = String.format("BK%03d", nextId++);
        this.balance = 0;
    }

    // Tự sinh accountNumber, cho phép nhập balance
    public Customer(String fullName, String phoneNumber, double balance) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.accountNumber = String.format("BK%03d", nextId++);
        this.balance = balance;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    // Thường không nên cho sửa accountNumber
    // public void setAccountNumber(String accountNumber) {
    //     this.accountNumber = accountNumber;
    // }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                '}';
    }
}