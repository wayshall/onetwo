package org.onetwo.plugins.jdoc.data;

import java.util.ArrayList;
import java.util.List;

public class JMethodDoc {
	
	private JClassDoc classDoc;
	
	private String name;
	private String description;
	

	
	private List<JFieldDoc> params = new ArrayList<JFieldDoc>();
	
	private ReturnDoc returnDoc = new ReturnDoc(this);
	private List<CodeDoc> errorCodes = new ArrayList<CodeDoc>();
	
	
	public JMethodDoc(JClassDoc classDoc) {
		super();
		this.classDoc = classDoc;
	}
	
	public String getFullName(){
		return this.classDoc.getFullName()+"#"+this.getName();
	}

	public JClassDoc getClassDoc() {
		return classDoc;
	}

	public String getKey(){
		return this.getFullName();
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
	
	public List<JFieldDoc> getParams() {
		return params;
	}
	public void addParam(JFieldDoc param){
		if(param==null)
			return ;
		this.params.add(param);
	}
	public void addErrorCode(CodeDoc code){
		if(code==null)
			return ;
		this.errorCodes.add(code);
	}
	public ReturnDoc getReturnDoc() {
		return returnDoc;
	}
	public void setReturnDoc(ReturnDoc returnDoc) {
		this.returnDoc = returnDoc;
	}
	public List<CodeDoc> getErrorCodes() {
		return errorCodes;
	}
}
