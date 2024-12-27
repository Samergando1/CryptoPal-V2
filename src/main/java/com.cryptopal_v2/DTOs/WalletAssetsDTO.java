package com.cryptopal_v2.DTOs;

import java.math.BigDecimal;

public class WalletAssetsDTO {
    public record WalletAssetDTO(
            String symbol,
            String tokenContractAddress,
            String tokenType,
            String balance, // Hexadecimal string from JSON
            BigDecimal currentUsdPrice,
            BigDecimal totalSupply,
            Integer decimals,
            String logoUri, // From logos array
            String homepageUrl // From URLs array
    ) {}

}
