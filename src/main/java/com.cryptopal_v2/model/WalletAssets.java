package com.cryptopal_v2.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "wallet_assets")
public class WalletAssets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to WalletAddress with a many-to-one relationship
    @ManyToOne
    @JoinColumn(name = "wallet_address_id", referencedColumnName = "id", nullable = false)
    private WalletAddress walletAddress;

    @Column(name = "symbol", nullable = false)
    private String symbol;

    @Column(name = "token_contract_address", nullable = false)
    private String tokenContractAddress;

    @Column(name = "token_type")
    private String tokenType;

    @Column(name = "holding_amount", precision = 50, scale = 18, nullable = false)
    private BigDecimal holdingAmount;

    @Column(name = "price_usd", precision = 50, scale = 18)
    private BigDecimal priceUsd;

    @Column(name = "value_usd", precision = 50, scale = 18)
    private BigDecimal valueUsd;

    @Column(name = "total_supply", precision = 50, scale = 18)
    private BigDecimal totalSupply;

    @Column(name = "decimals", nullable = false)
    private Integer decimals;

    @Column(name = "logo_uri")
    private String logoUri;

    @Column(name = "homepage_url")
    private String homepageUrl;

    // Token ID (optional, for NFTs or special tokens)
    @Column(name = "token_id")
    private String tokenId;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WalletAddress getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(WalletAddress walletAddress) {
        this.walletAddress = walletAddress;
    }

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

    public BigDecimal getHoldingAmount() {
        return holdingAmount != null ? holdingAmount : BigDecimal.ZERO;
    }

    public void setHoldingAmount(BigDecimal holdingAmount) {
        this.holdingAmount = holdingAmount;
    }

    public BigDecimal getPriceUsd() {
        return priceUsd != null ? priceUsd : BigDecimal.ZERO;
    }

    public void setPriceUsd(BigDecimal priceUsd) {
        this.priceUsd = priceUsd;
    }

    public BigDecimal getValueUsd() {
        return valueUsd != null ? valueUsd : BigDecimal.ZERO;
    }

    public void setValueUsd(BigDecimal valueUsd) {
        this.valueUsd = valueUsd;
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

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
}
