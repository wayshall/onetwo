package org.onetwo.common.result;

import java.util.Map;

import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;

@SuppressWarnings("serial")
public class DataResult extends AbstractDataResult<Map<String, Object>>{

	public static DataResult createFailed(String message){
		return create(FAILED, message);
	}

	public static DataResult createSucceed(String message){
		return create(SUCCEED, message);
	}

	public static DataResult createSucceed(Object...objects){
		return create(SUCCEED, "SUCCEED", objects);
	}
	public static DataResult create(int code, String message, Object...objects){
		DataResult result = new DataResult();
		result.setCode(code);
		result.setMessage(message);
		if(!LangUtils.isEmpty(objects)){
			Map<String, Object> data = CUtils.asMap(objects);
			result.setData(data);
		}
		return result;
	}
	private Map<String, Object> data;
	

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
	public void putData(String name, Object value){
		if(this.data==null)
			this.data = LangUtils.newHashMap();
		this.data.put(name, value);
	}
	
}
