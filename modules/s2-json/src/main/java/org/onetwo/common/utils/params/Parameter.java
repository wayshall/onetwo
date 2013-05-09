package org.onetwo.common.utils.params;

import java.util.Map;

import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.onetwo.common.utils.json.JSONUtils;

/**********
 * paramName[keep-property:property]=paramValue
 * @author weishao.zeng
 *
 */
public class Parameter {

	public static final String SEPARATOR = ":";
	public static final String KEEP = "keep-";
	
	public static Parameter parse(String name, Object value){
		if(StringUtils.isBlank(name))
			return null;
		int start = name.indexOf('[');
		if(start==-1 || !name.endsWith("]"))
			return null;
		String actualName = name.substring(0, start);
		String propertiesStr = name.substring(start+1, name.length()-1);
		String[] properties = StringUtils.split(propertiesStr, SEPARATOR);
		if(properties==null || properties.length==0)
			return null;
		System.out.println("[name="+actualName+", properites="+propertiesStr+"]");
		Parameter parameter = new Parameter();
		parameter.setName(actualName);
		parameter.setPropertiesByString(properties);
		parameter.setValue(value.toString());
		return parameter;
	}
	
	public static Predicate isKeepParameter = new Predicate(){

		@Override
		public boolean evaluate(Object object) {
			return ((ParameterProperty)object).isKeep();
		}
		
	};
	
	private ParameterProperty[] properties;
	private String name;
	private Object value;
	
	private Parameter(){
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}
	
	public ParameterProperty[] getProperties() {
		return properties;
	}

	public void setPropertiesByString(String[] properties) {
		if(properties==null || properties.length==0)
			return ;
		for(String prop : properties){
			if(StringUtils.isBlank(prop))
				continue;
			this.properties = (ParameterProperty[])ArrayUtils.add(this.properties, new ParameterProperty(prop));
		}
	}

	public void setValue(Object origanlValue) {
		if(origanlValue!=null && String.class.isAssignableFrom(origanlValue.getClass()) && JSONUtils.isJsonString(origanlValue.toString())){
			value = JSONUtils.getJsonMap(origanlValue.toString());
		}else{
			value = origanlValue;
		}
	}


	public boolean isMapValue(){
		return value instanceof Map;
	}
	
	public String getValueString(){
		String vs = null;
		if(value instanceof Map){
			vs = JSONUtils.getJsonString(value);
		}else{
			vs = value.toString();
		}
		return vs;
	}

	public String getKeepParameterString(){
		return getString(KEEP, isKeepParameter);
	}
	
	public String getString(String type, Predicate predicate){
		if(properties==null || properties.length==0)
			return "";
		StringBuilder s = new StringBuilder(name);
		s.append("[");
		int index=0;
		for(ParameterProperty prop : properties){
			if(predicate!=null && !predicate.evaluate(prop))
				continue;
			if(index!=0)
				s.append(":");
			if(StringUtils.isBlank(type))
				s.append(prop.getName());
			else
				s.append(prop.getTypeName(type));
			index++;
		}
		s.append("]=");
		String vs = getValueString();
		s.append(vs);
		return s.toString();
	}
	
	public String getTypeName(String type) {
		if(StringUtils.isBlank(type))
			return name+"="+value;
		return name+"["+type+"]="+value;
	}
	
	public String toString(){
		return getString(null, null);
	}
}
