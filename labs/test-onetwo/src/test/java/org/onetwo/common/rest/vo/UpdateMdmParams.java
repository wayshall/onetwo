package org.onetwo.common.rest.vo;


public class UpdateMdmParams extends BaseMdmParams {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6803167143014532070L;
	/********
	 * 预订入园时间（为空时不更新）
	 */
	private String orderdate;
	/*******
	 * 联系人（为空时不更新）
	 */
	private String customername;

	/*********
	 * 全票数量（为空时不更新）
	 */
	private String quantity;
	/********
	 * 半票数量（为空时不更新）
	 */
	private String childqty;
	private String contactphone;

	public String getContactphone() {
		return contactphone;
	}

	public void setContactphone(String contactphone) {
		this.contactphone = contactphone;
	}
	public String getOrderdate() {
		return orderdate;
	}

	public void setOrderdate(String orderdate) {
		this.orderdate = orderdate;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getChildqty() {
		return childqty;
	}

	public void setChildqty(String childqty) {
		this.childqty = childqty;
	}

}
