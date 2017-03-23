package org.onetwo.common.dbm.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name="employee")
public class EmployeeEntity extends BaseEntity {
	
	public static enum EmployeeGenders {
		FEMALE("女性"),
		MALE("男性");
		
		final private String label;
		private EmployeeGenders(String label) {
			this.label = label;
		}
		public String getLabel() {
			return label;
		}
	}

	final static public CrudEntityManager<EmployeeEntity, Long> crudManager = Dbms.newCrudManager(EmployeeEntity.class);

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	@Column(name="ID")
	protected Long id;
	@Length(min=1, max=50)
	protected String name;
	protected Date joinDate;
	private Long departmentId;
	protected Date birthday;
	@Enumerated(EnumType.ORDINAL)
	protected EmployeeGenders gender;
  
	public EmployeeEntity(){
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

	public Date getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(Date joinDate) {
		this.joinDate = joinDate;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public EmployeeGenders getGender() {
		return gender;
	}

	public void setGender(EmployeeGenders gender) {
		this.gender = gender;
	}
	
}