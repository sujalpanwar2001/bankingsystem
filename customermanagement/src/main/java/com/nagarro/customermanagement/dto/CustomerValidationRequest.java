package com.nagarro.customermanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
