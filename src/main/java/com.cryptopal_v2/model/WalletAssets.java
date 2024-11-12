package com.cryptopal_v2.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "wallet_assets")
public class WalletAssets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to WalletAddress
    @OneToOne
    @JoinColumn(name = "wallet_address_id", referencedColumnName = "id", nullable = false)
    private WalletAddress walletAddress;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "token_contract_address")
    private String tokenContractAddress;

    @Column(name = "holding_amount")
    private BigDecimal holdingAmount;

    @Column(name = "price_usd")
    private BigDecimal priceUsd;

    @Column(name = "value_usd")
    private BigDecimal valueUsd;

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

    public BigDecimal getHoldingAmount() {
        return holdingAmount;
    }

    public void setHoldingAmount(BigDecimal holdingAmount) {
        this.holdingAmount = holdingAmount;
    }

    public BigDecimal getPriceUsd() {
        return priceUsd;
    }

    public void setPriceUsd(BigDecimal priceUsd) {
        this.priceUsd = priceUsd;
    }

    public BigDecimal getValueUsd() {
        return valueUsd;
    }

    public void setValueUsd(BigDecimal valueUsd) {
        this.valueUsd = valueUsd;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
}
