package com.cryptopal_v2.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class WalletAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String address;

    @OneToOne(mappedBy = "walletAddress")
    private Portfolio portfolio;
} 