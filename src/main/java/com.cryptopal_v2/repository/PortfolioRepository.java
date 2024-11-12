package com.cryptopal_v2.repository;

import com.cryptopal_v2.model.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    List<Portfolio> findByIsConnected(boolean isConnected); // to find connected portfolios
}
