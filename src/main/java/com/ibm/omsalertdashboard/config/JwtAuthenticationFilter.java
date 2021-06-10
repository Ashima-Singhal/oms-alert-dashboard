package com.ibm.omsalertdashboard.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ibm.omsalertdashboard.service.CustomUserDetailsService;
import com.ibm.omsalertdashboard.util.JwtUtil;

@Component
//class to create JwtAuthentication configuration
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	JwtUtil jwtUtil;
	
	@Autowired
	CustomUserDetailsService customUserDetailsService;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		//get jwt
		String reqTokenHeader = request.getHeader("Authorization");
		String username = null;
		String jwtToken = null;
		
		//checking null and format of token
		if(reqTokenHeader != null && reqTokenHeader.startsWith("Bearer ")) {
			jwtToken = reqTokenHeader.substring(7);
			try {
				username = this.jwtUtil.getUsernameFromToken(jwtToken);
			}catch(Exception e) {
				e.printStackTrace();
			}
			UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);
			//security
			if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}else {
				System.out.println("Token is not validated!!!");
			}
			
		}
		filterChain.doFilter(request, response);
	}

	
}
