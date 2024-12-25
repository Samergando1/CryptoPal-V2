package com.cryptopal_v2.model;

import jakarta.persistence.*;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Entity
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String avatar;
    private String name; // Name of the portfolio
    private boolean isConnected; // True if connected to a wallet, false for manual asset since these two portfolio types are managed differently

    // Reference to the WalletAddress if it's a connected portfolio
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "wallet_address_id", referencedColumnName = "id", nullable = true)
    private WalletAddress walletAddress;

    @OneToOne(mappedBy = "portfolio", cascade = CascadeType.ALL, orphanRemoval = true)
    private ManualAssets manualAsset;

    // Additional metadata
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Link Portfolio to a specific user
    @ManyToOne
    @JoinColumn(name = "user_id")  // Remove insertable = false, updatable = false
    private User user;


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsConnected() {
        return isConnected;
    }

    public void setIsConnected(boolean connected) {
        isConnected = connected;
    }

    public WalletAddress getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(WalletAddress walletAddress) {
        this.walletAddress = walletAddress;
    }

    public ManualAssets getManualAsset() {
        return manualAsset;
    }

    public void setManualAsset(ManualAssets manualAsset) {
        this.manualAsset = manualAsset;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
