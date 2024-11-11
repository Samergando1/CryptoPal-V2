package com.cryptopal_v2.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_info")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firebase_uid", unique = true, nullable = false)
    private String firebaseUid;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // One-to-many relationship with Portfolio
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Portfolio> portfolios = new ArrayList<>();

    // Getters and Setters

    public Long getId() { return id; }

    public String getFirebaseUid() { return firebaseUid; }

    public String getEmail() { return email; }

    public String getDisplayName() { return displayName; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public List<Portfolio> getPortfolios() { return portfolios; }

    public void setId(Long id) { this.id = id; }

    public void setFirebaseUid(String firebaseUid) { this.firebaseUid = firebaseUid; }

    public void setEmail(String email) { this.email = email; }

    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public void setPortfolios(List<Portfolio> portfolios) { this.portfolios = portfolios; }

}
