package com.cryptopal_v2.mappers;

import com.cryptopal_v2.model.WalletAddress;
import com.cryptopal_v2.model.WalletAssets;
import com.cryptopal_v2.responses.WalletAssetResponse;

import java.math.BigDecimal;
import java.math.BigInteger;

public class WalletAssetsMapper {

    // simple class to map the deserialized tokens in the form of a json array into walletAssetModels
    public WalletAssets mapToEntity(WalletAssetResponse response, WalletAddress walletAddress) {

        // Initialize WalletAssets entity
        WalletAssets walletAsset = new WalletAssets();

        // Set symbol (fallback to "UNKNOWN" if null or empty)
        walletAsset.setSymbol(response.getSymbol() != null && !response.getSymbol().isEmpty()
                ? response.getSymbol()
                : "UNKNOWN");

        // Set token contract address
        System.out.println(response.getContractAddress());          // we are not getting the contract address
        walletAsset.setTokenContractAddress(response.getContractAddress());

        // Set decimals (default to 0 if null)
        Integer decimals = response.getDecimals() != null ? response.getDecimals() : 0;
        walletAsset.setDecimals(decimals);

        // Convert balance from hex to BigDecimal
        try {
            // Decode hex balance using BigInteger
            BigDecimal holdingAmount = new BigDecimal(new BigInteger(response.getBalance().substring(2), 16))
                    .divide(BigDecimal.TEN.pow(decimals), BigDecimal.ROUND_DOWN); // Avoid division errors
            walletAsset.setHoldingAmount(holdingAmount);
        } catch (Exception e) {
            System.err.println("Error parsing balance for contract: " + response.getContractAddress());
            e.printStackTrace();
            walletAsset.setHoldingAmount(BigDecimal.ZERO); // Default to zero if parsing fails
        }

        // Set current USD price (fallback to 0 if null)
        BigDecimal priceUsd = response.getCurrentUsdPrice() != null ? response.getCurrentUsdPrice() : BigDecimal.ZERO;
        walletAsset.setPriceUsd(priceUsd);

        // Set total supply (fallback to null if not available)
        walletAsset.setTotalSupply(response.getTotalSupply() != null
                ? response.getTotalSupply()
                : null);


        // Calculate valueUsd if priceUsd is available
        walletAsset.setValueUsd(priceUsd.compareTo(BigDecimal.ZERO) > 0
                ? walletAsset.getHoldingAmount().multiply(priceUsd)
                : BigDecimal.ZERO);

        // Extract logo URI (fallback to null if not available)
        walletAsset.setLogoUri(response.getLogos() != null && !response.getLogos().isEmpty()
                ? response.getLogos().get(0).uri()
                : null);

        // Extract homepage URL (fallback to null if not available)
        walletAsset.setHomepageUrl(response.getUrls() != null && !response.getUrls().isEmpty()
                ? response.getUrls().get(0).url()
                : null);

        // Set the wallet address relationship
        walletAsset.setWalletAddress(walletAddress);

        return walletAsset;
    }



}
