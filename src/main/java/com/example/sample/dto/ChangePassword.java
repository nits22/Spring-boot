package com.example.sample.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePassword {

	String message;
	String token;

	public ChangePassword(String message, String token){
		this.message = message;
		this.token = token;
	}

}
