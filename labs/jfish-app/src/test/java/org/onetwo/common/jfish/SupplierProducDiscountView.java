package org.onetwo.common.jfish;

import org.onetwo.common.fish.annotation.JFishQueryable;

@JFishQueryable(table="v_supplier_produc_discount")
public class SupplierProducDiscountView {
	
	private double cardFree;
	private double price;
	private String logoRsurl;
	private String name;
	private String feature;
	private double notEnoughOwnerDiscount;
	private double adultDiscount;
	
	
	public double getCardFree() {
		return cardFree;
	}
	public void setCardFree(double cardFree) {
		this.cardFree = cardFree;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getLogoRsurl() {
		return logoRsurl;
	}
	public void setLogoRsurl(String logoRsurl) {
		this.logoRsurl = logoRsurl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFeature() {
		return feature;
	}
	public void setFeature(String feature) {
		this.feature = feature;
	}
	public double getNotEnoughOwnerDiscount() {
		return notEnoughOwnerDiscount;
	}
	public void setNotEnoughOwnerDiscount(double notEnoughOwnerDiscount) {
		this.notEnoughOwnerDiscount = notEnoughOwnerDiscount;
	}
	public double getAdultDiscount() {
		return adultDiscount;
	}
	public void setAdultDiscount(double adultDiscount) {
		this.adultDiscount = adultDiscount;
	}
	
	
	
	
}
