package com.cryptopal_v2.repository;

import com.cryptopal_v2.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    // Additional query methods if needed
}
