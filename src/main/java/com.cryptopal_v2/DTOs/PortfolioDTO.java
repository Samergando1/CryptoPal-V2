package com.cryptopal_v2.DTOs;


import com.cryptopal_v2.model.WalletAddress;

/**
 * 1
 * @param walletAddress
 * @param portfolioName
 */
public record PortfolioDTO(String walletAddress, String portfolioName, String avatar, boolean isConnected) {}


