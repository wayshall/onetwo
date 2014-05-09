package org.onetwo.plugins.rest;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
public class BaseRestResult implements Serializable {

	/**
	 * 返回值
	 */
	private Integer ret_flag;
	/**
	 * 错误代码
	 */
	private String error_code;
	/**
	 * 返回信息
	 */
	private String ret_msg;
	
	private Object[] errorArgs;
	
	public BaseRestResult(){
		this.markSucceed();
	}

	@JsonIgnore
	public boolean isSucceed(){
		return ErrorCode.RET_SUCCEED.equals(ret_flag);
	}

	@JsonIgnore
	public boolean isFailed(){
		return ErrorCode.RET_FAILED.equals(ret_flag);
	}
	@JsonIgnore
	public final void markSucceed(){
		this.ret_flag = ErrorCode.RET_SUCCEED;
	}
	@JsonIgnore
	public final void markAsfailed(){
		this.ret_flag = ErrorCode.RET_FAILED;
	}
	public String getError_code() {
		return error_code;
	}
	public void setError_code(String error_code, Object...args) {
		this.markAsfailed();
		this.error_code = error_code;
		this.errorArgs = args;
	}
	public String getRet_msg() {
		return ret_msg;
	}
	public void setRet_msg(String ret_msg) {
		this.ret_msg = ret_msg;
	}
	public Integer getRet_flag() {
		return ret_flag;
	}
	public void setRet_flag(Integer ret_flag) {
		this.ret_flag = ret_flag;
	}

	@JsonIgnore
	public Object[] getErrorArgs() {
		return errorArgs;
	}

	public void setErrorArgs(Object... errorArgs) {
		this.errorArgs = errorArgs;
	}

}
