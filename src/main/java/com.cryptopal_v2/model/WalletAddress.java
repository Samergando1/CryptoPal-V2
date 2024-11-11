package com.cryptopal_v2.model;

import jakarta.persistence.*;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "wallet_address")
public class WalletAddress {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Unique wallet address identifier

    @Column(name = "wallet_address", nullable = false, unique = true)
    private String walletAddress;

    // Balance of the wallet

    @Column(name = "balance", precision = 19, scale = 4)
    private BigDecimal balance;

    // Last time the balance or other data was fetched

    @Column(name = "last_fetched")
    private LocalDateTime lastFetched;

    // Establishing a one-to-one relationship with Portfolio

    @OneToOne(mappedBy = "walletAddress", cascade = CascadeType.ALL, orphanRemoval = true)
    private Portfolio portfolio;

    @OneToOne(mappedBy = "walletAddress", cascade = CascadeType.ALL, orphanRemoval = true)
    private WalletAssets walletAssets;

    // Getters and setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public LocalDateTime getLastFetched() {
        return lastFetched;
    }

    public void setLastFetched(LocalDateTime lastFetched) {
        this.lastFetched = lastFetched;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public WalletAssets getWalletAssets() {
        return walletAssets;
    }

    public void setWalletAssets(WalletAssets walletAssets) {
        this.walletAssets = walletAssets;
    }
}
