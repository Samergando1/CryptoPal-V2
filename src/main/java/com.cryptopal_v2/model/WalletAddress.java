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

    @Column(name = "wallet_address", nullable = false, unique = true)
    private String walletAddress;

    @Column(name = "firebase_uid", nullable = false)
    private String firebaseUid;

    @Column(name = "last_fetched")
    private LocalDateTime lastFetched;

    @OneToOne(mappedBy = "walletAddress", cascade = CascadeType.ALL, orphanRemoval = true)
    private Portfolio portfolio;

    @OneToOne(mappedBy = "walletAddress", cascade = CascadeType.ALL, orphanRemoval = true)
    private WalletAssets walletAssets;

    // New association with User
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Getters and setters


    public String getFirebaseUid() {
        return firebaseUid;
    }

    public void setFirebaseUid(String firebaseUid) {
        this.firebaseUid = firebaseUid;
    }

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


    public void setUser(User user) {
        this.user = user;
    }
}
