package org.onetwo.common.web.asyn2;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.web.asyn2.AsynMessageHolder.AsynState;


public class SimpleMessage<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1897759687596570672L;

	
	protected T source;
	protected AsynState state;
	protected String detail;
	
	public SimpleMessage(){
	}
	
	public SimpleMessage(T source){
		this.source = source;
	}
	
	public T getSource() {
		return source;
	}
	public void setSource(T source) {
		this.source = source;
	}
	
	public String getDetail() {
		if(StringUtils.isNotBlank(detail)){
			return detail;
		}
		return state.getName();
	}
	
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public AsynState getState() {
		return state;
	}
	public void setState(AsynState state) {
		this.state = state;
	}
	
	public String toString(){
		if(StringUtils.isNotBlank(detail))
			return detail;
		return LangUtils.append(source==null?"":source+" ", state.getName());
	}
}
