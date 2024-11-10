package com.cryptopal_v2.service;
import com.cryptopal_v2.model.User;


import com.cryptopal_v2.model.WalletAddress;
import com.cryptopal_v2.repository.UserRepository;
import com.cryptopal_v2.repository.WalletRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class FirebaseAuthService {


    /**
     * Dependancies injection of repositories for wallet and user, allow for loose-coupling and testing with mock db.
     * Just got practise overall.
     *
     */
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    /**
     * Verifies the Firebase ID token.
     * @param idToken Firebase ID token to verify.
     * @return FirebaseToken object if verification is successful.
     * @throws FirebaseAuthException if the token is invalid.
     */
    public FirebaseToken verifyToken(String idToken) throws FirebaseAuthException {
        return FirebaseAuth.getInstance().verifyIdToken(idToken);
    }

    /**
     * Saves a new user to the database if they don't already exist, based on Firebase UID.
     * @param uid Firebase UID of the user.
     * @throws FirebaseAuthException if there's an error fetching the user from Firebase.
     */
    public void saveIfUserNotExists(String uid) throws FirebaseAuthException {
        if (!userRepository.existsByFirebaseUid(uid)) {
            UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);
            User user = new User();
            user.setFirebaseUid(userRecord.getUid());
            user.setDisplayName(userRecord.getDisplayName());
            user.setEmail(userRecord.getEmail());
            user.setCreatedAt(LocalDateTime.now());
            userRepository.save(user);
        }
    }

    /**
     * Saves a test user directly to the database (useful for testing without Firebase).
     * @param email User's email.
     * @param displayName User's display name.
     * @return The saved User object.
     */
    public User saveUserForTesting(String email, String displayName) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            user = new User();
            user.setDisplayName(displayName);
            user.setEmail(email);
            user.setCreatedAt(LocalDateTime.now());
            userRepository.save(user);
        }
        return user;
    }

    /**
     * Saves key user sign in information to the firebase database. So idea is that local postgresDB stores
     * key user info, while this is what we will use for authenticating user signups
     * @param request
     * @return
     * @throws FirebaseAuthException
     */

    public UserRecord createUser(UserRecord.CreateRequest request) throws FirebaseAuthException {
        // This method uses FirebaseAuth to create a user based on the request
        return FirebaseAuth.getInstance().createUser(request);
    }

    /**
     * Adds wallet addresses for a given user.
     * @param userId ID of the user.
     * @param walletAddresses List of wallet addresses to add.
     * @param walletNicknames List of wallet nicknames corresponding to each address.
     * @throws IllegalArgumentException if the size of addresses and nicknames lists do not match.
     */
    public void addWalletAddresses(Long userId, List<String> walletAddresses, List<String> walletNicknames) {
        if (walletAddresses.size() != walletNicknames.size()) {
            throw new IllegalArgumentException("The number of wallet addresses must match the number of portfolios");
        }

        // Find the user once outside the loop
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Loop to save each wallet address with the corresponding nickname
        for (int i = 0; i < walletAddresses.size(); i++) {
            WalletAddress newWalletAddress = new WalletAddress();
            newWalletAddress.setWalletAddress(walletAddresses.get(i));
            newWalletAddress.setWalletNickname(walletNicknames.get(i));
            newWalletAddress.setUser(user);

            walletRepository.save(newWalletAddress);
        }
    }

    /**
     * Retrieves all wallet addresses for a given user by user ID.
     * @param userId ID of the user.
     * @return List of WalletAddress objects.
     * @throws RuntimeException if the user is not found.
     */
    public List<WalletAddress> getWalletAddressesByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Could not fetch user by ID: " + userId));
        return user.getWalletAddresses();
    }
}
