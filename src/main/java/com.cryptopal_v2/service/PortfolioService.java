package com.cryptopal_v2.service;

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
     * @param portfolioRequest
     * @param firebaseUid
     * @return
     */
    @Transactional
    public Portfolio createPortfolio(Portfolio portfolioRequest, String firebaseUid) {
        // Fetch user from repository
        Optional<User> userOpt = userRepository.findByFirebaseUid(firebaseUid);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = userOpt.get();

        // Initialize Portfolio from the incoming request data
        Portfolio portfolio = new Portfolio();
        portfolio.setName(portfolioRequest.getName());
        portfolio.setIsConnected(portfolioRequest.getIsConnected());
        portfolio.setUser(user);
        portfolio.setAvatar(portfolioRequest.getAvatar()); // Set avatar if provided

        if (portfolio.getIsConnected() && portfolioRequest.getWalletAddress() != null && portfolioRequest.getWalletAddress().getWalletAddress() != null) {
            WalletAddress wallet = new WalletAddress();
            wallet.setWalletAddress(portfolioRequest.getWalletAddress().getWalletAddress());
            wallet.setFirebaseUid(firebaseUid);  // Associate the Firebase UID
            wallet.setUser(user);                // Set the user here
            portfolio.setWalletAddress(wallet);

            // Save wallet address in the repository
            walletAddressRepository.save(wallet);

            // Fetch initial assets for the wallet
            walletAssetsService.fetchAndSaveAssets(wallet);
        }


        // Save and return the newly created portfolio
        return portfolioRepository.save(portfolio);
    }

    /**
     * Deletes a portfolio and removes associated wallet and assets if necessary.
     *
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
     * Polling mechanism for updating assets of connected portfolios every 10 minutes.
     */
    @Scheduled(fixedRate = 600000) // 10 minutes in milliseconds
    public void pollForConnectedWalletUpdates() {
        // Find all portfolios with connected wallets
        portfolioRepository.findByIsConnected(true).forEach(portfolio -> {
            WalletAddress walletAddress = portfolio.getWalletAddress();
            if (walletAddress != null) {
                walletAssetsService.fetchAndSaveAssets(walletAddress);

            }
        });
    }
}
