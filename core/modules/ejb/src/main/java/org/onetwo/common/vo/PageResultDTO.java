package org.onetwo.common.vo;

import java.io.Serializable;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.Page;
import org.onetwo.common.ws.WSResult;

@SuppressWarnings("serial")
/*****
 * 带执行状态的分页DTO
 * @author wayshall
 *
 * @param <T>
 */
public class PageResultDTO<T> implements Serializable {

	public static <T> PageResultDTO<T> create(Exception e){
		PageResultDTO<T> result = new PageResultDTO<T>();
		result.getResult().setSuccess(false);
		if(e instanceof ServiceException){
			ServiceException se = (ServiceException) e;
			result.setResultCode(se.getCode());
		}
		result.setMessage(WSResult.MSG_FAIL+":"+e.getMessage());
		return result;
	}

	public static <T> PageResultDTO<T> create(){
		PageResultDTO<T> result = new PageResultDTO<T>();
		result.setMessage(WSResult.MSG_SUCCESS);
		return result;
	}
	
	private Page<T> page;
	
	/********
	 * 执行结果状态
	 */
	private WSResult result;
	
	public PageResultDTO(){
		this.page = new Page<T>();
		this.result = new WSResult(true);
	}

	public Page<T> getPage() {
		return page;
	}

	public void setPage(Page<T> page) {
		this.page = page;
	}

	private WSResult getResult() {
		return result;
	}

	public Boolean isSuccess() {
		return result.isSuccess();
	}

	public void setSuccess(Boolean success) {
		result.setSuccess(success);
	}

	public String getMessage() {
		return result.getMessage();
	}

	public void setMessage(String message) {
		result.setMessage(message);
	}

	public String getResultCode() {
		return result.getResultCode();
	}

	public void setResultCode(String resultCode) {
		result.setResultCode(resultCode);
	}
	
}
