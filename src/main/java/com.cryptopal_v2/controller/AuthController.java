package com.cryptopal_v2.controller;

import com.cryptopal_v2.model.User;
import com.cryptopal_v2.repository.UserRepository;
import com.cryptopal_v2.service.FirebaseAuthService;
import com.cryptopal_v2.service.JWTService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/authorize")
public class AuthController{
    private final FirebaseAuthService firebaseAuthService;
    private final UserRepository userRepository;
    private final JWTService jwtService; // jwt service needs definition
    @Autowired
    public AuthController(FirebaseAuthService firebaseAuthService, UserRepository userRepository, JWTService jwtService) {
        this.firebaseAuthService = firebaseAuthService;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    /**
     * This will allow users to login using their email address
     * @param  credentials
     * @return
     */
    @PostMapping("/google-login")
    public ResponseEntity<Map<String, String>> authenticateWithGoogle(@RequestBody Map<String, String> credentials) {
        try {
            String firebaseIdToken = credentials.get("firebaseIdToken");
            FirebaseToken decodedToken = firebaseAuthService.verifyToken(firebaseIdToken);
            String uid = decodedToken.getUid();
            firebaseAuthService.saveIfUserNotExists(uid);

            // will handle auth once loggedin
            String jwtToken = jwtService.generateToken(uid);

            Map<String, String> response = new HashMap<>();
            // Return UID as a JSON response
            response.put("token", jwtToken);
            response.put("userId", uid);
            return ResponseEntity.ok(response);
        } catch (FirebaseAuthException e) {
            e.printStackTrace();            // this logging is just fine for these purposes lol
            return ResponseEntity.status(401).body(Collections.singletonMap("error", e.getMessage()));
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
            // Create a user in Firebase and save their information, which will primarily be used for user authentication
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(email)
                    .setPassword(password)
                    .setDisplayName(displayName);

            // saves the user to firebase database
            UserRecord userRecord = firebaseAuthService.createUser(request);

            // Create a User instance for local PostgresSQL DB for local database work that have to do with managing portfolios
            User newUser = new User();
            newUser.setFirebaseUid(userRecord.getUid());
            newUser.setEmail(email);
            newUser.setDisplayName(displayName);
            newUser.setCreatedAt(LocalDateTime.now());

            userRepository.save(newUser);

            return ResponseEntity.ok(String.format("%s signed up successfully with UID %s", userRecord.getDisplayName(), userRecord.getUid()));

        } catch (FirebaseAuthException e) {
            return ResponseEntity.badRequest().body("Error during sign-up: " + e.getMessage());
        }
    }



    // Delete Mapping this should delete everything in my postgresDB and all other associations
    /**
     * Delete a user and their associated data in postgresSQL and Firebase
     * @param uid The UID of the Firebase user to delete
     * @return ResponseEntity with deletion status
     */
    @DeleteMapping("/delete-user")
    public ResponseEntity<String> deleteUser(@RequestParam String uid) {
        try {
            // Delete user from Firebase
            FirebaseAuth.getInstance().deleteUser(uid);

            // Find the user in the local PostgresSQL DB
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

