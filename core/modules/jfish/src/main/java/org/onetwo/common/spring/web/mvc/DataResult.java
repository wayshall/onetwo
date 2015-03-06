package org.onetwo.common.spring.web.mvc;

import java.util.Map;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Result;

public class DataResult implements Result<Integer, Map<String, Object>>{

	public static DataResult createFailed(String message){
		return create(FAILED, message);
	}

	public static DataResult createSucceed(String message){
		return create(SUCCEED, message);
	}
	public static DataResult create(int code, String message, Object...objects){
		DataResult result = new DataResult();
		result.setCode(code);
		result.setMessage(message);
		if(!LangUtils.isEmpty(objects)){
			Map<String, Object> data = LangUtils.asMap(objects);
			result.setData(data);
		}
		return result;
	}
	public static final int SUCCEED = 1;
	public static final int FAILED = 0;
	private int code = 1;//0,1;
	private String message;//
	
	private Map<String, Object> data;
	
	public DataResult(){
		this.code = SUCCEED;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

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
	
	public boolean isSuccess(){
		if(code==SUCCEED)
			return true;
		else
			return false;
	}
	
}
