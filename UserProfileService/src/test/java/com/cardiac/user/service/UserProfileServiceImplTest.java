package com.cardiac.user.service;

import com.cardiac.user.model.UserProfile;
import com.cardiac.user.model.Users;
import com.cardiac.user.repository.UserProfileRepository;

import jakarta.persistence.EntityNotFoundException;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@ExtendWith(MockitoExtension.class)  // Use MockitoExtension for unit tests
public class UserProfileServiceImplTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private UserProfileServiceImpl userProfileService;

    private UserProfile userProfile;

    @BeforeEach
    void setUp() {
        userProfile = new UserProfile();
        userProfile.setUsername("testUser");
        userProfile.setEmail("test@domain.com");
        userProfile.setFirstName("Test");
        userProfile.setLastName("User");
    }

    @Test
    void testRegisterUser() {
        when(userProfileRepository.save(Mockito.any(UserProfile.class))).thenReturn(userProfile);

        UserProfile savedUser = userProfileService.registerUser(userProfile);

        assertNotNull(savedUser);
        assertEquals("testUser", savedUser.getUsername());
    }

    @Test
    void testUpdateUser() {
        when(userProfileRepository.findByUsername("testUser")).thenReturn(Optional.of(userProfile));
        when(userProfileRepository.save(Mockito.any(UserProfile.class))).thenReturn(userProfile);

        UserProfile updatedUser = userProfileService.updateUser(userProfile);

        assertNotNull(updatedUser);
        assertEquals("testUser", updatedUser.getUsername());
    }

    @Test
    void testUpdateUserNotFound() {
        // Set the username to a non-existing user for the test
        String nonExistingUsername = "nonExistingUser";
        
        // Stub the repository to return Optional.empty() for the non-existing user
        when(userProfileRepository.findByUsername(nonExistingUsername)).thenReturn(Optional.empty());

        // Create a new userProfile object and set a non-existing username
        userProfile.setUsername(nonExistingUsername);
        
        // Assert that EntityNotFoundException is thrown
        assertThrows(EntityNotFoundException.class, () -> {
            userProfileService.updateUser(userProfile);  // Should throw exception due to "nonExistingUser"
        });
    }



    @Test
    void testDeleteUser() {
        when(userProfileRepository.findByUsername("testUser")).thenReturn(Optional.of(userProfile));

        boolean isDeleted = userProfileService.deleteUser("testUser");

        assertTrue(isDeleted);
        verify(userProfileRepository, times(1)).delete(Mockito.any(UserProfile.class));
    }

    @Test
    void testDeleteUserNotFound() {
        when(userProfileRepository.findByUsername("nonExistingUser")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            userProfileService.deleteUser("nonExistingUser");
        });
    }

    @Test
    void testGetUser() {
        when(userProfileRepository.findByUsername("testUser")).thenReturn(Optional.of(userProfile));

        UserProfile retrievedUser = userProfileService.getUser("testUser");

        assertNotNull(retrievedUser);
        assertEquals("testUser", retrievedUser.getUsername());
    }

    @Test
    void testGetUserNotFound() {
        when(userProfileRepository.findByUsername("nonExistingUser")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            userProfileService.getUser("nonExistingUser");
        });
    }

    @Test
    void testSendObjectToTopic() {
        CompletableFuture<SendResult<String, Object>> future = CompletableFuture.completedFuture(new SendResult<>(null, null));

        // Mocking kafkaTemplate.send() to return the completed future
        when(kafkaTemplate.send(Mockito.anyString(), Mockito.any())).thenReturn(future);

        // Call the method under test
        userProfileService.sendObjectToTopic(new Users("testUser", "password", "example@gmail.com","USER"));

        // Verify that kafkaTemplate.send was called exactly once
        verify(kafkaTemplate, times(1)).send(Mockito.anyString(), Mockito.any());
    }
}
