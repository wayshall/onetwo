package org.example.model.member.vo;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

public class LoginParams implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3972449797244481952L;
	
	@NotBlank
	private String userName;
	
	@NotBlank
	private String userPassword;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	
	

}
