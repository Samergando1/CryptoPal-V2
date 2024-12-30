package com.cryptopal_v2.model;

import jakarta.persistence.*;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    // this is the case each walletAddress will have a list of associated wallet assets
    @OneToMany(mappedBy = "walletAddress", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WalletAssets> walletAssets = new ArrayList<>();


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


    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public List<WalletAssets> getWalletAssets() {
        return walletAssets;
    }

    public void setWalletAssets(List<WalletAssets> walletAssets) {
        this.walletAssets = walletAssets;
    }


    public void setUser(User user) {
        this.user = user;
    }
}
