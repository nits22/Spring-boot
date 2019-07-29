package com.example.sample.controller;

import com.example.sample.dto.User;
import com.example.sample.library.Utility;
import com.example.sample.service.PasswordService;
import com.example.sample.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private PasswordService PasswordService;
	
	@Autowired
	private UserService userService;

	public UserController() {
	}

	@RequestMapping(method=RequestMethod.POST, value="/addUser")
	public ResponseEntity addUser(@RequestBody @Valid User user) {
		user = userService.addUser(user);
		if(user != null)
			return new ResponseEntity(Utility.successResponse(user, "User created"), HttpStatus.OK);
		else
			return new ResponseEntity(Utility.failureResponse("User already exist with email id"), HttpStatus.OK);


	}

}
