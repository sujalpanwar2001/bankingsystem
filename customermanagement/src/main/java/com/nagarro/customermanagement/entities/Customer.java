package com.nagarro.customermanagement.entities;
import jakarta.persistence.*;
import lombok.*;
import com.nagarro.customermanagement.enums.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId; // primary key

    private String name;
    private String email; // unique
    private String mobile; // unique
    private String password;


    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private LocalDateTime created;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private LocalDateTime modified;

    @Enumerated(EnumType.STRING)
    private Status status;







}

