package org.unibl.etf.emobility_hub_promotions.beans;

import java.io.Serializable;
import java.util.Objects;

public class UserBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private String token;
	private boolean isLoggedIn;

	public UserBean() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	@Override
	public int hashCode() {
		return Objects.hash(isLoggedIn, password, token, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserBean other = (UserBean) obj;
		return isLoggedIn == other.isLoggedIn && Objects.equals(password, other.password)
				&& Objects.equals(token, other.token) && Objects.equals(username, other.username);
	}

}