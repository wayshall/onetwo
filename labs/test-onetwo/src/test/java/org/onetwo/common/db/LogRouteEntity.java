package org.onetwo.common.db;

import java.util.Date;
import java.util.HashMap;
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
import javax.persistence.Transient;

import org.onetwo.common.utils.map.KVEntry;



/*****
 * 线路同步日志
 * @Entity
 */
@SuppressWarnings("serial")
@Entity
@Table(name="SYN_LOG_ROUTE")
@SequenceGenerator(name="LogRouteEntityGenerator", sequenceName="SEQ_SYN_LOG_ROUTE")
public class LogRouteEntity extends AbstractBaseEntity<Long> {
	
	public static class LogRouteType {
		public static final Map<String, String> values = new HashMap<String, String>();
		public static final KVEntry<String, String> ADD = KVEntry.create("Add", "增加", values);
		public static final KVEntry<String, String> UPDATE = KVEntry.create("Update", "更新", values);
	}
	

	public static class LogRouteState {
		public static final Map<Long, String> values = new HashMap<Long, String>();
		public static final KVEntry<Long, String> SUCCEED = KVEntry.create(1l, "成功", values);
		public static final KVEntry<Long, String> MIX = KVEntry.create(2l, "部分成功", values);
		public static final KVEntry<Long, String> FAILED = KVEntry.create(0l, "失败", values);
	}
	
	
	protected Long id;
  
	protected String yooyoRouteId;
  
	protected String supplierRouteCode;
  
	protected String routeName;
  
	protected Date synStartTime;
  
	protected Date synEndTime;
  
	protected Long state;
  
	protected String failReason;
  
	protected Long newTour;
  
	protected Long updateTour;
  
	protected Long deleteTour;
  
	protected Long failTour;
  
	protected String type;
  
	protected Long repetLogSupplierId;
  
	
	private LogSupplierEntity logSupplier;
  
	
	public LogRouteEntity(){
	}
	
	
	/*****
	 * 
	 * @return
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LogRouteEntityGenerator")
	@Column(name="ID")
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	/*****
	 * 商家同步日志ID
	 * @return
	 */
	@ManyToOne
	@JoinColumn(name="LOG_SUPPLIER_ID")
	public LogSupplierEntity getLogSupplier() {
		return logSupplier;
	}

	public void setLogSupplier(LogSupplierEntity logSupplier) {
		this.logSupplier = logSupplier;
	}
	
	/*****
	 * YOOYO线路ID
	 * @return
	 */
	@Column(name="YOOYO_ROUTE_ID")
	public String getYooyoRouteId() {
		return this.yooyoRouteId;
	}


	public void setYooyoRouteId(String yooyoRouteId) {
		this.yooyoRouteId = yooyoRouteId;
	}
	
	/*****
	 * 供应商的线路代码
	 * @return
	 */
	@Column(name="SUPPLIER_ROUTE_CODE")
	public String getSupplierRouteCode() {
		return this.supplierRouteCode;
	}
	
	public void setSupplierRouteCode(String supplierRouteCode) {
		this.supplierRouteCode = supplierRouteCode;
	}
	
	/*****
	 * 线路名称
	 * @return
	 */
	@Column(name="ROUTE_NAME")
	public String getRouteName() {
		return this.routeName;
	}
	
	public void setRouteName(String routeName) {
		this.routeName = routeName;
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
	 * 同步状态(1成功，-1失败)
	 * @return
	 */
	@Column(name="STATE")
	public Long getState() {
		return this.state;
	}
	
	public void setState(Long state) {
		this.state = state;
	}
	
	@Transient
	public boolean isFuzzyFailedState(){
		return LogRouteState.FAILED.equals(getState()) || LogRouteState.MIX.equals(getState());
	}
	
	@Transient
	public boolean isAllTourFailed(){
		return getFailTour()==getTotalSynTour();
	}
	
	@Transient
	public boolean isAllTourSuceed(){
		return getFailTour()==0;
	}
	
	@Transient
	public void autoSetStateByTourCount(){
		if(isAllTourSuceed()){
			setState(LogRouteState.SUCCEED.getKey());
		}else if(isAllTourFailed()){
			setState(LogRouteState.FAILED.getKey());
			appendFailReason("autoSetStateByTourCount:"+LogRouteState.FAILED.getValue());
		}else{
			setState(LogRouteState.MIX.getKey());
			appendFailReason("autoSetStateByTourCount:"+LogRouteState.MIX.getValue());
		}
	}
	
	/*****
	 * 失败原因
	 * @return
	 */
	@Column(name="FAIL_REASON")
	public String getFailReason() {
		if(this.failReason!=null && this.failReason.length()>4000){
			this.failReason = this.failReason.substring(0, 3900);
		}
		return this.failReason;
	}
	
	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}
	
	/*****
	 * 新增团条数
	 * @return
	 */
	@Column(name="NEW_TOUR")
	public Long getNewTour() {
		if(this.newTour==null)
			this.newTour = 0l;
		return this.newTour;
	}
	
	public void setNewTour(Long newTour) {
		this.newTour = newTour;
	}
	
	public void increaseNewTour(int count) {
		setNewTour(this.getNewTour() + count);
	}
	
	public void increaseUpdateTour(int count) {
		setUpdateTour(getUpdateTour() + count);
	}
	
	public void increaseDeleteTour(int count) {
		setDeleteTour(getDeleteTour() + count);
	}
	
	/*****
	 * 更新团条数
	 * @return
	 */
	@Column(name="UPDATE_TOUR")
	public Long getUpdateTour() {
		if(this.updateTour==null)
			this.updateTour = 0l;
		return this.updateTour;
	}
	
	public void setUpdateTour(Long updateTour) {
		this.updateTour = updateTour;
	}
	
	public long getTotalSynTour(){
		return getNewTour()+getUpdateTour()+getDeleteTour();
	}
	
	/*****
	 * 删除团条数
	 * @return
	 */
	@Column(name="DELETE_TOUR")
	public Long getDeleteTour() {
		if(this.deleteTour==null)
			this.deleteTour = 0l;
		return this.deleteTour;
	}
	
	public void setDeleteTour(Long deleteTour) {
		this.deleteTour = deleteTour;
	}
	
	/*****
	 * 失败团条数
	 * @return
	 */
	@Column(name="FAIL_TOUR")
	public Long getFailTour() {
		if(this.failTour==null)
			this.failTour = 0l;
		return this.failTour;
	}
	
	public void increaseFailTour(int count) {
		setFailTour(getFailTour() + count);
	}
	
	public void appendFailReason(String reason) {
		if(this.failReason==null)
			this.setFailReason("");
		this.setFailReason(getFailReason()+reason);
	}
	
	public void setFailTour(Long failTour) {
		this.failTour = failTour;
	}
	
	/*****
	 * 同步类型(ADD增加、UPDATE修改，DELETE删除)
	 * @return
	 */
	@Column(name="TYPE")
	public String getType() {
		return this.type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	/*****
	 * 重新同步线路日志ID
	 * @return
	 */
	@Column(name="REPET_LOG_SUPPLIER_ID")
	public Long getRepetLogSupplierId() {
		return this.repetLogSupplierId;
	}
	
	public void setRepetLogSupplierId(Long repetLogSupplierId) {
		this.repetLogSupplierId = repetLogSupplierId;
	}
	
	@Transient
	public Long getSynCostTime(){
		if(getSynStartTime()==null || getSynEndTime()==null)
			return 0l;
		return getSynEndTime().getTime() - getSynStartTime().getTime();
	}


	@Override
	public String toString() {
		return "LogRouteEntity [supplierRouteCode=" + supplierRouteCode + ", routeName=" + routeName + ", newTour=" + newTour + ", updateTour=" + updateTour + ", failTour=" + failTour + "]";
	}
	
}
