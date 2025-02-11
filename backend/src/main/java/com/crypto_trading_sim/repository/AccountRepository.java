package com.crypto_trading_sim.repository;

import com.crypto_trading_sim.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findFirstByOrderByIdAsc();
}
