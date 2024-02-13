package com.mokhal.customer;

import com.mokhal.exception.DuplicateResourceException;
import com.mokhal.exception.RequestValidationException;
import com.mokhal.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao){
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers(){
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomerById(Long id){
        return customerDao.selectCustomerById(id).orElseThrow(() -> new ResourceNotFoundException("Customer %s".formatted(id)));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest){
        //check if email exist
        if (customerDao.existsPersonWithEmail(customerRegistrationRequest.email())){
            throw new DuplicateResourceException("Email is already taken");
        }

        customerDao.insertCustomer(new Customer(customerRegistrationRequest.name(), customerRegistrationRequest.email(),
                customerRegistrationRequest.age()));
    }

    public void deleteCustomer(Long id) {
        if (customerDao.selectCustomerById(id).isPresent()){
            customerDao.deleteCustomerById(id);
        }else {
            throw new ResourceNotFoundException("Customer with id [%s] is not found".formatted(id));
        }
    }

    public void updateCustomer(Long id, CustomerUpdateRequest customerUpdateRequest) {
        Customer customer = getCustomerById(id);

        boolean changes = false;

        if (customerUpdateRequest.name() != null && !customerUpdateRequest.name().equals(customer.getName())) {
            customer.setName(customerUpdateRequest.name());
            changes = true;
        }

        if (customerUpdateRequest.email() != null && !customerUpdateRequest.email().equals(customer.getEmail())) {
            if (customerDao.existsPersonWithEmail(customerUpdateRequest.email())){
                throw new DuplicateResourceException("Email already taken");
            }

            customer.setEmail(customerUpdateRequest.email());
            changes = true;
        }

        if (customerUpdateRequest.age() != null && !customerUpdateRequest.age().equals(customer.getAge())) {
            customer.setAge(customerUpdateRequest.age());
            changes = true;
        }

        if (!changes) {
            throw new RequestValidationException("Nothing changed");
        }

        customerDao.updateCustomer(customer);
    }
}
