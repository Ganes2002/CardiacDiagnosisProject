package com.cardiac.user.service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.cardiac.user.exception.FormatValidation;
import com.cardiac.user.exception.UsernameAlreadyExistsException;
import com.cardiac.user.model.UserProfile;
import com.cardiac.user.model.Users;
import com.cardiac.user.repository.UserProfileRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserProfileServiceImpl implements UserProfileService{
    
    @Autowired
    private KafkaTemplate<String, Object> template;

    @Autowired
    private UserProfileRepository userProfileRepository;

    public UserProfile registerUser(UserProfile userProfile) {
    	validateUserProfile(userProfile);
        if (userProfileRepository.existsByUsername(userProfile.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists: " + userProfile.getUsername());
        }
        return userProfileRepository.save(userProfile);
    }

    private void validateUserProfile(UserProfile userProfile) {
		validateUsername(userProfile.getUsername());
		validateEmail(userProfile.getEmail());
		checkExistingUser(userProfile.getUsername(),userProfile.getEmail());
	}
 
	private void validateUsername(String username) {
		if (username == null) {
			throw new FormatValidation("Username cannot be null.");
		} else if (username.length() <= 2) {
			throw new FormatValidation("Username must be greater than 2 characters.");
		}		
	}
 
	private void validateEmail(String email) {
		if (email == null) {
			throw new FormatValidation("Email cannot be null.");
		} else if (!email.matches("^[A-Za-z0-9+_.-]+@(gmail\\.com|itcinfotech\\.com)$")) {
			throw new FormatValidation("Invalid email format. Only @gmail.com and @itcinfotech.com are allowed.");
		}
	}
 
	private void checkExistingUser(String username,String email) {
		System.out.println("data from validation"+username);
		if (userProfileRepository.findByUsername(username).isPresent()) {
			throw new FormatValidation("Username already exists");
		}
		if (userProfileRepository.findByEmail(email) != null) {
			throw new FormatValidation("Email is already in use. Please choose a different one.");
		}
	}

    public UserProfile updateUser(UserProfile userProfile) {
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findByUsername(userProfile.getUsername());
        if (optionalUserProfile.isPresent()) {
            UserProfile existingUserProfile = optionalUserProfile.get();
            try {
    			validateUpdateFields(existingUserProfile, userProfile);
    		} catch (IllegalArgumentException e) {
    			throw new IllegalArgumentException(e.getMessage());
    		}
            
            if (userProfile.getUsername() != null) {
    			validateUsername(userProfile.getUsername());
    			existingUserProfile.setUsername(userProfile.getUsername());
    		}
            if (userProfile.getEmail() != null) {
    			validateEmail(userProfile.getEmail());
    			existingUserProfile.setUsername(userProfile.getUsername());
    		}
            
            if (userProfile.getFirstName() != null) {
//    			validateEmail(userProfile.getEmail());
    			existingUserProfile.setFirstName(userProfile.getFirstName());
    		}
            
            if (userProfile.getLastName() != null) {
//    			validateEmail(userProfile.getEmail());
    			existingUserProfile.setLastName(userProfile.getLastName());
            }
            
            if (userProfile.getDob() != null) {
//    			validateEmail(userProfile.getEmail());
    			existingUserProfile.setDob(userProfile.getDob());
    		}
            
            if (userProfile.getGender() != null) {
    			existingUserProfile.setGender(userProfile.getGender());
    		}
            
            if (userProfile.getDepartment() != null) {
    			existingUserProfile.setDepartment(userProfile.getDepartment());
    		}
            
            return userProfileRepository.save(existingUserProfile);
        } else {
            throw new EntityNotFoundException("User not found: " + userProfile.getUsername());
        }
    }
    
    public void validateUpdateFields(UserProfile existingUser, UserProfile updatedSignupEntity)
			throws IllegalArgumentException {
		if (updatedSignupEntity.getUsername() != null
&& !updatedSignupEntity.getUsername().equals(existingUser.getUsername())) {
			throw new IllegalArgumentException("Username cannot be updated.");
		}
		if (updatedSignupEntity.getEmail() != null && !updatedSignupEntity.getEmail().equals(existingUser.getEmail())) {
			throw new IllegalArgumentException("Email cannot be updated.");
		}
	}

    public boolean deleteUser(String username) {
        Optional<UserProfile> optional = userProfileRepository.findByUsername(username);
        if (optional.isPresent()) {
            UserProfile profile = optional.get();
            userProfileRepository.delete(profile);
            return true;
        } else {
            throw new EntityNotFoundException("User not found: " + username);
        }
    }

    public UserProfile getUser(String username) {
        return userProfileRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));
    }
    

    public void sendObjectToTopic(Users userAuth) {
        CompletableFuture<SendResult<String, Object>> future = template.send("my-itctopic4", userAuth);

        future.whenComplete((res, exc) -> {
            if (exc == null) {
                System.out.println("Object Sent = " + userAuth.toString() + " with offset value = " + res.getRecordMetadata().offset());
            } else {
                System.out.println("Object Sent failed due to " + exc.getMessage());
            }
        });
    }
}
