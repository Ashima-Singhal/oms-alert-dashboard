package com.ibm.omsalertdashboard.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ibm.omsalertdashboard.model.JwtRequest;
import com.ibm.omsalertdashboard.repositoryImpl.JwtRequestRepositoryImpl;


//service class to match username with actual username
@Service 
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	JwtRequestRepositoryImpl jwtRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		JwtRequest obj = jwtRepo.findByUsername(username);
		
		if(obj != null) {
			return new User(obj.getUserName(), obj.getPassword(), new ArrayList<>()); 
		}
		else {
			throw new UsernameNotFoundException("User Not Found!!!");
		}
		
		
	}

	
}
