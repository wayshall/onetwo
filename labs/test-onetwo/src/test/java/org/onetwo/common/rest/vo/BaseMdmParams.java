package org.onetwo.common.rest.vo;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

public class BaseMdmParams implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6086024604175338752L;
	private String service;
	private String userkey;
	/*****
	 * 景点KEY
	 */
	@NotBlank
	private String bukey;
	@NotBlank
	private String alipaykey;
	/******
	 * 订单编号
	 */
	@NotBlank
	private String itemordersno;
	private String rd;

	public String getUserkey() {
		return userkey;
	}

	public void setUserkey(String userkey) {
		this.userkey = userkey;
	}

	public String getBukey() {
		return bukey;
	}

	public void setBukey(String bukey) {
		this.bukey = bukey;
	}

	public String getAlipaykey() {
		return alipaykey;
	}

	public void setAlipaykey(String alipaykey) {
		this.alipaykey = alipaykey;
	}

	public String getItemordersno() {
		return itemordersno;
	}

	public void setItemordersno(String itemordersno) {
		this.itemordersno = itemordersno;
	}

	public String getRd() {
		return rd;
	}

	public void setRd(String rd) {
		this.rd = rd;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

}
