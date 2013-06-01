package org.onetwo.common.db;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;



/*****
 * 商家同步日志
 * @Entity
 */
@SuppressWarnings("serial")
@Entity
@Table(name="SYN_LOG_SUPPLIER")
@SequenceGenerator(name="LogSupplierEntityGenerator", sequenceName="SEQ_SYN_LOG_SUPPLIER")
public class LogSupplierEntity extends AbstractBaseEntity<Long> implements TestInterface{
	
	public static class SynTypes {
		public static final long mannual = 1;
		public static final long auto = 2;
	}

	public static class SynState {
		public static final long processing = 0;
		public static final long finished = 1;
	}
	
	protected Long id;
  
	protected String yooyoSupplierId;
  
	protected String supplierCode;
  
	protected String supplierName;
  
	protected Date synStartTime;
  
	protected Date synEndTime;
  
	protected Long newRoute;
  
	protected Long updateRoute;
  
	protected long deleteRoute;
  
	protected Long failRoute;
  
	protected Long state;
  
	protected Long synType;
  
	protected String remark;
  
  
	
	public LogSupplierEntity(){
	}
	
	
	/*****
	 * 
	 * @return
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LogSupplierEntityGenerator")
	@Column(name="ID")
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	/*****
	 * yooyo的供应商ID
	 * @return
	 */
	@Column(name="YOOYO_SUPPLIER_ID")
	public String getYooyoSupplierId() {
		return this.yooyoSupplierId;
	}
	
	public void setYooyoSupplierId(String yooyoSupplierId) {
		this.yooyoSupplierId = yooyoSupplierId;
	}
	
	/*****
	 * 供应商编号
	 * @return
	 */
	@Column(name="SUPPLIER_CODE")
	public String getSupplierCode() {
		return this.supplierCode;
	}
	
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	
	/*****
	 * 供应商名称
	 * @return
	 */
	@Column(name="SUPPLIER_NAME")
	public String getSupplierName() {
		return this.supplierName;
	}
	
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	
	/*****
	 * 同步开始时间
	 * @return
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="SYN_START_TIME")
	public Date getSynStartTime() {
		return this.synStartTime;
	}
	
	public void setSynStartTime(Date synStartTime) {
		this.synStartTime = synStartTime;
	}
	
	/*****
	 * 同步结束时间
	 * @return
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="SYN_END_TIME")
	public Date getSynEndTime() {
		return this.synEndTime;
	}
	
	public void setSynEndTime(Date synEndTime) {
		this.synEndTime = synEndTime;
	}
	
	/*****
	 * 新增线路条数
	 * @return
	 */
	@Column(name="NEW_ROUTE")
	public Long getNewRoute() {
		if(this.newRoute==null)
			this.newRoute = 0l;
		return this.newRoute;
	}
	
	public void setNewRoute(Long newRoute) {
		this.newRoute = newRoute;
	}
	
	public void increaseNewRoute(int count){
		setNewRoute(getNewRoute() + count);
	}
	
	public void increaseFailRoute(int count){
		setFailRoute(getFailRoute() + count);
	}
	
	/*****
	 * 修改线路条数
	 * @return
	 */
	@Column(name="UPDATE_ROUTE")
	public Long getUpdateRoute() {
		if(this.updateRoute==null)
			this.updateRoute = 0l;
		return this.updateRoute;
	}
	
	public void setUpdateRoute(Long updateRoute) {
		this.updateRoute = updateRoute;
	}
	
	public void increaseUpdateRoute(int count){
		setUpdateRoute(getUpdateRoute()+count);
	}
	
	/*****
	 * 删除线路条数
	 * @return
	 */
	@Column(name="DELETE_ROUTE")
	public long getDeleteRoute() {
		return this.deleteRoute;
	}
	
	public void setDeleteRoute(long deleteRoute) {
		this.deleteRoute = deleteRoute;
	}
	
	@Transient
	public void increaseDeleteRoute(int count){
		setDeleteRoute(getDeleteRoute()+count);
	}
	
	/*****
	 * 同步线路失败条数
	 * @return
	 */
	@Column(name="FAIL_ROUTE")
	public Long getFailRoute() {
		return this.failRoute;
	}
	
	public void setFailRoute(Long failRoute) {
		this.failRoute = failRoute;
	}
	
	/*****
	 * 状态：0正在同步，1同时完成
	 * @return
	 */
	@Column(name="STATE")
	public Long getState() {
		return this.state;
	}
	
	public void setState(Long state) {
		this.state = state;
	}
	
	/*****
	 * 同步类型（1手工同步，2自动同步）
	 * @return
	 */
	@Column(name="SYN_TYPE")
	public Long getSynType() {
		return this.synType;
	}
	
	public void setSynType(Long synType) {
		this.synType = synType;
	}
	
	@Transient
	public boolean isAutoSyn(){
		return SynTypes.auto==this.synType;
	}
	
	public Date getCreateTime() {
		return super.getCreateTime();
	}

	@Override
	public Object getObject() {
		// TODO Auto-generated method stub
		return null;
	}
}
