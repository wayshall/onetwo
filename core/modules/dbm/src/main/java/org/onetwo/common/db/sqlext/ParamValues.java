package org.onetwo.common.db.sqlext;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.onetwo.dbm.utils.DbmUtils;

import com.google.common.collect.ImmutableMap;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ParamValues {
	
	/*public static enum PlaceHolder {
		POSITION,
		NAMED
	}*/

	protected ExtQueryDialet sqlDialet;
//	private PlaceHolder holder;
	private Map<String, Object> values = new LinkedHashMap<String, Object>();
	
	
	public ParamValues(ExtQueryDialet sqlDialet){
		this.sqlDialet = sqlDialet;
	}

	public void addValue(String field, Object value, StringBuilder sqlScript){
		int size = values.size();
		String fieldName = this.sqlDialet.getNamedPlaceHolder(field, size);
		sqlScript.append(":").append(fieldName);
		values.put(fieldName, getActualSqlValue(value));
	}
	
	private Object getActualSqlValue(Object value){
		return DbmUtils.getActualSqlValue(value);
	}
	
	/*public void directAddValue(Object value){
		Map map = getValues();
		if(value instanceof Collection){
			Map m = CUtils.asLinkedMap(((Collection)value).toArray());
			if(m!=null && !m.isEmpty())
				map.putAll(m);
		}else if(value instanceof Map){
			map.putAll((Map)value);
		}else{
			LangUtils.throwBaseException("unsupported args: "+LangUtils.toString(value));
		}
	}*/
	
	public void addValues(Map<String, Object> params){
		params.forEach((k, v)->values.put(k, getActualSqlValue(v)));
	}
	
	public void joinToQuery(ExtQuery subQuery){
		/*Map<String, Object> pvs = getValues();
		Map<String, Object> subParamValues = subQuery.getParamsValue().getValues();
		subParamValues.putAll(pvs);
		subQuery.build();
		pvs.clear();
		pvs.putAll(subParamValues);*/
		ParamValues subParamValues = subQuery.getParamsValue();
		subParamValues.addValues(getValues());
		subQuery.build();
		
		clear();
		addValues(subParamValues.getValues());
	}
	
	private void clear(){
		this.values.clear();
	}
	
	/*********
	 * List Or Map
	 * @return
	 */
	public Map<String, Object> getValues(){
//		return ImmutableMap.copyOf(values);
		return Collections.unmodifiableMap(values);
	}
	public Map<String, Object> asMap(){
		return (Map) getValues();
	}
	/*public List<Object> asList(){
		return (List<Object>) getValues();
	}
	
	public boolean isList(){
		return List.class.isInstance(values);
	}
	*/
	/*public boolean isMap(){
		return Map.class.isInstance(values);
	}*/

}
