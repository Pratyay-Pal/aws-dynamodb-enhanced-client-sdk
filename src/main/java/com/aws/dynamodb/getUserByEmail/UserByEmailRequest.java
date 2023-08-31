package com.aws.dynamodb.getUserByEmail;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public class UserByEmailRequest {

	@Valid
	@NotBlank(message="Please enter email")
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
