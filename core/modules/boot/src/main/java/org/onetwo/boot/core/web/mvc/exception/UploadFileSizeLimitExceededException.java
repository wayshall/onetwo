package org.onetwo.boot.core.web.mvc.exception;

import org.onetwo.boot.core.web.utils.WebErrors;
import org.onetwo.common.exception.ServiceException;

/**
 * @author weishao zeng
 * <br/>
 */
public class UploadFileSizeLimitExceededException extends ServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3441768247450521557L;
	
	public UploadFileSizeLimitExceededException(long maxUploadSize, long actualSize) {
		super("单个文件上传超过了限制. maxUploadSize: " + maxUploadSize + 
				"(bytes), actualSize: " + actualSize + "(bytes)", 
				WebErrors.UPLOAD_FILE_SIZE_LIMIT_EXCEEDED.getErrorCode());
	}

	public UploadFileSizeLimitExceededException(Throwable cause, String message) {
		super(WebErrors.UPLOAD_FILE_SIZE_LIMIT_EXCEEDED, cause, message);
	}
}

