package org.onetwo.boot.core.web.mvc.exception;

import lombok.Data;

/**
 * @author weishao zeng
 * <br/>
 */
@Data
public class ExceptionMapping {
	private String code;
	private String mesage;
	boolean detail;
	private Integer httpStatus;
	private String viewName;
	/***
	 * 是否发送邮件通知
	 */
	boolean notify;
}
