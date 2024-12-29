package com.cryptopal_v2.service;

import com.cryptopal_v2.DTOs.PortfolioDTO;
import com.cryptopal_v2.model.Portfolio;
import com.cryptopal_v2.model.User;
import com.cryptopal_v2.model.WalletAddress;
import com.cryptopal_v2.repository.PortfolioRepository;
import com.cryptopal_v2.repository.UserRepository;
import com.cryptopal_v2.repository.WalletAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletAddressRepository walletAddressRepository;

    @Autowired
    private WalletAssetsService walletAssetsService;

    /**
     * Creates a new portfolio for a user, optionally connecting it to a wallet address.
     * The @Transactional here ensures changes do not persist if they are any exceptions thrown in the service
     * @param portfolioRequestDTO
     * @param firebaseUid
     * @return
     */
    @Transactional
    public Portfolio createPortfolio(PortfolioDTO portfolioRequestDTO, String firebaseUid) {
        // Fetch user from repository
        // The precondition is really that the user has been created and exists within our database
        Optional<User> userOpt = userRepository.findByFirebaseUid(firebaseUid);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found!");
        }
        User user = userOpt.get();      // getting the user object stored in the database

        // creates a portfolio model to represent the logged in user

        // Create a portfolio model to represent the logged-in user
        Portfolio portfolio = new Portfolio();
        portfolio.setName(portfolioRequestDTO.portfolioName());
        portfolio.setIsConnected(portfolioRequestDTO.isConnected());
        portfolio.setAvatar(portfolioRequestDTO.avatar());
        portfolio.setUser(user); // Associate the portfolio with the logged-in user

        if (portfolioRequestDTO.isConnected() && portfolioRequestDTO.walletAddress() != null) {
            WalletAddress wallet = new WalletAddress();
            wallet.setWalletAddress(portfolioRequestDTO.walletAddress().getWalletAddress());
            wallet.setFirebaseUid(firebaseUid); // Associate the Firebase UID
            wallet.setUser(user); // Set the user here
            portfolio.setWalletAddress(wallet); // Associate the wallet with the portfolio

            // Save wallet address in the repository
            walletAddressRepository.save(wallet);

            // Fetch initial assets for the wallet
            walletAssetsService.fetchWalletAssetsAndSave(wallet.getWalletAddress());
        }
        // Save and return the newly created portfolio

        return portfolioRepository.save(portfolio);
    }

    /**
     * Deletes a portfolio and removes associated wallet and assets if necessary.
     * @param portfolioId the ID of the portfolio to delete
     */
    @Transactional
    public void deletePortfolio(Long portfolioId) {
        Optional<Portfolio> portfolioOpt = portfolioRepository.findById(portfolioId);
        if (portfolioOpt.isEmpty()) {
            throw new RuntimeException("Portfolio not found");
        }

        Portfolio portfolio = portfolioOpt.get();
        if (portfolio.getIsConnected() && portfolio.getWalletAddress() != null) {
            // Delete associated wallet and assets
            walletAddressRepository.delete(portfolio.getWalletAddress());
        }
        // Delete the portfolio
        portfolioRepository.delete(portfolio);
    }

    /**
     * Polling mechanism for updating assets of connected portfolios every 5 minutes.
     */
    @Scheduled(fixedRate = 300000) // 10 minutes in milliseconds
    public void pollForConnectedWalletUpdates() {
        // Find all portfolios with connected wallets
        portfolioRepository.findByIsConnected(true).forEach(portfolio -> {
            WalletAddress walletAddress = portfolio.getWalletAddress();
            if (walletAddress != null) {
//                walletAssetsService.fetchAndSaveAssets(walletAddress);

            }
        });
    }
}
