package com.cardiac.authenticate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cardiac.authenticate.model.User;
import com.cardiac.authenticate.repository.AuthenticateRepository;
import com.cardiac.user.model.Users;

@Service
public class KafkaMessageListener {
	
	@Autowired
	AuthenticateRepository repo;
	
	@Autowired
    BCryptPasswordEncoder passwordEncoder;
	
	Logger log =  LoggerFactory.getLogger(KafkaMessageListener.class);
	
	
	@KafkaListener(topics = "my-itctopic4", groupId = "my-group")
	public void consumeObject(Users userAuth)
	{
		System.out.println(userAuth.toString());
		log.info("Consumer consumed the object"+ userAuth.toString());
		
		User user = new User();
		user.setUsername(userAuth.getUsername());
		user.setPassword(passwordEncoder.encode(userAuth.getPassword()));
		user.setRole(userAuth.getRole());
		user.setEmail(userAuth.getEmail());
		
		User savedObject = repo.save(user);
		
	}
	

}


