package org.onetwo.plugins.jdoc.Lexer;

import org.apache.commons.lang.ArrayUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

public class DocDirectiveInfo {
	
	private DocDirective directive;
	private String description;
	private String[] values;
	
	public DocDirectiveInfo(DocDirective directive) {
		super();
		this.directive = directive;
	}
	public String getName(){
		return directive.getName();
	}
	public DocDirective getDirective() {
		return directive;
	}
	public void setDirective(DocDirective directive) {
		this.directive = directive;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
		values = StringUtils.split(description, "-");
	}
	
	public String getValue(int index){
		if(LangUtils.isEmpty(values) || index>=values.length)
			return "";
		return values[index].trim();
	}
	
	public String getValues(int start){
		return getValues(start, values.length);
	}
	
	public String getValues(int start, int end){
		if(LangUtils.isEmpty(values) || start>=values.length)
			return "";
		Object[] ary = ArrayUtils.subarray(values, start, end);
		String str = StringUtils.join(ary, "");
		return str;
	}

	public String[] getValues() {
		return values;
	}
	public String toString(){
		return directive + ":" + description;
	}
}
