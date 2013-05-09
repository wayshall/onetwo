package org.onetwo.common.excel;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="ZJK_CARD_TYPE")
@SequenceGenerator(name="CardTypeEntityGenerator", sequenceName="SEQ_ZJK_CARD_TYPE")
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
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CardTypeEntityGenerator")
	@Column(name="ID")
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="NAME")
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="VALIDITY")
	public Long getValidity() {
		return this.validity;
	}
	
	public void setValidity(Long validity) {
		this.validity = validity;
	}
	
	@Column(name="RETAIL_PRICE")
	public Long getRetailPrice() {
		return this.retailPrice;
	}
	
	public void setRetailPrice(Long retailPrice) {
		this.retailPrice = retailPrice;
	}
	
	@Column(name="SETTLEMENT_PRICE")
	public BigDecimal getSettlementPrice() {
		return this.settlementPrice;
	}
	
	public void setSettlementPrice(BigDecimal settlementPrice) {
		this.settlementPrice = settlementPrice;
	}
	
	@Column(name="IS_AGENT_SALE")
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
