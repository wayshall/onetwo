package org.onetwo.common.web.s2;

import java.util.HashMap;
import java.util.Map;

import org.onetwo.common.utils.map.M;

/****
 * ajax action的实现接口
 * 
 * @author weishao
 *
 */
@SuppressWarnings("unchecked")
abstract public class TheAjaxAction implements AjaxAction {

	protected Map<String, Object> result = new HashMap<String, Object>();
	
	@Override
	public Map execute() {
		ajax();
		return result;
	}
	

	public TheAjaxAction failed(String message){
		addResult(MESSAGE_CODE_KEY, RESULT_FAILED);
		addMessage(MESSAGE_KEY, message);
		return this;
	}

	public TheAjaxAction succeed(String message){
		addResult(MESSAGE_CODE_KEY, RESULT_SUCCEED);
		addMessage(MESSAGE_KEY, message);
		return this;
	}
	
	public TheAjaxAction addResult(String key, Object value){
		this.result.put(key, value);
		return this;
	}
	
	public TheAjaxAction addMessage(String key, String value){
		this.result.put(key, value);
		return this;
	}
	
	abstract public void ajax();

}
