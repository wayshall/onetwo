package org.onetwo.ext.poi.excel;

import java.math.BigDecimal;

@SuppressWarnings("serial")
public class CardTypeEntity {
	
	protected Long id;
  
	protected String name;
  
	protected Long validity;
  
	protected Long retailPrice;
  
	protected BigDecimal settlementPrice;
  
	protected Long isAgentSale;
  
	protected CardBean cardBean;
  
	
	public CardTypeEntity(){
	}
	
	
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Long getValidity() {
		return this.validity;
	}
	
	public void setValidity(Long validity) {
		this.validity = validity;
	}
	
	public Long getRetailPrice() {
		return this.retailPrice;
	}
	
	public void setRetailPrice(Long retailPrice) {
		this.retailPrice = retailPrice;
	}
	
	public BigDecimal getSettlementPrice() {
		return this.settlementPrice;
	}
	
	public void setSettlementPrice(BigDecimal settlementPrice) {
		this.settlementPrice = settlementPrice;
	}
	
	public Long getIsAgentSale() {
		return this.isAgentSale;
	}
	
	public void setIsAgentSale(Long isAgentSale) {
		this.isAgentSale = isAgentSale;
	}


	public CardBean getCardBean() {
		return cardBean;
	}


	public void setCardBean(CardBean cardBean) {
		this.cardBean = cardBean;
	}
	
}
