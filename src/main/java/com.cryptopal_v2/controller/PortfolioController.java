package com.cryptopal_v2.controller;

import com.cryptopal_v2.model.Portfolio;
import com.cryptopal_v2.model.User;
import com.cryptopal_v2.service.PortfolioService;
import com.cryptopal_v2.service.WalletAssetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private WalletAssetsService walletAssetsService;


    @PostMapping("/create-wallet-portfolio")
    public ResponseEntity<String> createWalletPortfolio(
            @RequestHeader("userId") String firebaseUID, // Now userId is kept as String
            @RequestBody Portfolio portfolio,
            @RequestParam boolean connectWallet) {

        try {
            // Pass userId as String to the service layer without parsing to Long
            Portfolio createdPortfolio = portfolioService.createPortfolio(portfolio, firebaseUID);

            if (connectWallet) {
                walletAssetsService.fetchAndSaveAssets(createdPortfolio.getWalletAddress());
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
