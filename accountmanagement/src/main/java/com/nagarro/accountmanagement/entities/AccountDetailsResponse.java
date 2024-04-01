package com.nagarro.accountmanagement.entities;
import com.nagarro.accountmanagement.dto.Customer;
import com.nagarro.accountmanagement.enums.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDetailsResponse {
    private Long customerId;
    private String name;
    private String email;
    private String mobile;
    private String accountNumber;
    private double balance;
    private LocalDateTime created;
    private LocalDateTime modified;
    private Status status;

    public AccountDetailsResponse(Customer customerDetails, Account accountDetails) {
        this.customerId = customerDetails.getCustomerId();
        this.name = customerDetails.getName();
        this.email = customerDetails.getEmail();
        this.mobile = customerDetails.getMobile();
        this.accountNumber = accountDetails.getAccountNumber();
        this.balance = accountDetails.getBalance();
        this.created = accountDetails.getCreated();
        this.modified = accountDetails.getModified();
        this.status = accountDetails.getStatus();
    }
}
