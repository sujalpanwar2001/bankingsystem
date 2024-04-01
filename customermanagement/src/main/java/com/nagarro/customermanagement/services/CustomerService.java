package com.nagarro.customermanagement.services;

import com.nagarro.customermanagement.entities.Customer;
import com.nagarro.customermanagement.enums.Status;
import com.nagarro.customermanagement.exceptions.CustomerExistsException;
import com.nagarro.customermanagement.exceptions.CustomerNotFoundException;
import com.nagarro.customermanagement.repo.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    public String encodePassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    public boolean matcher(CharSequence rawPassword,
                           String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }


    public Customer addCustomer(Customer customer) {
        log.info("Adding new customer: {}", customer);

        return customerRepository.save(customer);
    }


    public List<Customer> getAllCustomers() {
        log.info("Fetching all customers");
        return customerRepository.findAll();
    }


    public Customer getCustomerByEmail(String email) {
        log.info("Fetching customer by email: {}", email);
        return customerRepository.findByEmail(email);
    }

    public Customer updateCustomer(Customer updatedCustomer) {
        log.info("Updating customer: {}", updatedCustomer);
        Long customerId = updatedCustomer.getCustomerId();

        // Retrieve the existing customer by customerId
        Customer existingCustomer = getCustomerById(customerId);

        // Check if the updated email is already in use by another customer
        if (!existingCustomer.getEmail().equals(updatedCustomer.getEmail())) {
            if (isEmailInUse(updatedCustomer.getEmail())) {
                throw new CustomerExistsException("Email is already in use");
            }
        }


        // Check if the updated mobile is already in use by another customer
        if (!existingCustomer.getMobile().equals(updatedCustomer.getMobile())) {
            if (isMobileInUse(updatedCustomer.getMobile())) {
                throw new CustomerExistsException("Mobile is already in use");
            }
        }


        // Update the details of the existing customer
        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        existingCustomer.setMobile(updatedCustomer.getMobile());
        existingCustomer.setPassword(updatedCustomer.getPassword());
        existingCustomer.setPassword(encodePassword(updatedCustomer.getPassword()));


        // Save the updated customer to the database
        return customerRepository.save(existingCustomer);
    }


    public boolean isEmailInUse(String email) {
        log.info("Checking if email {} is in use", email);
        Optional<Customer> existingCustomer = Optional.ofNullable(customerRepository.findByEmail(email));
        return existingCustomer.isPresent();
    }

    public boolean isMobileInUse(String mobile) {
        log.info("Checking if mobile {} is in use", mobile);
        return customerRepository.findByMobile(mobile).isPresent();
    }

    public Customer getCustomerById(Long customerId) {
        log.info("Fetching customer by ID: {}", customerId);
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with given ID not found"));
    }

    public void markCustomerAsInactive(Long customerId) {
        log.info("Marking customer with ID {} as inactive", customerId);
        // Retrieve the customer entity from the database
        Customer customer = customerRepository.findById(customerId)
                .orElse(null);

        if (customer != null) {
            // Update the status of the customer to inactive
            customer.setStatus(Status.INACTIVE);
            customerRepository.save(customer);

        } else{
            log.error("Customer with given customerId {} is not present", customerId);
            throw new CustomerNotFoundException("Customer with given customerId is not present");

        }
    }
}



