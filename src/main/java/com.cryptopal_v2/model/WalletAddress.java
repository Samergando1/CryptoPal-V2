package com.cryptopal_v2.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "wallet_address")
public class WalletAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "walletAddress")

    private String walletAddress;
    @Column(name = "walletNickname")

    private String walletNickname;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)


    private User user;

    // Additional fields for API data (e.g., balance, last updated)
    @Column(name = "balance")

    private BigDecimal balance;
    @Column(name = "lastFetched")

    private LocalDateTime lastFetched;

    // Getters and setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public void setWalletNickname(String walletNickname) {
        this.walletNickname = walletNickname;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setLastFetched(LocalDateTime lastFetched) {
        this.lastFetched = lastFetched;
    }
}
