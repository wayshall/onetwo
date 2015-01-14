package org.onetwo.common.spring.web.mvc.log;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.onetwo.common.log.DataChangedContext;
import org.onetwo.common.utils.LangUtils;

public class OperatorLogInfo implements Serializable {
	
	private long operatorId;
	private String operatorName;
	private Date operatorTime;
	private String url;
	private boolean success = true;
	private String message = "";
	private Map<String, String[]> parameters;
	
	
	private DataChangedContext datas;
	
	public long getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(long operatorId) {
		this.operatorId = operatorId;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public Date getOperatorTime() {
		return operatorTime;
	}
	public void setOperatorTime(Date operatorTime) {
		this.operatorTime = operatorTime;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public DataChangedContext getDatas() {
		return datas;
	}
	public void setDatas(DataChangedContext datas) {
		this.datas = datas;
	}
	public Map<String, String[]> getParameters() {
		return parameters;
	}
	public void addParameter(String name, String... values){
		if(this.parameters==null){
			this.parameters = LangUtils.newHashMap();
		}
		this.parameters.put(name, values);
	}
	public void setParameters(Map<String, String[]> parameters) {
		this.parameters = parameters;
	}
	
}
