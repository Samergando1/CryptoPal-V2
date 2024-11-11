package com.cryptopal_v2.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "wallet_address")
public class WalletAddress {

    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Unique wallet address identifier
    @Getter
    @Setter
    @Column(name = "wallet_address", nullable = false, unique = true)
    private String walletAddress;

    // Balance of the wallet
    @Getter
    @Setter
    @Column(name = "balance", precision = 19, scale = 4)
    private BigDecimal balance;

    // Last time the balance or other data was fetched
    @Getter
    @Setter
    @Column(name = "last_fetched")
    private LocalDateTime lastFetched;

    // Establishing a one-to-one relationship with Portfolio
    @Setter
    @Getter
    @OneToOne(mappedBy = "walletAddress", cascade = CascadeType.ALL, orphanRemoval = true)
    private Portfolio portfolio;

    @OneToOne(mappedBy = "walletAddress", cascade = CascadeType.ALL, orphanRemoval = true)
    private WalletAssets walletAssets;

    // Getters and setters

}
