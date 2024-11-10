package com.cryptopal_v2.controller;


import com.cryptopal_v2.model.User;
import com.cryptopal_v2.repository.UserRepository;
import com.cryptopal_v2.service.FirebaseAuthService;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("CryptoPal/authorize")
public class AuthController{
    private final FirebaseAuthService firebaseAuthService;
    private final UserRepository userRepository;

    @Autowired
    public AuthController(FirebaseAuthService firebaseAuthService, UserRepository userRepository) {
        this.firebaseAuthService = firebaseAuthService;
        this.userRepository = userRepository;
    }

    /**
     * This will allow users to login using their email address
     * @param firebaseIdToken
     * @return
     */
    @PostMapping("/google-login")
    public ResponseEntity<String> authenticateWithGoogle(@RequestParam String firebaseIdToken ){

        try{
            FirebaseToken decodedToken = firebaseAuthService.verifyToken(firebaseIdToken);
            String uid = decodedToken.getUid();
            firebaseAuthService.saveIfUserNotExists(uid);
            return ResponseEntity.ok("User authenticated with UID: " + uid);

        } catch (FirebaseAuthException e){
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }


    /**
     * Sign up a new user with email, password, and displayName.
     * @param email The user's email address.
     * @param password The user's password.
     * @param displayName The user's display name.
     * @return ResponseEntity with sign-up status.
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signUpUser(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String displayName) {

        try {
            // Create a user amd save their information to firebase
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password)
                    .setDisplayName(displayName);

            UserRecord userRecord = firebaseAuthService.createUser(request);

            // Create a user amd save their information to out local postgresDB
            User newUser = new User();
            newUser.setFirebaseUid(userRecord.getUid());
            newUser.setEmail(email);
            newUser.setDisplayName(displayName);
            newUser.setCreatedAt(LocalDateTime.now());

            userRepository.save(newUser);

            return ResponseEntity.ok("User signed up successfully with UID: " + userRecord.getUid());

        } catch (FirebaseAuthException e) {
            return ResponseEntity.badRequest().body("Error during sign-up: " + e.getMessage());
        }
    }


}

// Add Post Mappings for WalletAddresses
