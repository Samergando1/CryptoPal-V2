package com.cryptopal_v2.mappers;

import com.cryptopal_v2.model.WalletAddress;
import com.cryptopal_v2.model.WalletAssets;
import com.cryptopal_v2.responses.WalletAssetResponse;

import java.math.BigDecimal;
import java.math.BigInteger;

public class WalletAssetsMapper {

    // Define maximum allowed value based on database precision and scale
    private static final BigDecimal MAX_ALLOWED_VALUE = new BigDecimal("99999999999999999999999999999999.999999999999999999");

    // Helper method to sanitize BigDecimal values
    private BigDecimal sanitizeValue(BigDecimal value) {
        if (value == null) return BigDecimal.ZERO; // Default null values to zero
        return value.compareTo(MAX_ALLOWED_VALUE) > 0 ? MAX_ALLOWED_VALUE : value;
    }

    // Method to map the deserialized tokens to WalletAssets
    public WalletAssets mapToEntity(WalletAssetResponse response, WalletAddress walletAddress) {

        // Initialize WalletAssets entity
        WalletAssets walletAsset = new WalletAssets();

        // Set symbol (fallback to "UNKNOWN" if null or empty)
        walletAsset.setSymbol(response.getSymbol() != null && !response.getSymbol().isEmpty()
                ? response.getSymbol()
                : "UNKNOWN");

        // Set token contract address
        System.out.println(response.getContractAddress());
        walletAsset.setTokenContractAddress(response.getContractAddress());

        // Set decimals (default to 0 if null)
        Integer decimals = response.getDecimals() != null ? response.getDecimals() : 0;
        walletAsset.setDecimals(decimals);

        // Convert balance from hex to BigDecimal
        try {
            BigDecimal holdingAmount = new BigDecimal(new BigInteger(response.getBalance().substring(2), 16))
                    .divide(BigDecimal.TEN.pow(decimals), BigDecimal.ROUND_DOWN);
            walletAsset.setHoldingAmount(sanitizeValue(holdingAmount)); // Sanitize holding amount
        } catch (Exception e) {
            System.err.println("Error parsing balance for contract: " + response.getContractAddress());
            e.printStackTrace();
            walletAsset.setHoldingAmount(BigDecimal.ZERO); // Default to zero if parsing fails
        }

        // Set current USD price (fallback to 0 if null)
        BigDecimal priceUsd = response.getCurrentUsdPrice() != null ? response.getCurrentUsdPrice() : BigDecimal.ZERO;
        walletAsset.setPriceUsd(sanitizeValue(priceUsd)); // Sanitize price USD

        // Set total supply (fallback to null if not available)
        if (response.getTotalSupply() != null) {
            walletAsset.setTotalSupply(sanitizeValue(response.getTotalSupply())); // Sanitize total supply
        } else {
            walletAsset.setTotalSupply(null);
        }

        // Calculate valueUsd if priceUsd is available
        BigDecimal valueUsd = priceUsd.compareTo(BigDecimal.ZERO) > 0
                ? walletAsset.getHoldingAmount().multiply(priceUsd)
                : BigDecimal.ZERO;
        walletAsset.setValueUsd(sanitizeValue(valueUsd)); // Sanitize value USD

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
        System.out.println("WalletAddress ID: " + walletAddress.getId());
        System.out.println("WalletAddress Name: " + walletAddress.getWalletAddress());


        return walletAsset;
    }
}
