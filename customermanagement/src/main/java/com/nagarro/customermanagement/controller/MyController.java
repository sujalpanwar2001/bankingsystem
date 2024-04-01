package com.nagarro.customermanagement.controller;

import com.nagarro.customermanagement.dto.CustomerValidationRequest;
import com.nagarro.customermanagement.entities.Customer;
import com.nagarro.customermanagement.enums.Status;
import com.nagarro.customermanagement.exceptions.AccountServiceUnavailableException;
import com.nagarro.customermanagement.exceptions.CustomerExistsException;
import com.nagarro.customermanagement.exceptions.CustomerNotFoundException;
import com.nagarro.customermanagement.exceptions.FaultToleranceException;
import com.nagarro.customermanagement.external.services.AccountService;
import com.nagarro.customermanagement.services.CustomerService;
import feign.FeignException;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/customers")
public class MyController {


    @Autowired
    private CustomerService customerService;

    @Autowired
    private AccountService accountService;

    // Endpoint to add a customer
    @PostMapping
    @CircuitBreaker(name = "accountBreaker", fallbackMethod = "accountFallback")
    @Transactional
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer) {
        log.info("Received request to add new customer: {}", customer);
        if (customerService.isEmailInUse(customer.getEmail())) {
            throw new CustomerExistsException("Email is already in use");
        }

        // Check if the mobile is already in use
        if (customerService.isMobileInUse(customer.getMobile())) {
            throw new CustomerExistsException("Mobile is already in use");
        }



        // Hash the password before storing it
        customer.setPassword(customerService.encodePassword(customer.getPassword()));

        // Set status as ACTIVE
        customer.setStatus(Status.ACTIVE);

        Customer addedCustomer = customerService.addCustomer(customer);

        try{
            // Calling account microservice to generate a new account for the new customer
            accountService.createAccount(addedCustomer.getCustomerId());

        }catch (FeignException fe){
            log.error("Error calling account microservice to create account for customer {}", addedCustomer, fe);
            throw new AccountServiceUnavailableException("Account service is currently down");
        }



        log.info("Customer added successfully: {}", addedCustomer);

        return new ResponseEntity<>(addedCustomer, HttpStatus.CREATED);
    }



    // Fallback method for CircuitBreaker
    public ResponseEntity<Customer> accountFallback(Customer customer, CallNotPermittedException ex) {
        log.error("Circuit breaker fallback triggered for addCustomer method: {}", ex.getMessage());
        throw new FaultToleranceException("Circuit is currently open ");
    }



    @RateLimiter(name="userRateLimiter", fallbackMethod = "FallbackRateLimiter")
    @GetMapping("getall")
    public List<Customer> getAllCustomer(){
        log.info("Fetching all customers");
        return customerService.getAllCustomers();
    }

    // Fallback method for RateLimiter
    public List<Customer> FallbackRateLimiter(Throwable throwable) {
        log.error("Rate limiter fallback triggered for getall method: {}", throwable.getMessage());
        throw new FaultToleranceException("Request Rate limit exceeded");
    }

    @GetMapping
    public ResponseEntity<Customer> getCustomerDetails(@RequestParam Long customerId) {

        log.info("Fetching customer details for customerId: {}", customerId);
        // Retrieve the customer by customerId
        Customer customer = customerService.getCustomerById(customerId);

        if (customer != null) {
            log.info("Customer details found: {}", customer);
            // Return the customer details if found
            return new ResponseEntity<>(customer, HttpStatus.OK);
        } else {
            log.info("Customer not found for customerId: {}", customerId);
            // Return a not found response if customer not found
            throw new CustomerNotFoundException("Customer with given ID does not exist");
        }
    }

    @PutMapping
    public ResponseEntity<Customer> updateCustomerDetails(@RequestBody Customer updatedCustomer) {
        log.info("Updating customer details: {}", updatedCustomer);
        Customer updated = customerService.updateCustomer(updatedCustomer);
        log.info("Customer details updated successfully: {}", updated);
            return new ResponseEntity<>(updated, HttpStatus.OK);

    }

    @PostMapping("/validate")
    public boolean validateCustomerDetails(@RequestBody CustomerValidationRequest request) {
        log.info("Validating customer details: {}", request);
        Customer customer = customerService.getCustomerById(request.getCustomerId());

        // Check if the customer exists and details match (except for the password)
        if (customer != null &&
                customer.getName().equals(request.getName()) &&
                customer.getEmail().equals(request.getEmail()) &&
                customer.getMobile().equals(request.getMobile()) &&
                customerService.matcher(request.getPassword(),customer.getPassword())
        )
        {
            return true;
        }

        // Return false if customer doesn't exist or details don't match
        return false;
    }


    @DeleteMapping()
    @Transactional
    public ResponseEntity<String> deleteCustomer(@RequestParam Long customerId) {
        try {
            log.info("Marking customer inactive with customerId: {}", customerId);
            customerService.markCustomerAsInactive(customerId);
            log.info("Deleting account for customer with customerId: {}", customerId);
            accountService.deleteAccountByCustomerService(customerId);
            return ResponseEntity.ok("Customer successfully marked inactive in both customer and account table");
        }catch (Exception e){
            log.error("Failed to delete customer with customerId: {}", customerId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete customer");
        }
    }

    @PostMapping("/delete")
    public void deleteAccountByAccountService(@RequestParam Long customerId){
        log.info("Customer account marked as inactive in customer table by account service for customer ID: {}", customerId);
        customerService.markCustomerAsInactive(customerId);
    }





}
