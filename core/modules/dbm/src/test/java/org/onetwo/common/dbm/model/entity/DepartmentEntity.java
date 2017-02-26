package org.onetwo.common.dbm.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.onetwo.common.db.CrudEntityManager;
import org.onetwo.dbm.support.Dbms;


/*****
 * @Entity
 */
@SuppressWarnings("serial")
@Entity
@Table(name="department")
public class DepartmentEntity extends BaseEntity {
	

	final static public CrudEntityManager<DepartmentEntity, Long> crudManager = Dbms.newCrudManager(DepartmentEntity.class);

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	@Column(name="ID")
	protected Long id;
	@Length(min=1, max=50)
	protected String name;
	protected Integer employeeNumber;
	protected Long companyId;
  
	public DepartmentEntity(){
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

	public Integer getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(Integer employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

}