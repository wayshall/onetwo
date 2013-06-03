package org.onetwo.plugins.jdoc.data;

import java.util.List;

public class ReturnDoc {

	private JMethodDoc method;
	private String type;
	private List<String> dataTypes;
	private String description;
	
	public ReturnDoc(JMethodDoc method) {
		super();
		this.method = method;
	}
	
	public JMethodDoc getMethod() {
		return method;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getDataTypes() {
		return dataTypes;
	}

	public void setDataTypes(List<String> dataTypes) {
		dataTypes.remove("org.onetwo.plugins.rest.RestResult");
		this.dataTypes = dataTypes;
	}


}
