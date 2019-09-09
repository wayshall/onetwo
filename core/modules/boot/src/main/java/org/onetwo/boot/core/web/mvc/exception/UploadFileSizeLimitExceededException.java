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

	public UploadFileSizeLimitExceededException(Throwable cause) {
		super(WebErrors.UPLOAD_FILE_SIZE_LIMIT_EXCEEDED, cause);
	}
}

