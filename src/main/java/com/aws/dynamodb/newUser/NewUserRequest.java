package com.aws.dynamodb.newUser;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public class NewUserRequest {

	@Valid
	@NotBlank(message="Name cannot be null")	
	private String name;
	@Valid
	@NotBlank(message="IGN cannot be null")
	private String ign;
	@Valid
	@NotBlank(message="Email cannot be null")
	private String email;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIgn() {
		return ign;
	}
	public void setIgn(String ign) {
		this.ign = ign;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return "NewUserRequest [name=" + name + ", ign=" + ign + ", email=" + email + "]";
	}
	
}
