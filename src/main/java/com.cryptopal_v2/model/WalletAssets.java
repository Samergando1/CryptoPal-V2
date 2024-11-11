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
}
