package org.onetwo.common.db;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.onetwo.common.db.sqlext.ExtQueryUtils;
import org.onetwo.common.db.sqlext.ParamValues.PlaceHolder;
import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.StringUtils;


@SuppressWarnings({ "rawtypes", "unchecked" })
public class JFishQueryValue {

	public static JFishQueryValue create(PlaceHolder holder, String sql){
		return new JFishQueryValue(holder, sql);
	}
	public static JFishQueryValue create(String sql){
		return new JFishQueryValue(PlaceHolder.POSITION, sql);
	}
	public static JFishQueryValue createNamed(String sql){
		return new JFishQueryValue(PlaceHolder.NAMED, sql);
	}
	
	private PlaceHolder holder;
	private Object values;
	private Class<?> resultClass;
	private String sql;
	private String countSql;
	
	private JFishQueryValue(PlaceHolder holder, String sql){
		this.holder = holder;
		if(PlaceHolder.POSITION.equals(holder))
			this.values = new ArrayList();
		else
			this.values = new LinkedHashMap();
		this.sql = sql;
	}

	public JFishQueryValue setValue(int index, Object value){
		if(PlaceHolder.POSITION.equals(holder)){
			List list = getValues();
			while(index>=list.size()){
				list.add(null);
			}
			list.set(index, convertValue(value));
		}
		else {
			Map map = getValues();
			map.put(String.valueOf(index), convertValue(value));
		}
		return this;
	}
	

	private Object convertValue(Object value){
		return ExtQueryUtils.getNameIfEnum(value, value);
	}
	
	public JFishQueryValue setValue(Map<String, Object> parameters){
		/*if(this.holder==PlaceHolder.NAMED){
			((Map<String, Object>) this.values).putAll(parameters);
		}else{
			List<Object> params = this.getValues();
			for(Map.Entry<String, Object> entry : parameters.entrySet()){
				try {
					params.add(Integer.parseInt(entry.getKey()), convertValue(entry.getValue()));
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException("error parameter position : " + entry.getKey());
				}
			}
		}*/
		parameters.forEach((k,v)-> setValue(k, v));
		return this;
	}
	
	public JFishQueryValue setValue(List<?> parametsrs){
		/*if(this.holder==PlaceHolder.POSITION){
			((List)this.values).addAll(parametsrs);
		}else{
			Map<String, Object> params = this.getValues();
			int index = 0;
			for(Object val : parametsrs){
				params.put(String.valueOf(index), convertValue(val));
				index++;
			}
		}*/
		Map<String, Object> parameters = CUtils.toMap(parametsrs, (e, index)->String.valueOf(index));
		this.setValue(parameters);
		return this;
	}
	
	public JFishQueryValue setValue(String field, Object value){
		if(PlaceHolder.POSITION.equals(holder)){
			List list = getValues();
			try {
				int index = Integer.parseInt(field);
				list.add(index, convertValue(value));
			} catch (NumberFormatException e) {
				list.add(convertValue(value));
			}
		}
		else {
			Map map = getValues();
			map.put(field, convertValue(value));
		}
		return this;
	}
	
	public Map<String, Object> asMap() {
		return getValues();
	}

	public List<Object> asList() {
		return getValues();
	}
	
	public boolean isPosition(){
		return this.holder == PlaceHolder.POSITION;
	}
	
	public boolean isNamed(){
		return this.holder == PlaceHolder.NAMED;
	}
	
	/*********
	 * List Or Map
	 * @return
	 */
	public <T> T getValues(){
		return (T) values;
	}

	public PlaceHolder getHolder() {
		return holder;
	}

	public Class<?> getResultClass() {
		return resultClass;
	}

	public void setResultClass(Class<?> resultClass) {
		this.resultClass = resultClass;
	}

	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public String getCountSql() {
		if(StringUtils.isBlank(countSql)){
			this.countSql = ExtQueryUtils.buildCountSql(this.sql, "");
		}
		return countSql;
	}
	public void setCountSql(String countSql) {
		this.countSql = countSql;
	}
}
