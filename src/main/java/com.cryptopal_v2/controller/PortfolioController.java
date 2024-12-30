package com.cryptopal_v2.controller;

import com.cryptopal_v2.DTOs.PortfolioDTO;
import com.cryptopal_v2.model.Portfolio;
import com.cryptopal_v2.model.User;
import com.cryptopal_v2.service.JWTService;
import com.cryptopal_v2.service.PortfolioService;
import com.cryptopal_v2.service.WalletAssetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {
    private final PortfolioService portfolioService;
    private final WalletAssetsService walletAssetsService;
    private final JWTService jwtService; // jwt service needs definition

    /**
     * Always use constructor based dependancy injection to ensure loose coupling between classess
     * @param portfolioService
     * @param walletAssetsService
     */
    @Autowired
    public PortfolioController(PortfolioService portfolioService, WalletAssetsService walletAssetsService, JWTService jwtService)  {
        this.portfolioService = portfolioService;
        this.walletAssetsService = walletAssetsService;
        this.jwtService = jwtService;
    }

    /**
     * Handles creation of wallet both on the UI and the all the accompanying models for the database to store this information.
     * @param authHeader
     * @param portfolioRequestDTO
     * @return
     */
    @PostMapping("/create-wallet-portfolio")
    public ResponseEntity<String> createWalletPortfolio(
            @RequestHeader("Authorization") String authHeader, // Now userId is kept as String since all actions require signup
            @RequestBody PortfolioDTO portfolioRequestDTO) { // boolean to see if the portfolio is connected to wallet or manual

        try {
            // validate jwt token
            String token = authHeader.replace("Bearer ", "");
            String firebaseUID = jwtService.validateToken(token).getSubject(); // validates token an decrypts json payload

            // Pass userId as String to the service layer without parsing to Long
            Portfolio createdPortfolio = portfolioService.createPortfolio(portfolioRequestDTO, firebaseUID);

            if (createdPortfolio.getIsConnected()) {
                walletAssetsService.fetchWalletAssetsAndSave(createdPortfolio.getWalletAddress().toString());
            }
            return ResponseEntity.ok("Portfolio created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating portfolio: " + e.getMessage());
        }
    }

    // Delete a portfolio by ID
    @DeleteMapping("/delete-portfolio/{id}")
    public ResponseEntity<String> deletePortfolio(@PathVariable Long id) {
        try {
            portfolioService.deletePortfolio(id);
            return ResponseEntity.ok("Portfolio deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting portfolio: " + e.getMessage());
        }
    }
}
