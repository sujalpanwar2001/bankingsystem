package com.nagarro.accountmanagement.service;

import com.nagarro.accountmanagement.entities.Account;
import com.nagarro.accountmanagement.enums.Status;
import com.nagarro.accountmanagement.repo.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;
@Slf4j
@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;


    public Account createNewAccount(Long customerId) {
        log.info("Creating a new account for customer ID: {}", customerId);
        // Create a new account
        Account account = new Account();
        account.setCustomerId(customerId);
        account.setAccountNumber(generateRandomNumericString(12));
        account.setBalance(0.0);

        account.setStatus(Status.ACTIVE);
        log.info("New account created successfully ");

        // Save the account in the database
        return accountRepository.save(account);
    }

    private String generateRandomNumericString(int length) {
        Random random = new Random();
        StringBuilder numericString = new StringBuilder();

        for (int i = 0; i < length; i++) {
            numericString.append(random.nextInt(10));
        }

        return numericString.toString();
    }

    public boolean isAccountExistsForCustomer(Long customerId) {
        log.info("Checking if account exists for customer ID: {}", customerId);
        return accountRepository.existsByCustomerId(customerId);
    }

    public double addMoney(Long customerId, Long amount) {
        log.info("Adding money for customer ID: {}", customerId);
        // Retrieve the account by customerId
        Account account = accountRepository.findByCustomerId(customerId);


            // Add the amount to the balance
            double newBalance = account.getBalance() + amount;

            // Update the balance in the database
            account.setBalance(newBalance);
            accountRepository.save(account);
            log.info("Money added successfully for customer ID: {}. Updated balance: {}", customerId, newBalance);


        return newBalance;
        }

    public double getBalance(Long customerId) {
        log.info("Retrieving balance for customer ID: {}", customerId);
        Account account = accountRepository.findByCustomerId(customerId);
        return account.getBalance();
        
    }

    public double withdrawMoney(Long customerId, double withdrawalAmount) {
        log.info("Withdrawing money for customer ID: {}", customerId);

        // Retrieve the account by customerId
        Account account = accountRepository.findByCustomerId(customerId);

        // Check if there is sufficient balance for withdrawal
        double currentBalance = account.getBalance();

        double newBalance = currentBalance - withdrawalAmount;
        account.setBalance(newBalance);
        accountRepository.save(account);

        log.info("Money withdrawn successfully for customer ID: {}. Updated balance: {}", customerId, newBalance);
        return newBalance;



    }

    public Account getAccountDetails(Long customerId) {
        log.info("Retrieving account details for customer ID: {}", customerId);
        return accountRepository.findByCustomerId(customerId);
    }



    public void markAccountAsInactive(Long customerId) {
        log.info("Marking account as inactive for customer ID: {}", customerId);
        Account account = accountRepository.findByCustomerId(customerId);
        account.setStatus(Status.INACTIVE);
        accountRepository.save(account);
        log.info("Account marked as inactive successfully for customer ID: {}", customerId);
    }

    public boolean verifyActiveness(Long customerId) {
        log.info("Verifying account activeness for customer ID: {}", customerId);
        Account account = accountRepository.findByCustomerId(customerId);
        log.info("Account activeness verified for customer ID: {} ", customerId);
        return account.getStatus() == Status.ACTIVE;
    }
}
