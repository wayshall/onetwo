package org.example.model.member.entity;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/*****
 * 
 * @Entity
 */
@SuppressWarnings("serial")
@Entity
@Table(name="cm_roles")
@SequenceGenerator(name="RolesEntityGenerator", sequenceName="SEQ_cm_roles")
public class RoleEntity implements Serializable {
	
	protected Long id;
  
	protected String name;
  
	protected String description;
  
	
	public RoleEntity(){
	}
	
	
	/*****
	 * 
	 * @return
	 */
	@Id
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RolesEntityGenerator")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="name")
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	/*****
	 * 
	 * @return
	 */
	@Column(name="description")
	public String getDescription() {
		return this.description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
}
