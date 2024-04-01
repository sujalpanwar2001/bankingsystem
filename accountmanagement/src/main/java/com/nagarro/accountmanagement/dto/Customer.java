package com.nagarro.accountmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.nagarro.accountmanagement.enums.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private Long customerId;
    private String name;
    private String email;
    private String mobile;
    private String password;
    private LocalDateTime created;
    private LocalDateTime modified;
    private Status status;
}
