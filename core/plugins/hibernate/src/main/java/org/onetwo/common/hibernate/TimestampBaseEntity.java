package org.onetwo.common.hibernate;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.onetwo.common.db.IBaseEntity;
import org.onetwo.common.utils.xml.jaxb.DateAdapter;

@SuppressWarnings("serial")
@MappedSuperclass
abstract public class TimestampBaseEntity implements IBaseEntity {
	
	private Date createTime;
	private Date lastUpdateTime;
	
	@Column(name="CREATE_TIME", updatable=false)
	@Temporal(TemporalType.TIMESTAMP)
	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Column(name="LAST_UPDATE_TIME", insertable=false)
	@Temporal(TemporalType.TIMESTAMP)
	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	

}
