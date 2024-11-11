package com.cryptopal_v2.service;
import com.cryptopal_v2.model.User;


import com.cryptopal_v2.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class FirebaseAuthService {


    /**
     * Dependancies injection of repositories for wallet and user, allow for loose-coupling and testing with mock db.
     * Just got practise overall.
     *
     */
    @Autowired
    private UserRepository userRepository;



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

}
