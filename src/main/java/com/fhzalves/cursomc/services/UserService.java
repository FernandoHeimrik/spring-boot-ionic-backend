package com.fhzalves.cursomc.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.fhzalves.cursomc.security.UserSS;

public class UserService {
	
	public static  UserSS authenticated() {
		try {
		return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}catch(Exception e) {
			return null;
		}
	}
}
