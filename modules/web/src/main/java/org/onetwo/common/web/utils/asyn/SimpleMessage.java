package org.onetwo.common.web.utils.asyn;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.map.ConstantMap;


@Deprecated
public class SimpleMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1897759687596570672L;

	public static class DefaultState {
		public static final Integer SUCCEED = 1;
		public static final Integer FAILED = 0;
		
		@SuppressWarnings("serial")
		public static final ConstantMap<Integer, String> LABELS = new ConstantMap<Integer, String>(){
			{
				constant(SUCCEED, "成功");
				constant(FAILED, "失败");
			}
		};
	}
	
	private String source;
	private Integer state;
	private String detail;
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	
	public String getDetail() {
		if(StringUtils.isNotBlank(detail)){
			return detail;
		}
		return DefaultState.LABELS.get(state);
	}
	
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	
	public void failed(String detail){
		this.state = DefaultState.FAILED;
		this.detail = detail;
	}
	
	public void succeed(String detail){
		this.state = DefaultState.SUCCEED;
		this.detail = detail;
	}
	
	public String toString(){
		return LangUtils.append(source, " [", detail, "]");
	}
}
