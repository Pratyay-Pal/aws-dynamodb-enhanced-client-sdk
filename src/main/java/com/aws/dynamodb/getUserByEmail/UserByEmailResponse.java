package com.aws.dynamodb.getUserByEmail;

public class UserByEmailResponse {

	private String user_id;
	private String email;
	private String ign;
	private String name;
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIgn() {
		return ign;
	}
	public void setIgn(String ign) {
		this.ign = ign;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
