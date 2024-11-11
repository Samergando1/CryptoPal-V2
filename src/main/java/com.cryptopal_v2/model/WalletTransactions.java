package com.cryptopal_v2.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class WalletTransactions {
    @Id
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
