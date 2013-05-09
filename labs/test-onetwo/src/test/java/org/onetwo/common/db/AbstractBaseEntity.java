package org.onetwo.common.db;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@SuppressWarnings("serial")
@MappedSuperclass
abstract public class AbstractBaseEntity<PK extends Serializable> {

	private Date createTime;
	
	private Date lastUpdateTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATE_TIME") 
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LAST_UPDATE_TIME")
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	
	public PK getMyId(){
		return null;
	}

}
	
	