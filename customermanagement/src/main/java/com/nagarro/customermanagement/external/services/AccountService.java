package com.nagarro.customermanagement.external.services;

import com.nagarro.customermanagement.dto.AccountDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ACCOUNT-SERVICE")
public interface AccountService {

    @PostMapping("/accounts/create")
    ResponseEntity<AccountDTO> createAccount(@RequestParam Long customerId);

    @PostMapping("/accounts/delete")
    public void deleteAccountByCustomerService(@RequestParam Long customerId);

}
