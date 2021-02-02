package org.onetwo.boot.core.web.mvc.log;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.onetwo.common.web.utils.WebContextUtils;
import org.onetwo.common.web.utils.WebHolder;

/**
 * @author weishao zeng
 * <br/>
 */
abstract public class OperationLogs {
	
	static private final String OPERATION_LOG_KEY = "__OPERATION_LOG_KEY__";
	
	public static OperatorLogInfo initLogInfo(HttpServletRequest request){
		String url = request.getMethod() + "|" + request.getRequestURL();
		long start = System.currentTimeMillis();
		OperatorLogInfo info = new OperatorLogInfo(url, start);
		
		WebContextUtils.attr(request, OPERATION_LOG_KEY, info);
		return info;
	}

	public static Optional<OperatorLogInfo> getCurrentLogInfo(){
		return WebHolder.getRequest().map(request -> {
			return WebContextUtils.getAttr(request, OPERATION_LOG_KEY);
		});
	}

	public static OperatorLogInfo getLogInfo(HttpServletRequest request){
		return WebContextUtils.getAttr(request, OPERATION_LOG_KEY);
	}

}
