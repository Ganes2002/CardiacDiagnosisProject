package com.cardiac.authenticate.service;

import com.cardiac.authenticate.model.ChangePassword;
import com.cardiac.authenticate.model.User;

public interface AuthenticateService {
	
	public User getUserDetails(String username);
	public User addUserDetails(User user);
	public User changeUserPassword(ChangePassword changePassword);
	public String getTemporaryPassword(String username);
	public boolean deleteUserByUsername(String username);
	String getUserEmail(String username);
//	boolean handleForgotPassword(String username);

}
