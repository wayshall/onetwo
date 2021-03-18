package org.onetwo.common.jackson;


import java.io.Serializable;
import java.util.List;

import org.onetwo.common.jackson.serializer.UrlJsonSerializer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class StoreDetailResponse implements Serializable {
    private Long storeId;
    private String storeName;
    private String logoUrl;
    private Double distance;
    private String address;
    private Double longitude;
    private Double latitude;
    private String businessHours;
    private String intro;
    private String detail;
    private String advertisementUrl;
    private Double grade;

    @JsonSerialize(using = UrlJsonSerializer.class)
    String[] slidePictureUrls;

    @JsonSerialize(using = UrlJsonSerializer.class)
    List<String> errorWithList;

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getBusinessHours() {
		return businessHours;
	}

	public void setBusinessHours(String businessHours) {
		this.businessHours = businessHours;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getAdvertisementUrl() {
		return advertisementUrl;
	}

	public void setAdvertisementUrl(String advertisementUrl) {
		this.advertisementUrl = advertisementUrl;
	}

	public Double getGrade() {
		return grade;
	}

	public void setGrade(Double grade) {
		this.grade = grade;
	}

	public String[] getSlidePictureUrls() {
		return slidePictureUrls;
	}

	public void setSlidePictureUrls(String[] slidePictureUrls) {
		this.slidePictureUrls = slidePictureUrls;
	}

	public List<String> getErrorWithList() {
		return errorWithList;
	}

	public void setErrorWithList(List<String> errorWithList) {
		this.errorWithList = errorWithList;
	}

    
}