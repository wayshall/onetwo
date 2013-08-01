package org.onetwo.common.rest.vo;


public class AddMdmParams extends UpdateMdmParams {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5657772185832959327L;
	private String ticketsname;
	private String createdate;
	
	private String returnurl;
	/***
	 * <add key="MDMName" value="测试"/>
	 */
	private String membername;

	public String getTicketsname() {
		return ticketsname;
	}

	public void setTicketsname(String ticketsname) {
		this.ticketsname = ticketsname;
	}

	public String getCreatedate() {
		return createdate;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	public String getReturnurl() {
		return returnurl;
	}

	public void setReturnurl(String returnurl) {
		this.returnurl = returnurl;
	}

	public String getMembername() {
		return membername;
	}

	public void setMembername(String membername) {
		this.membername = membername;
	}

}
