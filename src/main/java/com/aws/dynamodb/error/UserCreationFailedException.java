package com.aws.dynamodb.error;

public class UserCreationFailedException extends RuntimeException{

	public UserCreationFailedException(String message) {
		super(message);
	}
	
}
