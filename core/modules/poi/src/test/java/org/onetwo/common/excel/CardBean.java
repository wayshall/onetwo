package org.onetwo.common.excel;

import java.util.List;

public class CardBean {
	private int cardNo;
	private String name;
	private String password;
	
	private List<CardBean> beans;

	public int getCardNo() {
		return cardNo;
	}

	public void setCardNo(int cardNo) {
		this.cardNo = cardNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<CardBean> getBeans() {
		return beans;
	}

	public void setBeans(List<CardBean> beans) {
		this.beans = beans;
	}


}
