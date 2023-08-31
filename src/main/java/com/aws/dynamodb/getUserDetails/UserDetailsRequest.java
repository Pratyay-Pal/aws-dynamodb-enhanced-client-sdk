package com.aws.dynamodb.getUserDetails;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public class UserDetailsRequest {

	@Valid
	@NotBlank(message="Please give User_id")
	private String user_id;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
}
