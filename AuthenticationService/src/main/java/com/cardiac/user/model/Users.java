package com.cardiac.user.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

public class Users {
	
	private String username;
	private String password;
	private String email;
	private String role;
	
	public Users() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Users(String username, String password, String email, String role) {
		super();
		this.username = username;
		this.password = password;
		this.email = email;
		this.role = role;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", email=" + email + ", role=" + role + "]";
	}

	

}
