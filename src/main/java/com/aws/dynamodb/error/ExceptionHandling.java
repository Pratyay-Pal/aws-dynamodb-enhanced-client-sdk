package com.aws.dynamodb.error;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandling {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> notValid(MethodArgumentNotValidException ex, HttpServletRequest request){
		List<String> errors = new ArrayList<>();
		ex.getAllErrors().forEach(error-> errors.add(error.getDefaultMessage()));
		
		Map<String,List<String>> result = new HashMap<String,List<String>>();
		result.put("ERRORS", errors);
		
		return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(UserNotfoundException.class)
	public ResponseEntity<ErrorResponse> userNotFound(Exception ex, WebRequest webRequest){
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorMessage(ex.getMessage());
		return new ResponseEntity<ErrorResponse>(errorResponse,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(UserCreationFailedException.class)
	public ResponseEntity<ErrorResponse> userCreationFailed(Exception ex, WebRequest webRequest){
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorMessage(ex.getMessage());
		return new ResponseEntity<ErrorResponse>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(UserDeletionFailedException.class)
	public ResponseEntity<ErrorResponse> userDeletionFailed(Exception ex, WebRequest webRequest){
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorMessage(ex.getMessage());
		return new ResponseEntity<ErrorResponse>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
