package org.onetwo.project.batch.tools.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.onetwo.common.hibernate.TimestampBaseEntity;
import org.onetwo.project.batch.tools.WebConstant;
import org.onetwo.project.batch.tools.WebConstant.ValidGroup.ValidWhenNew;
import org.springframework.format.annotation.DateTimeFormat;


/*****
 * PSAM卡信息表(
 * @Entity
 */
@SuppressWarnings("serial")
@Entity
@Table(name="TERM_PSAM")
@TableGenerator(table=WebConstant.SEQ_TABLE_NAME, pkColumnName="GEN_NAME",valueColumnName="GEN_VALUE", allocationSize=1, initialValue=0, name="PsamEntityGenerator", pkColumnValue="SEQ_TERM_PSAM")
//@DozerMapping
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class PsamEntity extends TimestampBaseEntity {
	
	/*****
	 * 递增序列
	 */
	protected Long id;
  
	/*****
	 * Psam卡的号码, 与pos终端一致
	 */
	protected String psamNo;
	
//	private String productNo;
  
	protected PsamStatus status;
  
	
  
	/*****
	 * 
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@NotNull(groups=ValidWhenNew.class)
	protected Date startDate;
  
	/*****
	 * 
	 */
	@NotNull(groups=ValidWhenNew.class)
	@DateTimeFormat(pattern="yyyy-MM-dd")
	protected Date endDate;
  
	/*****
	 * 
	 */
	//protected String secretKey;
  
	/*****
	 * 
	 */
	protected Date destroyDate;
	
//	private PosEntity pos;
	
	protected String areaCode;
	
	protected PsamFactory psamFactory;
  
	public PsamEntity(){
	}
	
	
	/*****
	 * 递增序列
	 * @return
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="PsamEntityGenerator")
	@Column(name="ID")
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	/*****
	 * Psam卡的号码, 与pos终端一致
	 * @return
	 */
	@Column(name="PSAM_NO")
	public String getPsamNo() {
		return this.psamNo;
	}
	
	public void setPsamNo(String psamNo) {
		this.psamNo = psamNo;
	}
	
	@Column(name="AREA_CODE")
	public String getAreaCode() {
		return areaCode;
	}


	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}


	/*****
	 * @return
	 */
	@Enumerated(EnumType.ORDINAL)
	@Column(name="STATUS")
	public PsamStatus getStatus() {
		return this.status;
	}
	
	public void setStatus(PsamStatus status) {
		this.status = status;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="START_DATE")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	public Date getStartDate() {
		return this.startDate;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="END_DATE")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	public Date getEndDate() {
		return this.endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
	 

	/*****
	 * 
	 * @return
	 */

	@Temporal(TemporalType.TIMESTAMP)
	public Date getDestroyDate() {
		return destroyDate;
	}
	public void setDestroyDate(Date destroyDate) {
		this.destroyDate = destroyDate;
	}

	
	
	
	
	
//	@OneToOne(mappedBy="psam")
//	public PosEntity getPos() {
//		return pos;
//	}


//	public void setPos(PosEntity pos) {
//		this.pos = pos;
//	}
	

	/*public String getProductNo() {
		return productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}*/

	@Enumerated(EnumType.STRING)
	public PsamFactory getPsamFactory() {
		return psamFactory;
	}


	public void setPsamFactory(PsamFactory psamFactory) {
		this.psamFactory = psamFactory;
	}



	/*****
	 * 0：初始状态
1：正在使用
2：损坏注销
	 */
	public static enum PsamStatus {
		CREATED("生成状态"),
		INTIALIZE("初始状态"),
		USING("使用中"),
		DESTROY("损坏注销");
		
		private final String name;
		
		private PsamStatus(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public int getValue() {
			return ordinal();
		}
		public static PsamStatus valueOf(int val){
			for(PsamStatus pt : values()){
				if(val==pt.ordinal())
					return pt;
			}
			throw new IllegalArgumentException("enum value: " + val);
		}
	}
	
	
	public static enum PsamFactory {
		WQ("握奇","WQ"),
		FTCX("飞天诚信","FTCX"),
		LNT("岭南通","LNT");
		
		private final String label;
		private final String value;
		private PsamFactory(String label, String value) {
			this.label = label;
			this.value = value;
		}
		public String getValue() {
			return value;
		}
		public String getLabel() {
			return label;
		}
		public static PsamFactory valueOf(int val){
			for(PsamFactory pt : values()){
				if(val==pt.ordinal())
					return pt;
			}
			throw new IllegalArgumentException("enum value: " + val);
		}
		
	}
	
	
}