package com.aws.dynamodb.error;

public class UserNotfoundException extends RuntimeException{

	public UserNotfoundException(String message) {
		super(message);
	}
	
}
