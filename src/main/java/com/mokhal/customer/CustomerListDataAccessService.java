package com.mokhal.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao{

    private static final List<Customer> customers;

    static {
        customers = new ArrayList<>();

        Customer alex = new Customer(1, "Alex", "alex@gmail.com", 28);
        customers.add(alex);

        Customer jamila = new Customer(2, "Jamila", "jamila@gmail.com", 24);
        customers.add(jamila);
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        return customers.stream().filter(customer -> customer.getId().equals(id)).findFirst();
    }

    @Override
    public void insertCustomer(Customer customer){
        customers.add(customer);
    }

    @Override
    public boolean existsPersonWithEmail(String email){
        return customers.stream().anyMatch(customer -> customer.getEmail().equals(email));
    }

    @Override
    public void deleteCustomerById(Long id) {
        customers.remove(selectCustomerById(id));
    }

    @Override
    public void updateCustomer(Customer customer){

    }
}
