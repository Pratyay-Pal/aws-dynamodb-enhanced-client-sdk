package com.aws.dynamodb.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aws.dynamodb.dao.dynamodbDAO;
import com.aws.dynamodb.deleteUser.DeleteUserRequest;
import com.aws.dynamodb.deleteUser.DeleteUserResponse;
import com.aws.dynamodb.entity.users;
import com.aws.dynamodb.error.UserCreationFailedException;
import com.aws.dynamodb.error.UserDeletionFailedException;
import com.aws.dynamodb.error.UserNotfoundException;
import com.aws.dynamodb.getAllUsers.allUsersResponse;
import com.aws.dynamodb.getUserByEmail.UserByEmailRequest;
import com.aws.dynamodb.getUserByEmail.UserByEmailResponse;
import com.aws.dynamodb.getUserDetails.UserDetailsRequest;
import com.aws.dynamodb.getUserDetails.UserDetailsResponse;
import com.aws.dynamodb.newUser.NewUserRequest;
import com.aws.dynamodb.newUser.NewUserResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/")
public class AllController {

	@Autowired
	dynamodbDAO dyDB;
	
	private Logger logger = LogManager.getLogger(AllController.class);
	
	@GetMapping("/allUsers")
	public ResponseEntity<?> getAllUsers() {
		logger.info("allUsers Request Received");
		List<users> userList = dyDB.getAllUsers();
		allUsersResponse response = new allUsersResponse();
		response.setUserList(userList);
		return new ResponseEntity<allUsersResponse>(response,HttpStatus.OK);
	}
	
	@GetMapping("/allUsersWithPagination")
	public ResponseEntity<?> getAllUsers(@RequestParam String lastUser, @RequestParam int count) {
		logger.info("allUsersWithPagination Request Received");
		List<users> userList = dyDB.getAllUsersWithPagination(lastUser, count);
		allUsersResponse response = new allUsersResponse();
		response.setUserList(userList);
		return new ResponseEntity<allUsersResponse>(response,HttpStatus.OK);
	}
	
	@GetMapping("/getUserDetails")
	public ResponseEntity<?> getUserDetails(@Valid @RequestBody UserDetailsRequest userDetailsRequest){
		logger.info("Info requested for user "+userDetailsRequest.getUser_id());
		users user = dyDB.getUserDetail(userDetailsRequest.getUser_id());
		if(user == null) 
			throw new UserNotfoundException(userDetailsRequest.getUser_id()+" not found");		
		UserDetailsResponse detailsResponse = new UserDetailsResponse();
		detailsResponse.setEmail(user.getEmail());
		detailsResponse.setIgn(user.getIgn());
		detailsResponse.setName(user.getName());
		detailsResponse.setUser_id(user.getUser_id());
		return new ResponseEntity<UserDetailsResponse>(detailsResponse,HttpStatus.OK);
	}
	
	@PostMapping("/createUser")
	public ResponseEntity<?> getUserDetails(@Valid @RequestBody NewUserRequest newUserRequest){
		logger.info("New user creation requested for user with details: "+newUserRequest.toString());
		boolean result = dyDB.createUser(newUserRequest.getName(), newUserRequest.getIgn(), newUserRequest.getEmail());
		if(!result) 
			throw new UserCreationFailedException("Cannot create user "+newUserRequest.getName());		
		NewUserResponse newUserResponse = new NewUserResponse();
		newUserResponse.setResult("SUCCESS");
		return new ResponseEntity<NewUserResponse>(newUserResponse,HttpStatus.OK);
	}
	
	@PostMapping("/deleteUser")
	public ResponseEntity<?> deleteUser(@Valid @RequestBody DeleteUserRequest deleteUserRequest){
		logger.info("User deletion requested for user id: "+deleteUserRequest.getUser_id());
		boolean result = dyDB.deleteUser(deleteUserRequest.getUser_id());
		if(!result) 
			throw new UserDeletionFailedException("Cannot delete user "+deleteUserRequest.getUser_id());		
		DeleteUserResponse deleteUserResponse = new DeleteUserResponse();
		deleteUserResponse.setResult("SUCCESS");
		return new ResponseEntity<DeleteUserResponse>(deleteUserResponse,HttpStatus.OK);
	}
	
	@PostMapping("/userByEmail")
	public ResponseEntity<?> userByEmail(@Valid @RequestBody UserByEmailRequest userByEmailRequest){
		logger.info("User details requested for user email: "+userByEmailRequest.getEmail());
		users user = dyDB.getUserByEmail(userByEmailRequest.getEmail());
		if(user == null) 
			throw new UserNotfoundException(userByEmailRequest.getEmail()+" not found");			
		UserByEmailResponse userByEmailResponse = new UserByEmailResponse();
		userByEmailResponse.setEmail(user.getEmail());
		userByEmailResponse.setIgn(user.getIgn());
		userByEmailResponse.setName(user.getName());
		userByEmailResponse.setUser_id(user.getUser_id());
		return new ResponseEntity<UserByEmailResponse>(userByEmailResponse,HttpStatus.OK);
	}
}
