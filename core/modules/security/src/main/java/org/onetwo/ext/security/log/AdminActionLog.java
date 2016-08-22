package org.onetwo.ext.security.log;

import java.io.Serializable;
import java.util.Date;

public class AdminActionLog implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long userId;
    private String userName;
    private Date actionTime;
    private String permissionCode;
    private String permissionName;
    private String actionUrl;
    private String httpMethod;
    private String operatorIp;
    private String actionInput;
    private Boolean isSuccess;
    private String actionOutput;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Date getActionTime() {
		return actionTime;
	}
	public void setActionTime(Date actionTime) {
		this.actionTime = actionTime;
	}
	public String getPermissionCode() {
		return permissionCode;
	}
	public void setPermissionCode(String permissionCode) {
		this.permissionCode = permissionCode;
	}
	public String getPermissionName() {
		return permissionName;
	}
	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}
	public String getActionUrl() {
		return actionUrl;
	}
	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}
	public String getHttpMethod() {
		return httpMethod;
	}
	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}
	public String getOperatorIp() {
		return operatorIp;
	}
	public void setOperatorIp(String operatorIp) {
		this.operatorIp = operatorIp;
	}
	public String getActionInput() {
		return actionInput;
	}
	public void setActionInput(String actionInput) {
		this.actionInput = actionInput;
	}
	public Boolean getIsSuccess() {
		return isSuccess;
	}
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getActionOutput() {
		return actionOutput;
	}
	public void setActionOutput(String actionOutput) {
		this.actionOutput = actionOutput;
	}
    

}