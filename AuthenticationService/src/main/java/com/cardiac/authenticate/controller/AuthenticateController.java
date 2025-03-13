package com.cardiac.authenticate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.cardiac.authenticate.exception.UsernameNotFoundException;
import com.cardiac.authenticate.model.ChangePassword;
import com.cardiac.authenticate.model.User;
import com.cardiac.authenticate.service.AuthenticateService;
import com.cardiac.authenticate.service.EmailService;
import com.cardiac.authenticate.service.JwtService;

@RestController
@RequestMapping("/authenticate")
public class AuthenticateController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticateController.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AuthenticateService serv;

    @Autowired
    JwtService jwtService;

    @Autowired
    EmailService emailService;

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        logger.info("Received request to add user with username: {}", user.getUsername());
        User userDetails = serv.addUserDetails(user);
        if (userDetails != null) {
            logger.info("User {} added successfully.", user.getUsername());
            return new ResponseEntity<>(userDetails, HttpStatus.OK);
        } else {
            logger.warn("Failed to add user {}", user.getUsername());
            return new ResponseEntity<>("Not Added", HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody User authRequest) throws UsernameNotFoundException {
        logger.info("Login attempt for user: {}", authRequest.getUsername());
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

            if (authenticate.isAuthenticated()) {
                String token = jwtService.generateToken(authRequest.getUsername());
                logger.info("User {} authenticated successfully, returning JWT token.", authRequest.getUsername());
                return ResponseEntity.ok(token);
            } else {
                logger.warn("Invalid credentials for user: {}", authRequest.getUsername());
                return new ResponseEntity<>("Invalid Credentials", HttpStatus.UNAUTHORIZED);
            }

        } catch (BadCredentialsException ex) {
            logger.warn("Bad credentials for user: {}", authRequest.getUsername());
            return new ResponseEntity<>("Invalid Credentials", HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            logger.error("An error occurred during login for user: {}", authRequest.getUsername(), ex);
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/forget/{username}")
    public ResponseEntity<String> forForgetPassword(@PathVariable String username) {
        logger.info("Password reset request received for username: {}", username);
        String email = serv.getUserEmail(username);
        if (email == null || email.isEmpty()) {
            logger.warn("No email found for the username: {}", username);
            return new ResponseEntity<>("No email found for the given username", HttpStatus.NOT_FOUND);
        }
        String body = "Dear User, " + "Here is your temporary password: " + serv.getTemporaryPassword(username)
                + " " + "Please change your password before logging in";
        logger.info("Temporary password sent to email for username: {}", username);
        return ResponseEntity.ok(body);
    }

    @PostMapping("/changepassword")
    public ResponseEntity<?> forChangePassword(@RequestBody ChangePassword changePassword) {
        logger.info("Password change request for user: {}", changePassword.getUsername());
        User updateDetails = serv.changeUserPassword(changePassword);
        if (updateDetails != null) {
            logger.info("Password changed successfully for user: {}", changePassword.getUsername());
            return new ResponseEntity<>(updateDetails, HttpStatus.OK);
        } else {
            logger.warn("Failed to change password for user: {}", changePassword.getUsername());
            return new ResponseEntity<>("Invalid Password", HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping("/remove/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        logger.info("Delete request for user: {}", username);
        boolean isDeleted = serv.deleteUserByUsername(username);
        if (isDeleted) {
            logger.info("User {} deleted successfully.", username);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } else {
            logger.warn("User {} not found for deletion.", username);
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }
}
