package org.example.app.model.member.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.example.app.model.utils.BaseEntity;

@Entity
@Table(name="T_COLUMN")
@SequenceGenerator(name="seqColumn", sequenceName="SEQ_T_COLUMN")
public class ColumnEntity extends BaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7778284871363913162L;
	private Long id;
	private String name;

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
