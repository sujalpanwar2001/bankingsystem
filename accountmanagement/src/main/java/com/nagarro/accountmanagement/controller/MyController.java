package com.nagarro.accountmanagement.controller;

import com.nagarro.accountmanagement.dto.Customer;
import com.nagarro.accountmanagement.dto.CustomerValidationRequest;
import com.nagarro.accountmanagement.entities.Account;
import com.nagarro.accountmanagement.entities.AccountDetailsResponse;
import com.nagarro.accountmanagement.exceptions.AccountExistsException;
import com.nagarro.accountmanagement.exceptions.AccountNotFoundException;
import com.nagarro.accountmanagement.exceptions.FaultToleranceException;
import com.nagarro.accountmanagement.exceptions.UserServiceUnavailableException;
import com.nagarro.accountmanagement.external.services.CustomerService;
import com.nagarro.accountmanagement.service.AccountService;
import feign.FeignException;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/accounts")
public class MyController {

    @Autowired
    private AccountService accountService;

    @Autowired
    CustomerService customerService;


    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestParam Long customerId) {
        log.info("Creating account for customer ID: {}", customerId);
        // Check if an account already exists for the given customerId
        if (accountService.isAccountExistsForCustomer(customerId)) {
            log.error("An account already exists for the provided customer ID: {}", customerId);
            throw new AccountExistsException("An account already exists for the provided customer ID");
        }

        // Create a new account
        Account newAccount = accountService.createNewAccount(customerId);
        log.info("Account created successfully for customer ID: {}", customerId);
        return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addMoney(@RequestBody CustomerValidationRequest request) {
        log.info("Adding money for customer ID: {}", request.getCustomerId());
        try {
            // Validate customer details against customer microservice
            if (customerService.validateCustomerDetails(request)) {

                if(accountService.verifyActiveness(request.getCustomerId())){
                    // Customer details are validated, proceed to add money
                    double updatedBalance = accountService.addMoney(request.getCustomerId(), request.getAmount());
                    log.info("Money added successfully for customer ID: {}. Updated balance: {}", request.getCustomerId(), updatedBalance);

                    return ResponseEntity.ok("Money added successfully. Updated balance: " + updatedBalance);
                }else{
                    log.error("This account is currently INACTIVE for customer ID: {}", request.getCustomerId());
                    throw new AccountNotFoundException("This account is currently INACTIVE");
                }


            } else {
                // Customer details are incorrect
                log.error("Incorrect customer details for customer ID: {}", request.getCustomerId());
                return ResponseEntity.ok("Incorrect details");
            }
        } catch (FeignException fe) {
            log.error("User Service is Currently down");
           throw new UserServiceUnavailableException("User Service is Currently down");
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdrawMoney(@RequestBody CustomerValidationRequest request) {
        log.info("Withdrawing money for customer ID: {}", request.getCustomerId());
        try {
            // Validate customer details against customer microservice
            if (customerService.validateCustomerDetails(request)) {
                // Customer details are validated, proceed to withdraw money

                if(accountService.verifyActiveness(request.getCustomerId())) {
                    // Check if the amount to withdraw is valid
                    double withdrawalAmount = request.getAmount();
                    if (withdrawalAmount <= 0) {
                        log.error("Invalid withdrawal amount for customer ID: {}", request.getCustomerId());
                        return ResponseEntity.ok("Invalid withdrawal amount");
                    }

                    // Retrieve the current balance
                    double currentBalance = accountService.getBalance(request.getCustomerId());

                    // Check if there is sufficient balance for withdrawal
                    if (currentBalance >= withdrawalAmount) {
                        // Withdraw money and update balance
                        double updatedBalance = accountService.withdrawMoney(request.getCustomerId(), withdrawalAmount);
                        log.info("Money withdrawn successfully for customer ID: {}. Updated balance: {}", request.getCustomerId(), updatedBalance);
                        return ResponseEntity.ok("Money withdrawn successfully. Updated balance: " + updatedBalance);
                    } else {
                        // Insufficient balance
                        log.error("Insufficient balance for customer ID: {}", request.getCustomerId());
                        return ResponseEntity.ok("Insufficient balance");
                    }

                }else {
                    log.error("This account is currently INACTIVE for customer ID: {}", request.getCustomerId());
                    throw new AccountNotFoundException("This account is currently INACTIVE");
                }


            } else {
                // Customer details are incorrect
                log.error("Incorrect customer details for customer ID: {}", request.getCustomerId());
                return ResponseEntity.ok("Incorrect details");
            }
        } catch (FeignException fe) {
            log.error("User Service is Currently down");
            throw new UserServiceUnavailableException("User Service is Currently down");
        }
    }

    @RateLimiter(name="accountRateLimiter", fallbackMethod = "FallbackRateLimiter")
    @GetMapping("/details")
    public ResponseEntity<AccountDetailsResponse> getAccountDetails(@RequestParam Long customerId) {
        log.info("Getting account details for customer ID: {}", customerId);
        try {

            if(accountService.getAccountDetails(customerId)==null){
                log.error("No account details found for customer ID: {}", customerId);
                throw new AccountExistsException("No account details found for customer ID: " + customerId);

            }



                // Retrieve customer details from customer service
                Customer customer = customerService.getCustomerDetails(customerId).getBody();


                // Retrieve account details from account service
                Account accountDetails = accountService.getAccountDetails(customerId);

                // Construct a response object
                AccountDetailsResponse response = new AccountDetailsResponse(customer, accountDetails);
                log.info("Account details retrieved successfully for customer ID: {}", customerId);

                return ResponseEntity.ok(response);

        } catch (FeignException fe) {
            log.error("User Service is Currently down");
            throw new UserServiceUnavailableException("User Service is Currently down");
        }


    }

    public ResponseEntity<AccountDetailsResponse> FallbackRateLimiter(@RequestParam Long customerId, Throwable throwable) {
        log.error("Rate limiter fallback triggered for details method: {}", throwable.getMessage());
        throw new FaultToleranceException("Request Rate limit exceeded");
    }

    @DeleteMapping()
    @Transactional
    public ResponseEntity<String> deleteAccount(@RequestParam Long customerId) {
        try {
            accountService.markAccountAsInactive(customerId);

            customerService.deleteAccountByAccountService(customerId);
            log.info("Customer account marked as inactive in both customer and account table for customer ID: {}", customerId);
            return ResponseEntity.ok("Customer successfully marked inactive in both customer and account table");
        }catch (Exception e){
            log.error("Failed to delete customer account for customer ID: {}", customerId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete customer");
        }
    }


    @PostMapping("/delete")
    public void deleteAccountByCustomerService(@RequestParam Long customerId){
        accountService.markAccountAsInactive(customerId);
        log.info("Customer account marked as inactive in account table by customer service for customer ID: {}", customerId);
    }







}
