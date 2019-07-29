package com.example.sample.service;

import com.example.sample.dto.ChangePasswordBody;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {
	
	public boolean changePassword(ChangePasswordBody changeBody) {
		return true;
	}

	public boolean validateNewPassword(ChangePasswordBody changeBody) {

		return true;
	}

}
