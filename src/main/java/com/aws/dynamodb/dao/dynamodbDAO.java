package com.aws.dynamodb.dao;

import java.util.List;

import com.aws.dynamodb.entity.users;

public interface dynamodbDAO {

	public List<users> getAllUsers();
	public users getUserDetail(String user_id);
	public boolean createUser(String name, String ign, String email);
	public boolean deleteUser(String user_id);
	public users getUserByEmail(String email);
	public List<users> getAllUsersWithPagination(String lastUserSent, int count);
}
