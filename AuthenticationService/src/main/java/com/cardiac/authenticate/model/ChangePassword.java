package com.cardiac.authenticate.model;

public class ChangePassword {
	
	private String username;
	private String oldPassword;
	private String newPassword;
	
	public ChangePassword() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ChangePassword(String username, String oldPassword, String newPassword) {
		super();
		this.username = username;
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	@Override
	public String toString() {
		return "ChangePassword [username=" + username + ", oldPassword=" + oldPassword + ", newPassword=" + newPassword
				+ "]";
	}
	

}
