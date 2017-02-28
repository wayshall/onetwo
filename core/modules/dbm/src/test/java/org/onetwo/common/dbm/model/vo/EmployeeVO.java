package org.onetwo.common.dbm.model.vo;

import java.util.Date;

public class EmployeeVO  {

	protected Long id;
	protected String name;
	protected Date joinDate;
	private Long departmentId;
	protected Date birthday;
	/*@Enumerated(EnumType.ORDINAL)
	protected EmployeeGenders gender;*/
  
	public EmployeeVO(){
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

}