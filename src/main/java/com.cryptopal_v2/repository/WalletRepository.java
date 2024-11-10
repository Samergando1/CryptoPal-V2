package com.cryptopal_v2.repository;

import com.cryptopal_v2.model.WalletAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<WalletAddress, Long> {

    // query which finds all wallet addresses associated with a specific user
    List<WalletAddress> findByUserId(Long userId);


}
