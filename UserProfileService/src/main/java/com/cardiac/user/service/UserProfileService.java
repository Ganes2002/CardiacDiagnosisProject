package com.cardiac.user.service;

import com.cardiac.user.model.Users;
import com.cardiac.user.model.UserProfile;

public interface UserProfileService {
	
	public UserProfile registerUser(UserProfile userProfile);
	public UserProfile updateUser(UserProfile userProfile);
	public boolean deleteUser(String  username);
	public UserProfile getUser(String username);
	public void sendObjectToTopic(Users userAuth);

}
