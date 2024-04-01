package com.nagarro.customermanagement.repo;

import com.nagarro.customermanagement.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByEmail(String email);

    Optional<Customer> findByMobile(String mobile);



}
