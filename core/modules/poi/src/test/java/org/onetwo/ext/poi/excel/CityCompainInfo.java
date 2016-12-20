package org.onetwo.ext.poi.excel;

public class CityCompainInfo {

	private Long id;
	private String name;
	private String phone;
	private String fax;
	private int faxType;
	private String address;
	private String postcode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public String getFax0() {
		if(faxType==0)
			return fax + faxType;
		return "";
	}

	public String getFax1() {
		if(faxType==1)
			return fax + faxType;
		return "";
	}

	public String getFax2() {
		if(faxType==2)
			return fax + faxType;
		return "";
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public int getFaxType() {
		return faxType;
	}

	public void setFaxType(int faxType) {
		this.faxType = faxType;
	}
}
