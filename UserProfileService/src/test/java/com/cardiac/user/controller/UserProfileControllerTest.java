package com.cardiac.user.controller;

import com.cardiac.user.model.RegistrationData;
import com.cardiac.user.model.UserProfile;
import com.cardiac.user.service.UserProfileService;

import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserProfileControllerTest {

    @Mock
    private UserProfileService userProfileService;

    @InjectMocks
    private UserProfileController userProfileController;

    private MockMvc mockMvc;

    private UserProfile userProfile;

    @BeforeEach
    void setUp() {
        // Set up the mock MVC
        mockMvc = MockMvcBuilders.standaloneSetup(userProfileController).build();

        // Initialize a UserProfile object to be used in tests
        userProfile = new UserProfile();
        userProfile.setUsername("testUser");
        userProfile.setEmail("test@domain.com");
        userProfile.setFirstName("Test");
        userProfile.setLastName("User");
    }

    @Test
    void testRegisterUser() throws Exception {
        // Mock the service call
        when(userProfileService.registerUser(any(UserProfile.class))).thenReturn(userProfile);

        // Create a RegistrationData object (this would come from the request body)
        RegistrationData registrationData = new RegistrationData();
        registrationData.setUsername("testUser");
        registrationData.setEmail("test@domain.com");
        registrationData.setFirstName("Test");
        registrationData.setLastName("User");

        // Perform the HTTP POST request
        mockMvc.perform(post("/user/add")
                        .contentType("application/json")
                        .content("{ \"username\": \"testUser\", \"email\": \"test@domain.com\", \"firstName\": \"Test\", \"lastName\": \"User\" }"))
                .andExpect(status().isOk()) // Expecting a 200 OK status
                .andExpect(jsonPath("$.username").value("testUser")) // Verifying the response body
                .andExpect(jsonPath("$.email").value("test@domain.com"));

        verify(userProfileService, times(1)).registerUser(any(UserProfile.class)); // Verify that the service method was called
    }

    @Test
    void testUpdateUser() throws Exception {
        // Mock the service call
        when(userProfileService.updateUser(any(UserProfile.class))).thenReturn(userProfile);

        // Perform the HTTP PUT request
        mockMvc.perform(put("/user/update")
                        .contentType("application/json")
                        .content("{ \"username\": \"testUser\", \"email\": \"test@domain.com\", \"firstName\": \"Test\", \"lastName\": \"User\" }"))
                .andExpect(status().isOk()) // Expecting a 200 OK status
                .andExpect(jsonPath("$.username").value("testUser"))
                .andExpect(jsonPath("$.email").value("test@domain.com"));

        verify(userProfileService, times(1)).updateUser(any(UserProfile.class)); // Verify service method call
    }

    @Test
    void testDeleteUser() throws Exception {
        // Mock the service call
        when(userProfileService.deleteUser(anyString())).thenReturn(true);

        // Perform the HTTP DELETE request
        mockMvc.perform(delete("/user/delete/testUser"))
                .andExpect(status().isOk()) // Expecting a 200 OK status
                .andExpect(content().string("User is deleted")); // Verifying the response body

        verify(userProfileService, times(1)).deleteUser(anyString()); // Verify that delete method was called
    }

    @Test
    void testGetUser() throws Exception {
        // Mock the service call
        when(userProfileService.getUser(anyString())).thenReturn(userProfile);

        // Perform the HTTP GET request
        mockMvc.perform(get("/user/get/testUser"))
                .andExpect(status().isOk()) // Expecting a 200 OK status
                .andExpect(jsonPath("$.username").value("testUser")) // Verify the response body
                .andExpect(jsonPath("$.email").value("test@domain.com"));

        verify(userProfileService, times(1)).getUser(anyString()); // Verify service method call
    }
}

    //
