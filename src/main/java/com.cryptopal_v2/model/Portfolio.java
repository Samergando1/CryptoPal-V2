package com.cryptopal_v2.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Name of the portfolio
    private boolean isConnected; // True if connected to a wallet, false for manual

    // Reference to the WalletAddress if it's a connected portfolio
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "wallet_address_id", referencedColumnName = "id", nullable = true)
    private WalletAddress walletAddress;

    @OneToOne(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private ManualAssets manualAsset;

    // Additional metadata
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void setUser(User user) {
    }

    // Getters and Setters
}
