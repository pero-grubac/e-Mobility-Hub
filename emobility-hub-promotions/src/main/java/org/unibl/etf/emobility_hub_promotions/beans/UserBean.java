package org.unibl.etf.emobility_hub_promotions.beans;

import java.io.Serializable;

// The signed-in manager. The password is never kept; the API token is enough.
public class UserBean implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String MANAGER_ROLE = "ROLE_MANAGER";

	private String username;
	private String role;
	private String token;

	public String getUsername() {
		return username;
	}

	public String getToken() {
		return token;
	}

	public boolean isManager() {
		return MANAGER_ROLE.equals(role) && token != null && !token.isEmpty();
	}

	public void signIn(String username, String role, String token) {
		this.username = username;
		this.role = role;
		this.token = token;
	}
}
