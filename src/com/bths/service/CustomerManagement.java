package com.bths.service;

import com.bths.entity.Customer;
import java.util.HashMap;

public class CustomerManagement {
    
    private HashMap<String ,Customer> customers;

    public CustomerManagement() {
        this.customers = new HashMap<>();
    }
    
    public void addCustomer(Customer customer) {
        customers.put(customer.getAccountNumber(), customer);
    }
    
    public Customer findCustomer(String accountNumber) {
        return customers.get(accountNumber);
    }
    
    public void removeCustomer(String accountNumber) {
        customers.remove(accountNumber);
    }
    
    public void displayAllCustomers() {
        for (Customer c : customers.values()) {
            System.out.println(c.toString());
        }
    }
    
}
