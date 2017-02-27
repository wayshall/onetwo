package org.onetwo.common.dbm.model.vo;

import java.util.List;



public class CompanyVO {

	protected Long id;
	protected String name;
	protected String description;
	protected int employeeNumber;
	protected List<DepartmentVO> departments;
  
	public CompanyVO(){
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

	public List<DepartmentVO> getDepartments() {
		return departments;
	}

	public void setDepartments(List<DepartmentVO> departments) {
		this.departments = departments;
	}

}