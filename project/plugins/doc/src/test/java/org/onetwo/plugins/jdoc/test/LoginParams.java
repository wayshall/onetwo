package org.onetwo.plugins.jdoc.test;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.onetwo.common.fish.annotation.JFishEntity;

/******
 * @class LoginParams
 * 登录参数
 * 
 *
 */
@JFishEntity(table="t_user")
public class LoginParams implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3803522294920469153L;
	
	/****
	 * @field member_type - Integer - true - 会员类型
	 */
	@NotNull
	private Integer member_type;
	/****
	 * @field user_code - String - true - 会员代码
	 */
	@NotBlank
	private String user_code;
	/****
	 * @field login_code - String - true - 登录代码
	 */
	private String login_code;
	private String password;
	private String user_ip;
	private String user_agent;
	private String nguid;
	private String token;
	private Long user_id;
	private String nick_name;
	private String mobile;
	private String email;
	private String real_name;
	private String previous_login_time;
	private String previous_login_ip;
	
	public LoginParams() {
		// TODO Auto-generated constructor stub
	}

	public Integer getMember_type() {
		return member_type;
	}

	public void setMember_type(Integer member_type) {
		this.member_type = member_type;
	}

	public String getLogin_code() {
		return login_code;
	}

	public void setLogin_code(String login_code) {
		this.login_code = login_code;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUser_ip() {
		return user_ip;
	}

	public void setUser_ip(String user_ip) {
		this.user_ip = user_ip;
	}

	public String getUser_agent() {
		return user_agent;
	}

	public void setUser_agent(String user_agent) {
		this.user_agent = user_agent;
	}

	public String getNguid() {
		return nguid;
	}

	public void setNguid(String nguid) {
		this.nguid = nguid;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getReal_name() {
		return real_name;
	}

	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}

	public String getPrevious_login_time() {
		return previous_login_time;
	}

	public void setPrevious_login_time(String previous_login_time) {
		this.previous_login_time = previous_login_time;
	}

	public String getPrevious_login_ip() {
		return previous_login_ip;
	}

	public void setPrevious_login_ip(String previous_login_ip) {
		this.previous_login_ip = previous_login_ip;
	}

	public String getUser_code() {
		return user_code;
	}

	public void setUser_code(String user_code) {
		this.user_code = user_code;
	}
}
