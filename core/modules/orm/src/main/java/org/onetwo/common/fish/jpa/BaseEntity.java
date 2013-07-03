package org.onetwo.common.fish.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.onetwo.common.db.IBaseEntity;
import org.onetwo.common.utils.xml.jaxb.DateAdapter;

@XmlRootElement
@MappedSuperclass
abstract public class BaseEntity<T> implements IBaseEntity{
 
	private static final long serialVersionUID = 122579169646461421L;

	protected Date createTime;
	
	protected Date lastUpdateTime;

	@Column(name="CREATE_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name="LAST_UPDATE_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

}
	
	