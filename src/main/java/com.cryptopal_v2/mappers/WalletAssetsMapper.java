package com.cryptopal_v2.mappers;

import com.cryptopal_v2.model.WalletAddress;
import com.cryptopal_v2.model.WalletAssets;
import com.cryptopal_v2.responses.WalletAssetResponse;

import java.math.BigDecimal;

public class WalletAssetsMapper {

    // simple class to map the deserialized tokens in the form of a json array into walletAssetModels

    public WalletAssets mapToEntity(WalletAssetResponse response, WalletAddress walletAddress){

        // it will set all the fields needed to persist assets to the database

        WalletAssets walletAsset = new WalletAssets();
        // Set basic fields
        walletAsset.setSymbol(response.getSymbol());
        walletAsset.setTokenContractAddress(response.getContractAddress());
        walletAsset.setDecimals(response.getDecimals());

        // Convert balance from hex to BigDecimal
        BigDecimal holdingAmount = new BigDecimal(Long.decode(response.getBalance()))
                .divide(BigDecimal.TEN.pow(response.getDecimals()));
        walletAsset.setHoldingAmount(holdingAmount);

        walletAsset.setPriceUsd(response.getCurrentUsdPrice());
        walletAsset.setTotalSupply(response.getTotalSupply());

        // Calculate valueUsd if priceUsd is available
        walletAsset.setValueUsd(response.getCurrentUsdPrice() != null
                ? holdingAmount.multiply(response.getCurrentUsdPrice())
                : BigDecimal.ZERO);

        // Extract logo URI (if available)
        walletAsset.setLogoUri(response.getLogos() != null && !response.getLogos().isEmpty()
                ? response.getLogos().get(0).uri()
                : null);


        // Extract homepage URL (if available)
        walletAsset.setHomepageUrl(response.getUrls() != null && !response.getUrls().isEmpty()
                ? response.getUrls().get(0).url()
                : null);

        walletAsset.setWalletAddress(walletAddress);
        return walletAsset;

    }

}
