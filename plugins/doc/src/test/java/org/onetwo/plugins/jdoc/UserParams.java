package org.onetwo.plugins.jdoc;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class UserParams {
	
	/**
	 * 会员Id
	 */
	@NotNull
	private Long id;
	/***
	 * 会员类型(1游客，2旅行社)
	 */
	@NotNull
	private Integer mem_type;
	
	/***
	 * 会员登录名/昵称
	 */
	private String nick_name;
	
	/**
	 * 邮箱
	 */
	@NotBlank
	private String email;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getMem_type() {
		return mem_type;
	}

	public void setMem_type(int mem_type) {
		this.mem_type = mem_type;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	

}
