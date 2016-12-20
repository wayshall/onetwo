package org.onetwo.ext.poi.word;

public class ParamData {
	
	private String name;
	private String dataType;
	private boolean required;
	private String sample;
	private String desc;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean isRequired) {
		this.required = isRequired;
	}
	public String getSample() {
		return sample;
	}
	public void setSample(String sample) {
		this.sample = sample;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	@Override
	public String toString() {
		return "ParamData [name=" + name + ", dataType=" + dataType
				+ ", required=" + required + ", sample=" + sample + ", desc="
				+ desc + "]";
	}
	

}
