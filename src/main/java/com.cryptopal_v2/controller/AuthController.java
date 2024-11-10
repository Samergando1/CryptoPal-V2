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
import org.springframework.web.bind.annotation.*;

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
     * @param  payload
     * @return
     */

    @PostMapping("/google-login")
    public ResponseEntity<String> authenticateWithGoogle(@RequestBody Map<String, String> payload) {
        try {
            String firebaseIdToken = payload.get("firebaseIdToken"); // Extract token from JSON
            FirebaseToken decodedToken = firebaseAuthService.verifyToken(firebaseIdToken);
            String uid = decodedToken.getUid();
            firebaseAuthService.saveIfUserNotExists(uid);
            return ResponseEntity.ok("User authenticated with UID: " + uid);
        } catch (FirebaseAuthException e) {
            e.printStackTrace();  // Log the exact error for debugging
            return ResponseEntity.status(401).body("Error: " + e.getMessage());
        }
    }


    /**
     * Sign up a new user with email, password, and displayName.
     * @param payload
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signUpUser(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");
        String displayName = payload.get("displayName");

        try {
            // Create a user in Firebase and save their information
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password)
                    .setDisplayName(displayName);

            UserRecord userRecord = firebaseAuthService.createUser(request);

            // Create a User instance for local PostgreSQL DB
            User newUser = new User();
//            newUser.setFirebaseUid(userRecord.getUid());
            newUser.setEmail(email);
            newUser.setDisplayName(displayName);
            newUser.setCreatedAt(LocalDateTime.now());

            userRepository.save(newUser);

            return ResponseEntity.ok("User signed up successfully with UID: " + userRecord.getUid());

        } catch (FirebaseAuthException e) {
            return ResponseEntity.badRequest().body("Error during sign-up: " + e.getMessage());
        }
    }

    /**
     * Test endpoint to add a user directly without Firebase.
     * @param user
     * @return
     */
    @PostMapping("/test-add-user")
    public ResponseEntity<String> testAddUser() {
        try {
            User newUser = new User();
            newUser.setEmail("test@example.com");
            newUser.setDisplayName("Test User");
            newUser.setCreatedAt(LocalDateTime.now());

            // Use the inherited save method from JpaRepository
            userRepository.save(newUser);

            return ResponseEntity.ok("Test user added successfully");
        } catch (Exception e) {
            e.printStackTrace(); // Log the exact error
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }


}

// Add Post Mappings for WalletAddresses
