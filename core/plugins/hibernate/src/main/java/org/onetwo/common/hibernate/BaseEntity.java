package org.onetwo.common.hibernate;

import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlRootElement;

import org.onetwo.common.db.IBaseEntity;

@XmlRootElement
@MappedSuperclass
abstract public class BaseEntity extends TimestampBaseEntity implements IBaseEntity{
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 4031516874721942989L;
	private Long creatorId;
	private Long updatorId;
	
	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
	public Long getUpdatorId() {
		return updatorId;
	}
	public void setUpdatorId(Long updatorId) {
		this.updatorId = updatorId;
	}
	
}
	
	