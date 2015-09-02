package org.onetwo.common.result;

import java.util.Map;

import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;

@SuppressWarnings("serial")
public class MapResult extends AbstractDataResult<Map<Object, Object>>{

	public static MapResult createFailed(String message){
		return create(FAILED, message);
	}

	public static MapResult createSucceed(String message){
		return create(SUCCEED, message);
	}

	public static MapResult createSucceed(Object...objects){
		return create(SUCCEED, "SUCCEED", objects);
	}
	public static MapResult create(int code, String message, Object...objects){
		MapResult result = new MapResult();
		result.setCode(code);
		result.setMessage(message);
		if(!LangUtils.isEmpty(objects)){
			Map<Object, Object> data = CUtils.asMap(objects);
			result.setData(data);
		}
		return result;
	}
	private Map<Object, Object> data;
	

	public Map<Object, Object> getData() {
		return data;
	}

	public void setData(Map<Object, Object> data) {
		this.data = data;
	}
	
	public void putData(Object name, Object value){
		if(this.data==null)
			this.data = LangUtils.newHashMap();
		this.data.put(name, value);
	}
	
}
