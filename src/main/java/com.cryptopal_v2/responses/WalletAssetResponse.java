package com.cryptopal_v2.responses;

import java.math.BigDecimal;
import java.util.List;

public class WalletAssetResponse {

    private BigDecimal currentUsdPrice;
    private String name;
    private String balance; // Hexadecimal string from JSON
    private List<Logo> logos;
    private BigDecimal totalSupply;
    private Integer decimals;
    private String contractAddress;
    private String symbol;
    private List<Url> urls;

    // Nested classes for logos and URLs
    public record Logo(String uri, int height, int width) {}
    public record Url(String name, String url) {}


    public BigDecimal getCurrentUsdPrice() {
        return currentUsdPrice;
    }

    public void setCurrentUsdPrice(BigDecimal currentUsdPrice) {
        this.currentUsdPrice = currentUsdPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public List<Logo> getLogos() {
        return logos;
    }

    public void setLogos(List<Logo> logos) {
        this.logos = logos;
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

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public List<Url> getUrls() {
        return urls;
    }

    public void setUrls(List<Url> urls) {
        this.urls = urls;
    }
}
