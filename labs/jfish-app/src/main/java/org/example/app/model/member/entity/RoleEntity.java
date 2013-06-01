package org.example.app.model.member.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.onetwo.common.db.IdEntity;

@Entity
@Table(name="T_ROLE")
@SequenceGenerator(name="seqRole", sequenceName="SEQ_T_ROLE")
public class RoleEntity implements IdEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8375012820817157172L;
	
	private Long id;
	private String name;

	@Id
	@GeneratedValue
	@Column(name="id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
