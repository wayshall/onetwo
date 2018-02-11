package org.onetwo.common.web.asyn;

import java.io.Serializable;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;


public class SimpleMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1897759687596570672L;

	
	protected Object source;
	protected TaskState state;
	protected String detail;
	
	public SimpleMessage(){
	}
	
	public SimpleMessage(Object source, TaskState state, String detail) {
		super();
		this.source = source;
		this.state = state;
		this.detail = detail;
	}

	public SimpleMessage(Object source){
		this.source = source;
	}
	
	public Object getSource() {
		return source;
	}
	public void setSource(Object source) {
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
	public TaskState getState() {
		return state;
	}
	public void setState(TaskState state) {
		this.state = state;
	}
	
	public String toString(){
		if(StringUtils.isNotBlank(detail))
			return detail;
		return LangUtils.append(source==null?"":source+" ", state.getName());
	}
}
