package org.onetwo.common.excel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@SuppressWarnings("serial")
@Entity
@Table(name="ZJK_CARD")
@SequenceGenerator(name="CardEntityGenerator", sequenceName="SEQ_ZJK_CARD")
public class CardEntity {
	
	/**
	 * 1已入库，2正在出库中，3正在发卡，4已发卡，5已出售，49未激活，50已激活，-1已失效，-2已挂失
	 * @author mrs
	 */
	public static class State {
		public static final Long STORAGE = 1L;
		public static final Long OUTBOUNDING = 2L;
		public static final Long DESPATCHING = 3L;
		public static final Long DESPATHED = 4L;
		public static final Long SALED = 5L;
		public static final Long NONACTIVATED = 49L;
		public static final Long ACTIVATED = 50L;
		public static final Long FAILURED = -1L;
		public static final Long REPORTLOST = -2L;
		
		public static final Map<Long, String> LABLES = new HashMap<Long, String>() {
			{
				put(STORAGE, "已入库");
				put(OUTBOUNDING, "正在出库");
				put(DESPATCHING, "正在发卡");
				put(DESPATHED, "已发卡");
				put(SALED, "已出售");
				put(NONACTIVATED, "未激活");
				put(ACTIVATED, "已激活");
				put(FAILURED, "已失效");
				put(REPORTLOST, "已挂失");
			}
		};
	}
	
	protected Long id;
  
	protected Long type;
  
	protected String cardNo;
  
	protected String cardPwd;
  
	protected String userName;
  
	protected String moblie;
  
	protected String carPlate;
  
	protected Long state;
  
	protected Date saleTime;
	
	protected Date storageTime;
	
	protected Date startTime;
  
	protected Date endTime;
  
	protected Long agentOrganId;
  
	protected String useUserId;
  
	protected CardTypeEntity cardType;
	
	protected List<CardBean> cardBeans;
  
	protected List<String> properties;
	
	public CardEntity(){
	}
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CardEntityGenerator")
	@Column(name="ID")
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="TYPE")
	public Long getType() {
		return this.type;
	}
	
	public void setType(Long type) {
		this.type = type;
	}
	
	@Column(name="CARD_NO")
	public String getCardNo() {
		return this.cardNo;
	}
	
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	
	@Column(name="CARD_PWD")
	public String getCardPwd() {
		return this.cardPwd;
	}
	
	public void setCardPwd(String cardPwd) {
		this.cardPwd = cardPwd;
	}
	
	@Column(name="USER_NAME")
	public String getUserName() {
		return this.userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Column(name="MOBLIE")
	public String getMoblie() {
		return this.moblie;
	}
	
	public void setMoblie(String moblie) {
		this.moblie = moblie;
	}
	
	@Column(name="CAR_PLATE")
	public String getCarPlate() {
		return this.carPlate;
	}
	
	public void setCarPlate(String carPlate) {
		this.carPlate = carPlate;
	}
	
	@Column(name="STATE")
	public Long getState() {
		return this.state;
	}
	
	public void setState(Long state) {
		this.state = state;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name="START_TIME")
	public Date getStartTime() {
		return this.startTime;
	}
	
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name="END_TIME")
	public Date getEndTime() {
		return this.endTime;
	}
	
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@Column(name="AGENT_ORGAN_ID")
	public Long getAgentOrganId() {
		return this.agentOrganId;
	}
	
	public void setAgentOrganId(Long agentOrganId) {
		this.agentOrganId = agentOrganId;
	}
	
	@Column(name="USE_USER_ID")
	public String getUseUserId() {
		return this.useUserId;
	}
	
	public void setUseUserId(String useUserId) {
		this.useUserId = useUserId;
	}

	@ManyToOne
	@JoinColumn(name="TYPE", referencedColumnName="ID")
	public CardTypeEntity getCardType() {
		return cardType;
	}


	public void setCardType(CardTypeEntity cardType) {
		this.cardType = cardType;
	}

	@Column(name="SALE_TIME")
	public Date getSaleTime() {
		return saleTime;
	}


	public void setSaleTime(Date saleTime) {
		this.saleTime = saleTime;
	}

	@Column(name="STORAGE_TIME")
	public Date getStorageTime() {
		return storageTime;
	}


	public void setStorageTime(Date storageTime) {
		this.storageTime = storageTime;
	}


	public List<CardBean> getCardBeans() {
		return cardBeans;
	}


	public void setCardBeans(List<CardBean> cardBeans) {
		this.cardBeans = cardBeans;
	}
	
	public void addCardBean(CardBean cardBean){
		if(this.cardBeans==null)
			this.cardBeans = new ArrayList<CardBean>();
		this.cardBeans.add(cardBean);
	}


	public List<String> getProperties() {
		return properties;
	}


	public void setProperties(List<String> properties) {
		this.properties = properties;
	}
	
}
