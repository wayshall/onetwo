package org.onetwo.ext.poi.word;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.ext.poi.utils.ExcelUtils;

public class JavaClassInfo {
	static public String doubleQuotes(String str){
		return "\""+str+"\"";
	}

	static public String oneLine(String str){
		return oneLine(1, str);
	}
	static public String oneLine(int tabsize, String str){
		String newstr = "";
		for (int i = 0; i < tabsize; i++) {
			newstr += "\t";
		}
		return newstr+str+"\n";
	}
	private String className;
	private List<FieldInfo> fields;
	private boolean convert2CamelStyle = true;

	public JavaClassInfo(String className) {
		super();
		this.className = className;
	}
	public String toJavaString() {
		String str = oneLine("public class "+className+" {");
		for(FieldInfo field : fields){
			str += field.toJavaString(convert2CamelStyle);
		}
		str += oneLine("}");
		return str;
	}
	
	public String toEsMappingString() {
		String str = oneLine(doubleQuotes(className)+": {");
		str += oneLine(2, doubleQuotes("properties")+": {");
		for(FieldInfo field : fields){
			str += field.toEsMappingString(3);
		}
		str += oneLine(2, "}");
		str += oneLine("}");
		return str;
	}

	public boolean isConvert2CamelStyle() {
		return convert2CamelStyle;
	}
	public void setConvert2CamelStyle(boolean convert2CamelStyle) {
		this.convert2CamelStyle = convert2CamelStyle;
	}
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<FieldInfo> getFields() {
		return fields;
	}

	public void setFields(List<FieldInfo> fields) {
		this.fields = fields;
	}

	public static class FieldInfo {
		private String name;
		private String type;
		private boolean required;
		private String comment;

		public String toJavaString(boolean convert2CamelStyle) {
			String str = "";
			if(StringUtils.isNotBlank(comment)){
				str += oneLine("/****");
				str += oneLine(comment);
				str += oneLine("*****/");
			}
			if(required){
				str += oneLine("@NotNull");
			}
			str += oneLine("private " + type + " " + getFieldName(convert2CamelStyle) + ";");
			return str;
		}
		
		public String toEsMappingString(int tabsize) {
			if(StringUtils.isBlank(name) || StringUtils.isBlank(type)){
				return "";
			}
			String str = oneLine(tabsize, doubleQuotes(name.toLowerCase())+": {");
			str += oneLine(tabsize+1, doubleQuotes("type")+": "+doubleQuotes(type.toLowerCase())+", ");
			str += oneLine(tabsize+1, doubleQuotes("index")+": "+doubleQuotes("no"));
			str += oneLine(tabsize, "},");
			return str;
		}
		
		private String getFieldName(boolean convert2CamelStyle){
			if(StringUtils.isBlank(name)){
				return "";
			}
			if(convert2CamelStyle){
				return ExcelUtils.toCamel(name, false);
			}
			return name;
		}

		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public boolean isRequired() {
			return required;
		}
		public void setRequired(boolean required) {
			this.required = required;
		}
		public String getComment() {
			return comment;
		}
		public void setComment(String comment) {
			this.comment = comment;
		}
		
	}

}
