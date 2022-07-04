package com.skillsethub.object;

public class User {

	private int id;
	private String firstName;
	private String lastName;
	private String email;
	private String address;
	private String contact;
	private String password;
	private int roleId;
	
	public User() {

	}

	public User(int id, String firstName, String lastName, String email,String password ,String address,int roleId,String contact) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.address = address;
		this.roleId = roleId;
		this.contact = contact;
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public int getRole() {
		return roleId;
	}

	public void setRole(int role) {
		this.roleId = role;
	} 

}
