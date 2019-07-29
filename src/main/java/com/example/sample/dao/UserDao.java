package com.example.sample.dao;

import com.example.sample.dto.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserDao {
	
	private static Map<String,User> users = new HashMap<>();
	
	public static Map<String,User> getUsers() {
		return users;
	}
	

}
