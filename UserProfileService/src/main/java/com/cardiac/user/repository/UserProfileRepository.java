package com.cardiac.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cardiac.user.model.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {

    
    Optional<UserProfile> findByUsername(String username);

    
    void deleteByUsername(String username);

   
    boolean existsByUsername(String username);


	Object findByEmail(String email);
}
