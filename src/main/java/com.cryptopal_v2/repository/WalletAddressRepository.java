package com.cryptopal_v2.repository;

import com.cryptopal_v2.model.WalletAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Component
public interface WalletAddressRepository extends JpaRepository<WalletAddress, Long> {


    Optional<WalletAddress> findByWalletAddress(String walletAddress);


}
