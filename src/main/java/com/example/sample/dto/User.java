package com.example.sample.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

//@JsonIgnoreProperties(value = { "password" })
@Getter
@Setter
public class User {

	private String id;
	@NotNull
	private String email;
	@NotNull
	private String password;
	private Date created;

	public User() {
	}

	public User(String id, String email, String password) {
		this.id=id;
		this.email=email;
		this.created=new Date();
		this.password=password;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	@JsonProperty("token")
	public String getPassword() {
		return password;
	}
	@JsonProperty("password")
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
}
