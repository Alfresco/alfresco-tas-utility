package org.alfresco.tester.model;

public class UserModel {
	private String username;
	private String password;

	public UserModel(String username, String password) {
		this.setUsername(username);
		this.setPassword(password);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("UserModel [");
		sb.append("Username=").append(getUsername()).append(" Password=").append(getPassword()).append("]");
		return sb.toString();
	}
}
