package com.nagarro.customermanagement.dto;
import com.nagarro.customermanagement.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {

    private Long id;
    private Long customerId;
    private String accountNumber;
    private double balance;
    private LocalDateTime created;
    private LocalDateTime modified;
    private Status status;

    // You can add additional fields or methods as needed
}