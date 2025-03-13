package com.cardiac.authenticate.configuration;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cardiac.authenticate.model.User;
import com.cardiac.authenticate.repository.AuthenticateRepository;


@Service
public class MyUserDetailsService implements UserDetailsService{
	
	@Autowired
	AuthenticateRepository repo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Optional<User> userInfo = repo.getByUsername(username);
		
		
		return userInfo.map(UserInfoUserDetails::new)
				.orElseThrow(()-> new UsernameNotFoundException("user not found " + username));
	}
		

}
