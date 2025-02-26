package com.cryptopal_v2.repository;

import com.cryptopal_v2.model.WalletAssets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
public interface WalletAssetsRepository extends JpaRepository<WalletAssets, Long> {

        @Modifying
        @Query("DELETE FROM WalletAssets wa WHERE wa.walletAddress.id = :walletAddressId")
        void deleteByWalletAddressId(@Param("walletAddressId") Long walletAddressId);



}
