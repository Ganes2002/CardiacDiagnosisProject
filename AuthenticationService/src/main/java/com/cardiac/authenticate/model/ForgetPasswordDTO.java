package com.cardiac.authenticate.model;

//import jakarta.validation.constraints.NotBlank;

public class ForgetPasswordDTO {
	
//	    @NotBlank(message = "Username cannot be empty")
	    private String username;
//	    @NotBlank(message = "Email cannot be empty")
//	    @Email(message = "Please provide a valid email")
	    private String email;
	    public ForgetPasswordDTO() {
	    }
	    public ForgetPasswordDTO(String username, String email) {
	        this.username = username;
	        this.email = email;
	    }
	    public String getUsername() {
	        return username;
	    }
	    public void setUsername(String username) {
	        this.username = username;
	    }
	    public String getEmail() {
	        return email;
	    }
	    public void setEmail(String email) {
	        this.email = email;
	    }
	    @Override
	    public String toString() {
	        return "ForgotPasswordDTO [username=" + username + ", email=" + email + "]";
	    }
}
