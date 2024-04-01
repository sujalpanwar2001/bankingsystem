package com.nagarro.customermanagement.services;

import com.nagarro.customermanagement.dto.AccountDTO;
import com.nagarro.customermanagement.external.services.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
@Slf4j
public class ExternalService {

    @Autowired
    private AccountService accountService;

    public ResponseEntity<AccountDTO> callRemoteCreateAccount(Long customerId) {
        return accountService.createAccount(customerId);
    }
}
