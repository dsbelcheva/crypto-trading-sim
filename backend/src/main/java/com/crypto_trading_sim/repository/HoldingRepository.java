package com.crypto_trading_sim.repository;

import com.crypto_trading_sim.model.Holding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HoldingRepository extends JpaRepository<Holding, Long> {
    Holding findBySymbol(String symbol);
}
