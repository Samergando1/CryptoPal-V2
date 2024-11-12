package com.cryptopal_v2.model;

import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Entity
@Component
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String avatar;

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
    // Could have used Lombok but I was having real issues with this breaking build, might add this later.


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public WalletAddress getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(WalletAddress walletAddress) {
        this.walletAddress = walletAddress;
    }

    public boolean getIsConnected() {
        return isConnected;
    }

    public void setIsConnected(boolean connected) {
        isConnected = connected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
