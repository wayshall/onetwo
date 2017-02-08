package org.onetwo.common.spring.mvc.log;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.onetwo.common.log.DataChangedContext;
import org.onetwo.common.utils.LangUtils;

@SuppressWarnings("serial")
public class OperatorLogInfo implements Serializable {
	
	private long operatorId;
	private String operatorName;
	private Date operatorTime;
	private String url;
	private String remoteAddr;
	private boolean success = true;
	private String message = "";
	private Map<String, String[]> parameters;
	final private long startTime;
	final private long endTime;
	private String webHandler;
	
	public OperatorLogInfo(long startTime, long endTime) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
	}
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
	public String getRemoteAddr() {
		return remoteAddr;
	}
	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}
	
	public long getExecutedTimeInMillis(){
		return endTime - startTime;
	}
	
	public long getExecutedTimeInSeconds(){
		return TimeUnit.MILLISECONDS.toSeconds(getExecutedTimeInMillis());
	}
	public String getWebHandler() {
		return webHandler;
	}
	public void setWebHandler(String webHandler) {
		this.webHandler = webHandler;
	}
}
