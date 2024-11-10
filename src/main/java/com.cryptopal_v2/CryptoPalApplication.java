package com.cryptopal_v2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication( exclude = SecurityAutoConfiguration.class)
public class CryptoPalApplication implements CommandLineRunner {

    // Replace this with your Firebase Web API Key from the Firebase Console
    private static final String FIREBASE_API_KEY = "AIzaSyCbBX9lwg0x2-kbf6ga_39-q8UeodZ5D38\n";

    public static void main(String[] args) {
        SpringApplication.run(CryptoPalApplication.class, args);
    }

    /**
     * For Testing the FirebaseAuth functionality before implementing the frontend
     * @param args
     * @throws Exception
     */

    @Override
    public void run(String... args) throws Exception {
        // Step 1: Generate a custom token
        String testUid = "BGIgqxLYMgOGl2yhayOcm4HQbQF3"; // replace with a meaningful UID for testing
        String customToken = generateCustomToken(testUid);

        if (customToken != null) {
            // Step 2: Exchange the custom token for an ID token
            String idToken = exchangeCustomTokenForIdToken(customToken);

            if (idToken != null) {
                System.out.println("Generated ID Token for testing: " + idToken);
            }
        }
    }

    /**
     * Generates a custom Firebase token for a given UID.
     *
     * @param uid The UID of the user for whom to generate the token.
     * @return The generated custom token.
     */
    private String generateCustomToken(String uid) {
        try {
            return FirebaseAuth.getInstance().createCustomToken(uid);
        } catch (FirebaseAuthException e) {
            System.err.println("Error creating custom token: " + e.getMessage());
            return null;
        }
    }

    /**
     * Exchanges a custom token for an ID token using Firebase's REST API.
     *
     * @param customToken The custom token to exchange.
     * @return The ID token.
     */
    private String exchangeCustomTokenForIdToken(String customToken) {
        String url = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithCustomToken?key=" + FIREBASE_API_KEY;
        RestTemplate restTemplate = new RestTemplate();

        // Set headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("token", customToken);
        requestBody.put("returnSecureToken", true);

        // Create HTTP request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            // Send POST request to Firebase REST API
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

            // Extract ID token from the response
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("idToken")) {
                return (String) responseBody.get("idToken");
            } else {
                System.err.println("ID token not found in response.");
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error exchanging custom token: " + e.getMessage());
            return null;
        }
    }
}
