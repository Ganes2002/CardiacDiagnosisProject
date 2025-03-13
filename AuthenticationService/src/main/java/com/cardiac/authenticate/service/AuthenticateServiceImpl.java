package com.cardiac.authenticate.service;

import java.security.SecureRandom;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cardiac.authenticate.model.ChangePassword;
import com.cardiac.authenticate.model.User;
import com.cardiac.authenticate.repository.AuthenticateRepository;

@Service
public class AuthenticateServiceImpl implements AuthenticateService {

	@Autowired
	AuthenticateRepository repo;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@Autowired
	EmailService emailService;

	@Override
	public User getUserDetails(String username) {

		Optional<User> user = repo.getByUsername(username);
		if (user.isPresent()) {
			return user.get();
		} else {
			return null;
		}

	}

	@Override
	public User addUserDetails(User user) {

		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);

		User userObject = repo.save(user);
		return userObject;

	}

	@Override
	public User changeUserPassword(ChangePassword changePassword) {

		Optional<User> optional = repo.getByUsername(changePassword.getUsername());

		if (!optional.isPresent()) {
			return null;
		}

		User user = optional.get();

		if (passwordEncoder.matches(changePassword.getOldPassword(), user.getPassword())) {
			user.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
			return repo.save(user);
		} else {
			return null;
		}

	}

//	@Override
//	public boolean getTemporaryPassword(String username) {
//
//		Optional<User> optional = repo.getByUsername(username);
//
//		if (optional != null) {
//			User user = optional.get();
//			user.setPassword(passwordEncoder.encode("Temp@123"));
//			repo.save(user);
//			return true;
//		}
//
//		else {
//			return false;
//		}
//	}

	@Override
	public boolean deleteUserByUsername(String username) {
		Optional<User> userOptional = repo.findByUsername(username); // Use findByUsername()
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			repo.delete(user); // Delete the user from the repository
			return true; // User successfully deleted
		} else {
			return false; // User not found
		}
	}
//	public boolean handleForgotPassword(ForgetPasswordDTO forgotPasswordDTO) {
//        Optional<User> optional = repo.getByUsername(forgotPasswordDTO.getUsername());
//        if (!optional.isPresent()) {
//            return false;         }
//        User users = optional.get();
//                String tempPassword = "Temp@123";
//        users.setPassword(passwordEncoder.encode(tempPassword));
//        repo.save(users);
//        emailService.sendTemporaryPassword(forgotPasswordDTO.getEmail(), tempPassword);
//        return true;
//    }
	
	@Override
	public String getUserEmail(String username) {
		Optional<User> userOptional = repo.findByUsername(username);
		User user = userOptional.get();
		return user.getEmail();
	}

	@Override
	public String getTemporaryPassword(String username) {
	    Optional<User> optional = repo.getByUsername(username);
	    String returnPassword = null;
	    if (optional.isPresent()) {
	        User user = optional.get();
	        String tempPassword = generateRandomPassword();
	        returnPassword = tempPassword;
	        user.setPassword(passwordEncoder.encode(tempPassword));
	        repo.save(user);
	        String subject = "Your temporary Password";
	        String body = "Your temporary password is: " + tempPassword;
	        emailService.sendEmail(user.getEmail(), subject, body);
	        return returnPassword;
	    } else {
	        return returnPassword;
	    }
	}
	private String generateRandomPassword() {
	    String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	    String lowerCase = "abcdefghijklmnopqrstuvwxyz";
	    String digits = "0123456789";
	    String specialChars = "!@#$%^&*()-_+=<>?";
	    String allCharacters = upperCase + lowerCase + digits + specialChars;
	    int passwordLength = 12;  
	    SecureRandom random = new SecureRandom();
	    StringBuilder password = new StringBuilder();
	    for (int i = 0; i < passwordLength; i++) {
	        int index = random.nextInt(allCharacters.length());
	        password.append(allCharacters.charAt(index));
	    }
//	    System.err.println(password.toString());
	    return password.toString();
	}

	
}

