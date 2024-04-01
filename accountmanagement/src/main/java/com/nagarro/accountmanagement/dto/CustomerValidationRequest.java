package com.nagarro.accountmanagement.dto;

import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerValidationRequest {

    private Long customerId;
    private String name;
    private String email;
    private String mobile;
    private String password;
    private Long amount;

}
