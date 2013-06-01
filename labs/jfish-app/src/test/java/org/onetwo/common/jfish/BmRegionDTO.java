package org.onetwo.common.jfish;

import org.onetwo.common.db.IdEntity;
import org.onetwo.common.fish.annotation.JFishEntity;

@SuppressWarnings("serial")
//@JFishQueryable(table="BM_REGION")
@JFishEntity(table="BM_REGION")
public class BmRegionDTO implements IdEntity<Long>{ 
	
	private Long id;
	private String ccode;
	private String cname;
	private String fullname;
	private String parentcode;
	private int grade;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCcode() {
		return ccode;
	}
	public void setCcode(String ccode) {
		this.ccode = ccode;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	
	public String getParentcode() {
		return parentcode;
	}
	public void setParentcode(String parentcode) {
		this.parentcode = parentcode;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}

}
