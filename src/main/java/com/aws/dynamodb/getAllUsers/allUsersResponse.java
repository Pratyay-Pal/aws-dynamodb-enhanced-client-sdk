package com.aws.dynamodb.getAllUsers;

import java.util.List;

import com.aws.dynamodb.entity.users;

public class allUsersResponse {

	private List<users> userList;

	public List<users> getUserList() {
		return userList;
	}

	public void setUserList(List<users> userList) {
		this.userList = userList;
	}	
}
