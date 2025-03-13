package com.cardiac.authenticate.configuration;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cardiac.authenticate.model.User;



public class UserInfoUserDetails implements UserDetails {
	
	private String userName;
	private String password;
	private List<SimpleGrantedAuthority> authorites;
	
//	ROLE_ADMIN, ROLE_USER, ROLE_GUEST
	
	public UserInfoUserDetails(User myUsers) {
		userName = myUsers.getUsername();
		password = myUsers.getPassword();
		authorites = 
				Arrays.stream(myUsers.getRole().split(","))
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
//		Collections.singleton(new SimpleGrantedAuthority("USER"));
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorites;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return userName;
	}

}
