package org.onetwo.plugins.batch;

import java.io.Serializable;

@SuppressWarnings("serial")
public class BoxNumberEntity implements Serializable {

	private Long id;
	private String icLno;
	private String bbox; // 箱号  5-4
	private String box; // 盒号   5-4-3
	private String status;
	private String certCode;
	private String phone;
	private String name;
	private String address;
	private String sexy;
	private String certExpBcd;
	private String dataBatch;
	private String countryName;
	private String townName;
	private String villageName;
	private String countryCode;
	private String townCode;
	private String villageCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIcLno() {
		return icLno;
	}

	public void setIcLno(String icLno) {
		this.icLno = icLno;
	}

	public String getBbox() {
		return bbox;
	}

	public void setBbox(String bbox) {
		this.bbox = bbox;
	}

	public String getBox() {
		return box;
	}

	public void setBox(String box) {
		this.box = box;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCertCode() {
		return certCode;
	}

	public void setCertCode(String certCode) {
		this.certCode = certCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSexy() {
		return sexy;
	}

	public void setSexy(String sexy) {
		this.sexy = sexy;
	}

	public String getCertExpBcd() {
		return certExpBcd;
	}

	public void setCertExpBcd(String certExpBcd) {
		this.certExpBcd = certExpBcd;
	}

	public String getDataBatch() {
		return dataBatch;
	}

	public void setDataBatch(String dataBatch) {
		this.dataBatch = dataBatch;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getTownName() {
		return townName;
	}

	public void setTownName(String townName) {
		this.townName = townName;
	}
	public String getVillageName() {
		return villageName;
	}

	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}
	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getTownCode() {
		return townCode;
	}

	public void setTownCode(String townCode) {
		this.townCode = townCode;
	}
	public String getVillageCode() {
		return villageCode;
	}

	public void setVillageCode(String villageCode) {
		this.villageCode = villageCode;
	}
    
	
	
	
}
