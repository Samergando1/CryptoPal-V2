package com.cryptopal_v2.DTOs;

import java.math.BigDecimal;

public class WalletAssetDTO {
    private String symbol;
    private String tokenContractAddress;
    private String tokenType;
    private String balance; // Hexadecimal string from JSON
    private BigDecimal currentUsdPrice;
    private BigDecimal totalSupply;
    private Integer decimals;
    private String logoUri; // From logos array
    private String homepageUrl; // From URLs array

    // I wanted to use a record since this is the better practise but just using a class so i odnt have to put everything into dto constrcutor

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getTokenContractAddress() {
        return tokenContractAddress;
    }

    public void setTokenContractAddress(String tokenContractAddress) {
        this.tokenContractAddress = tokenContractAddress;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public BigDecimal getCurrentUsdPrice() {
        return currentUsdPrice;
    }

    public void setCurrentUsdPrice(BigDecimal currentUsdPrice) {
        this.currentUsdPrice = currentUsdPrice;
    }

    public BigDecimal getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(BigDecimal totalSupply) {
        this.totalSupply = totalSupply;
    }

    public Integer getDecimals() {
        return decimals;
    }

    public void setDecimals(Integer decimals) {
        this.decimals = decimals;
    }

    public String getLogoUri() {
        return logoUri;
    }

    public void setLogoUri(String logoUri) {
        this.logoUri = logoUri;
    }

    public String getHomepageUrl() {
        return homepageUrl;
    }

    public void setHomepageUrl(String homepageUrl) {
        this.homepageUrl = homepageUrl;
    }

    // Getters and setters...
}

