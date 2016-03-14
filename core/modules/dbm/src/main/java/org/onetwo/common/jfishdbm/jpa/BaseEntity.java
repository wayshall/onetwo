package org.onetwo.common.jfishdbm.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.onetwo.common.db.TimeRecordableEntity;
import org.onetwo.common.xml.jaxb.DateAdapter;

@XmlRootElement
@MappedSuperclass
abstract public class BaseEntity implements TimeRecordableEntity{
 
	private static final long serialVersionUID = 122579169646461421L;

	protected Date createTime;
	
	protected Date lastUpdateTime;

	@Column(name="CREATE_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getCreateAt() {
		return createTime;
	}

	public void setCreateAt(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name="LAST_UPDATE_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	@XmlJavaTypeAdapter(DateAdapter.class)
	public Date getUpdateAt() {
		return lastUpdateTime;
	}

	public void setUpdateAt(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

}
	
	