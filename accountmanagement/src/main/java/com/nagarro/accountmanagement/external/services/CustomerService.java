package com.nagarro.accountmanagement.external.services;

import com.nagarro.accountmanagement.dto.Customer;
import com.nagarro.accountmanagement.dto.CustomerValidationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "CUSTOMER-SERVICE")
public interface CustomerService {

    @PostMapping("/customers/validate")
    boolean validateCustomerDetails(CustomerValidationRequest request);

    @GetMapping("/customers")
    ResponseEntity<Customer> getCustomerDetails(@RequestParam  Long customerId);
    @PostMapping("/customers/delete")
    void deleteAccountByAccountService(@RequestParam Long customerId);
}
