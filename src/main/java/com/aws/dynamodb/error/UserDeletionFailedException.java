package com.aws.dynamodb.error;

public class UserDeletionFailedException extends RuntimeException{

	public UserDeletionFailedException(String message) {
		super(message);
	}
	
}
