package org.onetwo.common.web.s2;


/****
 * ajax action的实现接口
 * 
 * @author weishao
 *
 */
@SuppressWarnings("unchecked")
abstract public class JsonpAjaxAction extends TheAjaxAction {
	
	private String callback;
	
	public JsonpAjaxAction(String callback){
		this.callback = callback;
	}

	public String getCallback() {
		return callback;
	}

}
