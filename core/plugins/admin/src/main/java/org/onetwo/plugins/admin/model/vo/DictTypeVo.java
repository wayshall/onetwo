package org.onetwo.plugins.admin.model.vo;

import java.util.List;

import org.onetwo.plugins.admin.model.data.entity.DictionaryEntity;

public class DictTypeVo {
	
	private String code;
	private String name;
	private String remark;
	private Integer sort;
	private Boolean valid;
	//如果是枚举值，不可修改
	protected Boolean enumValue;
	
	private List<DictionaryEntity> dicts;
	
	public DictTypeVo(){
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<DictionaryEntity> getDicts() {
		return dicts;
	}
	public void setDicts(List<DictionaryEntity> dicts) {
		this.dicts = dicts;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Boolean getValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	public Boolean getEnumValue() {
		return enumValue;
	}

	public void setEnumValue(Boolean enumValue) {
		this.enumValue = enumValue;
	}

	
}
