package com.example.sample.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ChangePasswordBody {

	@NotNull
	String emailId;
	@NotNull
	String oldPassword;
	@NotNull
	String newPassword;

	public ChangePasswordBody(){

	}

	public ChangePasswordBody(String emailId, String oldPassword, String newPassword){
		this.emailId = emailId;
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
	}

	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
