package org.onetwo.common.dbm.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.onetwo.common.db.CrudEntityManager;
import org.onetwo.dbm.utils.Dbms;


/*****
 * @Entity
 */
@SuppressWarnings("serial")
@Entity
@Table(name="company")
public class CompanyEntity extends BaseEntity {
	

	final static public CrudEntityManager<CompanyEntity, Long> crudManager = Dbms.newCrudManager(CompanyEntity.class);

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	@Column(name="ID")
	protected Long id;
	@Length(min=1, max=50)
	protected String name;
	@Length(min=0, max=1000)
	protected String description;
	protected int employeeNumber;
  
	public CompanyEntity(){
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(int employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

}