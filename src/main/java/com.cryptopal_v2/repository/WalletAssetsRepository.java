package com.cryptopal_v2.repository;

import com.cryptopal_v2.model.WalletAssets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@Component
public interface WalletAssetsRepository extends JpaRepository<WalletAssets, Long> {

}
