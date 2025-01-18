package org.unibl.etf.emobility_hub_user.models.entity;

import java.io.Serializable;
import java.util.Objects;

public class ClientEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private RoleEnum role;
	private String email;
	private String phoneNumber;
	private String idCardNumber;
	private String avatarImage;

	private boolean isDeactivated;
	private boolean isBlocked;

	public ClientEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ClientEntity(Long id, String username, String password, String firstName, String lastName, RoleEnum role,
			String email, String phoneNumber, String idCardNumber, String avatarImage, boolean isDeactivated,
			boolean isBlocked) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.idCardNumber = idCardNumber;

		this.role = role;
		this.avatarImage = avatarImage;
		this.isDeactivated = isDeactivated;
		this.isBlocked = isBlocked;
	}

	public ClientEntity(String username, String password, String firstName, String lastName, String email,
			String phoneNumber, String idCardNumber) {
		super();
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.idCardNumber = idCardNumber;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public RoleEnum getRole() {
		return role;
	}

	public void setRole(RoleEnum role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getIdCardNumber() {
		return idCardNumber;
	}

	public void setIdCardNumber(String idCardNumber) {
		this.idCardNumber = idCardNumber;
	}

	public String getAvatarImage() {
		return avatarImage;
	}

	public void setAvatarImage(String avatarImage) {
		this.avatarImage = avatarImage;
	}

	public boolean isDeactivated() {
		return isDeactivated;
	}

	public void setDeactivated(boolean isDeactivated) {
		this.isDeactivated = isDeactivated;
	}

	public boolean isBlocked() {
		return isBlocked;
	}

	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	@Override
	public int hashCode() {
		return Objects.hash(avatarImage, email, firstName, id, idCardNumber, isBlocked, isDeactivated, lastName,
				password, phoneNumber, role, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientEntity other = (ClientEntity) obj;
		return Objects.equals(avatarImage, other.avatarImage) && Objects.equals(email, other.email)
				&& Objects.equals(firstName, other.firstName) && Objects.equals(id, other.id)
				&& Objects.equals(idCardNumber, other.idCardNumber) && isBlocked == other.isBlocked
				&& isDeactivated == other.isDeactivated && Objects.equals(lastName, other.lastName)
				&& Objects.equals(password, other.password) && Objects.equals(phoneNumber, other.phoneNumber)
				&& role == other.role && Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "ClientEntity [id=" + id + ", username=" + username + ", password=" + password + ", firstName="
				+ firstName + ", lastName=" + lastName + ", role=" + role + ", email=" + email + ", phoneNumber="
				+ phoneNumber + ", idCardNumber=" + idCardNumber + ", avatarImage=" + avatarImage + ", isDeactivated="
				+ isDeactivated + ", isBlocked=" + isBlocked + "]";
	}

}
