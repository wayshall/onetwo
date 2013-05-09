package org.onetwo.plugins.jdoc.data;

import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.utils.LangUtils;

public class JClassDoc {
	
	private String packageName;
	private String name;
	private String description;
	
	private List<JFieldDoc> fields = new ArrayList<JFieldDoc>();
	private List<JMethodDoc> methods = new ArrayList<JMethodDoc>();

	public JClassDoc() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getFullName(){
		return this.packageName+"."+name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<JMethodDoc> getMethods() {
		return methods;
	}
	
	public JMethodDoc getMethod(String name){
		for(JMethodDoc m : methods){
			if(m.getFullName().equals(name))
				return m;
		}
		return null;
	}

	public void addMethod(JMethodDoc m){
		if(m==null)
			return ;
		this.methods.add(m);
	}

	public void addField(JFieldDoc f){
		if(f==null)
			return ;
		this.fields.add(f);
	}
	
	public String toString(){
		return LangUtils.append(getFullName());
	}

	public List<JFieldDoc> getFields() {
		return fields;
	}

	public void setFields(List<JFieldDoc> fields) {
		this.fields = fields;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	
}
