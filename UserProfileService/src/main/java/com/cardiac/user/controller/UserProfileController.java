package com.cardiac.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cardiac.user.exception.UsernameAlreadyExistsException;
import com.cardiac.user.model.RegistrationData;
import com.cardiac.user.model.Users;
import com.cardiac.user.model.UserProfile;
import com.cardiac.user.service.UserProfileService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserProfileController {

    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);

    @Autowired
    private UserProfileService userProfileService;

//    @CrossOrigin("http://localhost:3000")
    @PostMapping("/add")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationData registrationData) {
        logger.info("Received request to register user with username: {}", registrationData.getUsername());
        
        UserProfile userProfile = new UserProfile();
        userProfile.setUsername(registrationData.getUsername());
        userProfile.setEmail(registrationData.getEmail());
        userProfile.setFirstName(registrationData.getFirstName());
        userProfile.setLastName(registrationData.getLastName());
        userProfile.setDob(registrationData.getDob());
        userProfile.setGender(registrationData.getGender());
        userProfile.setDepartment(registrationData.getDepartment());

        Users userAuth = new Users(registrationData.getUsername(), registrationData.getPassword(), registrationData.getEmail(), "USER");

        try {
            userProfileService.sendObjectToTopic(userAuth);
            UserProfile user = userProfileService.registerUser(userProfile);
            logger.info("User registered successfully with username: {}", registrationData.getUsername());
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UsernameAlreadyExistsException ex) {
            logger.error("Username already exists: {}", registrationData.getUsername());
            return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
        }
    }

//    @CrossOrigin("http://localhost:3000")
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserProfile userProfile) {
        logger.info("Received request to update user with username: {}", userProfile.getUsername());

        UserProfile updatedUser = userProfileService.updateUser(userProfile);
        if (updatedUser != null) {
            logger.info("User updated successfully with username: {}", userProfile.getUsername());
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            logger.warn("Username not found for update: {}", userProfile.getUsername());
            return new ResponseEntity<>("Username not found", HttpStatus.NOT_FOUND);
        }
    }

//    @CrossOrigin("http://localhost:3000")
    @DeleteMapping("/delete/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        logger.info("Received request to delete user with username: {}", username);

        boolean userDeleted = userProfileService.deleteUser(username);
        if (userDeleted) {
            logger.info("User with username {} deleted successfully", username);
            return new ResponseEntity<>("User is deleted", HttpStatus.OK);
        } else {
            logger.warn("Username not found for deletion: {}", username);
            return new ResponseEntity<>("Username not found", HttpStatus.NOT_FOUND);
        }
    }

//    @CrossOrigin("http://localhost:3000")
    @GetMapping("/get/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        logger.info("Received request to fetch user with username: {}", username);

        try {
            UserProfile user = userProfileService.getUser(username);
            logger.info("User found with username: {}", username);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            logger.error("User not found with username: {}", username);
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

//    @CrossOrigin("http://localhost:3000")
    @GetMapping("check/{username}")
    public String checkExistingUser(@PathVariable String username) {
        logger.info("Received request to check existence of user with username: {}", username);

        try {
            UserProfile user = userProfileService.getUser(username);
            if (user != null) {
                logger.info("User found with username: {}", username);
                return "User Present";
            } else {
                logger.warn("User not found with username: {}", username);
                return "User Not Present";
            }
        } catch (EntityNotFoundException e) {
            logger.error("User not found with username: {}", username, e);
            return "User Not Found";
        }
    }
}
