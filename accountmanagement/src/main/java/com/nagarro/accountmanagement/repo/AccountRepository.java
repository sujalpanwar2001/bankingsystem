package com.nagarro.accountmanagement.repo;

import com.nagarro.accountmanagement.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByCustomerId(Long customerId);

    Account findByCustomerId(Long customerId);
}
