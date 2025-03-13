package com.cardiac.user.exception;


public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String message) {
    	 super("Username already exists");
    }
}
