package org.example.app.model.member.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;

@SuppressWarnings("serial")
@Entity
@Table(name="T_USER")
public class UserVersionEntity extends UserEntity {

	private Long version;
	
	@Version
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}
}
