package org.example.model.sell.entity;

import javax.persistence.TemporalType;
import javax.persistence.Temporal;
import java.util.Date;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;


/*****
 * 
 * @Entity
 */
@SuppressWarnings("serial")
@Entity
@Table(name="ISSUE_SELL_PLAN")
@SequenceGenerator(name="SellPlanEntityGenerator", sequenceName="SEQ_ISSUE_SELL_PLAN")
public class SellPlanEntity implements Serializable {
	
	/*****
	 * 
	 */
	protected Long planId;
  
	/*****
	 * 
	 */
	protected String remark;
  
	/*****
	 * 
	 */
	protected String param;
  
	/*****
	 * 
	 */
	protected Long templetNo;
  
	/*****
	 * 
	 */
	protected String beginIcLseq;
  
	/*****
	 * 
	 */
	protected Long length;
  
	/*****
	 * 
	 */
	protected Long lengthBak;
  
	/*****
	 * 
	 */
	protected Long preAddValue;
  
	/*****
	 * 
	 */
	protected Long agentIssueId;
  
	/*****
	 * 
	 */
	protected String providerNo;
  
	/*****
	 * 
	 */
	protected String corpCode;
  
	/*****
	 * 
	 */
	protected String corpCardExp;
  
	/*****
	 * 
	 */
	protected Date issueDate;
  
	/*****
	 * 
	 */
	protected String printType;
  
	/*****
	 * 
	 */
	protected String washType;
  
	/*****
	 * 
	 */
	protected String cityNo;
  
	/*****
	 * 
	 */
	protected String channel;
  
	/*****
	 * 
	 */
	protected Long abnormal;
  
	/*****
	 * 
	 */
	protected String operId;
  
	/*****
	 * 
	 */
	protected Date createTime;
  
	/*****
	 * 
	 */
	protected Date checkTime;
  
	/*****
	 * 
	 */
	protected String checkOperId;
  
	/*****
	 * 
	 */
	protected Long clipId;
  
	/*****
	 * 
	 */
	protected Long preInit;
  
	/*****
	 * 
	 */
	protected String clipBatch;
  
	/*****
	 * 
	 */
	protected String issueStatus;
  
	/*****
	 * 
	 */
	protected Date updTime;
  
	public SellPlanEntity(){
	}
	
	
	/*****
	 * 
	 * @return
	 */
	@Id
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SellPlanEntityGenerator")
	@Column(name="PLAN_ID")
	public Long getPlanId() {
		return this.planId;
	}
	
	public void setPlanId(Long planId) {
		this.planId = planId;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="REMARK")
	public String getRemark() {
		return this.remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="PARAM")
	public String getParam() {
		return this.param;
	}
	
	public void setParam(String param) {
		this.param = param;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="TEMPLET_NO")
	public Long getTempletNo() {
		return this.templetNo;
	}
	
	public void setTempletNo(Long templetNo) {
		this.templetNo = templetNo;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="BEGIN_IC_LSEQ")
	public String getBeginIcLseq() {
		return this.beginIcLseq;
	}
	
	public void setBeginIcLseq(String beginIcLseq) {
		this.beginIcLseq = beginIcLseq;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="LENGTH")
	public Long getLength() {
		return this.length;
	}
	
	public void setLength(Long length) {
		this.length = length;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="LENGTH_BAK")
	public Long getLengthBak() {
		return this.lengthBak;
	}
	
	public void setLengthBak(Long lengthBak) {
		this.lengthBak = lengthBak;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="PRE_ADD_VALUE")
	public Long getPreAddValue() {
		return this.preAddValue;
	}
	
	public void setPreAddValue(Long preAddValue) {
		this.preAddValue = preAddValue;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="AGENT_ISSUE_ID")
	public Long getAgentIssueId() {
		return this.agentIssueId;
	}
	
	public void setAgentIssueId(Long agentIssueId) {
		this.agentIssueId = agentIssueId;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="PROVIDER_NO")
	public String getProviderNo() {
		return this.providerNo;
	}
	
	public void setProviderNo(String providerNo) {
		this.providerNo = providerNo;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="CORP_CODE")
	public String getCorpCode() {
		return this.corpCode;
	}
	
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="CORP_CARD_EXP")
	public String getCorpCardExp() {
		return this.corpCardExp;
	}
	
	public void setCorpCardExp(String corpCardExp) {
		this.corpCardExp = corpCardExp;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="ISSUE_DATE")
	public Date getIssueDate() {
		return this.issueDate;
	}
	
	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="PRINT_TYPE")
	public String getPrintType() {
		return this.printType;
	}
	
	public void setPrintType(String printType) {
		this.printType = printType;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="WASH_TYPE")
	public String getWashType() {
		return this.washType;
	}
	
	public void setWashType(String washType) {
		this.washType = washType;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="CITY_NO")
	public String getCityNo() {
		return this.cityNo;
	}
	
	public void setCityNo(String cityNo) {
		this.cityNo = cityNo;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="CHANNEL")
	public String getChannel() {
		return this.channel;
	}
	
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="ABNORMAL")
	public Long getAbnormal() {
		return this.abnormal;
	}
	
	public void setAbnormal(Long abnormal) {
		this.abnormal = abnormal;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="OPER_ID")
	public String getOperId() {
		return this.operId;
	}
	
	public void setOperId(String operId) {
		this.operId = operId;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_TIME")
	public Date getCreateTime() {
		return this.createTime;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CHECK_TIME")
	public Date getCheckTime() {
		return this.checkTime;
	}
	
	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="CHECK_OPER_ID")
	public String getCheckOperId() {
		return this.checkOperId;
	}
	
	public void setCheckOperId(String checkOperId) {
		this.checkOperId = checkOperId;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="CLIP_ID")
	public Long getClipId() {
		return this.clipId;
	}
	
	public void setClipId(Long clipId) {
		this.clipId = clipId;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="PRE_INIT")
	public Long getPreInit() {
		return this.preInit;
	}
	
	public void setPreInit(Long preInit) {
		this.preInit = preInit;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="CLIP_BATCH")
	public String getClipBatch() {
		return this.clipBatch;
	}
	
	public void setClipBatch(String clipBatch) {
		this.clipBatch = clipBatch;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="ISSUE_STATUS")
	public String getIssueStatus() {
		return this.issueStatus;
	}
	
	public void setIssueStatus(String issueStatus) {
		this.issueStatus = issueStatus;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPD_TIME")
	public Date getUpdTime() {
		return this.updTime;
	}
	
	public void setUpdTime(Date updTime) {
		this.updTime = updTime;
	}
	
}