package com.ibm.omsalertdashboard.service;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//import com.ibm.omsalertdashboard.model.User;

//service class to match username with actual username
@Service 
public class CustomUserDetailsService implements UserDetailsService{

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if(username.equals("Shivani")) {
			return new User("Shivani", "shivani123", new ArrayList<>());
		}
		else {
			throw new UsernameNotFoundException("User Not Found!!!");
		}
		
	}

	
}
