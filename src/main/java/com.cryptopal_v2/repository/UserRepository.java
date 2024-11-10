package com.cryptopal_v2.repository;

import com.cryptopal_v2.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Component
public interface UserRepository extends JpaRepository<User, Long> {
    // Find a user by Firebase UID
    Optional<User> findByFirebaseUid(String firebaseUid);

    // Find a user by email
    Optional<User> findByEmail(String email);

    // Check if a user exists by Firebase UID
    boolean existsByFirebaseUid(String firebaseUid);

    // Check if a user exists by email
    boolean existsByEmail(String email);

    // Custom query to find users created after a certain date
    List<User> findByCreatedAtAfter(LocalDateTime date);

    // Custom query to fetch all users with a display name containing a specific string
    List<User> findByDisplayNameContaining(String keyword);
}