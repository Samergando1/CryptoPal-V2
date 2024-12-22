package com.cryptopal_v2.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "wallet_assets")
public class WalletAssets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to WalletAddress with many-to-one relationship since multiple assets can be linked to a wallet address
    @OneToOne // Changed to ManyToOne as multiple assets can be linked to the same wallet
    @JoinColumn(name = "wallet_address_id", referencedColumnName = "id", nullable = false)
    private WalletAddress walletAddress;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "token_contract_address")
    private String tokenContractAddress;

    @Column(name = "token_type")
    private String tokenType;

    @Column(name = "holding_amount", precision = 38, scale = 18)
    private BigDecimal holdingAmount;

    @Column(name = "price_usd", precision = 38, scale = 18)
    private BigDecimal priceUsd;

    @Column(name = "value_usd", precision = 38, scale = 18)
    private BigDecimal valueUsd;




    @Column(name = "token_id", nullable = true)
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

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
}
