/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bths.entity;

/**
 *
 * @author Admin
 */
public class Customer {
    private String cusId;
    private String fullName;
    private String phoneNumber;
    private String accountNumber;
    private double balance;

    public Customer() {
    }

    public Customer(String cusId, String fullName, String phoneNumber, String accountNumber, double balance) {
        this.cusId = cusId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
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

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Customer{" + "cusId=" + cusId + ", fullName=" + fullName + ", phoneNumber=" + phoneNumber + ", accountNumber=" + accountNumber + ", Balance=" + balance + '}';
    }
    
}
