package com.cryptopal_v2.controller;


import com.cryptopal_v2.model.User;
import com.cryptopal_v2.repository.UserRepository;
import com.cryptopal_v2.service.FirebaseAuthService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/authorize")
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



    // Delete Mapping this should delete everything in my postgresDB and all other associations
    /**
     * Delete a user and their associated data in PostgreSQL and Firebase
     * @param uid The UID of the Firebase user to delete
     * @return ResponseEntity with deletion status
     */
    @DeleteMapping("/delete-user")
    public ResponseEntity<String> deleteUser(@RequestParam String uid) {
        try {
            // Delete user from Firebase
            FirebaseAuth.getInstance().deleteUser(uid);

            // Find the user in the local PostgreSQL DB
            Optional<User> userOptional = userRepository.findByFirebaseUid(uid);
            if (userOptional.isPresent()) {
                // Remove associated wallet addresses and any other dependent entities
                userRepository.delete(userOptional.get());
                return ResponseEntity.ok("User and all associated data deleted successfully");
            } else {
                return ResponseEntity.status(404).body("User not found in the database");
            }
        } catch (FirebaseAuthException e) {
            return ResponseEntity.status(500).body("Error deleting Firebase user: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting user data: " + e.getMessage());
        }
    }



}

// Add Post Mappings for WalletAddresses
