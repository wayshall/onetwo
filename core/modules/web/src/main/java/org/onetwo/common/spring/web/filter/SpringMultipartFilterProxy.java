package org.onetwo.common.spring.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.result.MapResult;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.common.web.utils.ResponseType;
import org.onetwo.common.web.utils.ResponseUtils;
import org.slf4j.Logger;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MultipartFilter;

public class SpringMultipartFilterProxy extends MultipartFilter {
	
	private final Logger logger = JFishLoggerFactory.getLogger(SpringMultipartFilterProxy.class);
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			super.doFilterInternal(request, response, filterChain);
		} catch (MaxUploadSizeExceededException e) {
			String msg = "文件超过限制："+LangUtils.getCauseServiceException(e).getMessage();
			logger.error(msg);
			if(RequestUtils.getResponseType(request)==ResponseType.JSON){
				MapResult dataResult = MapResult.error(msg);
				ResponseUtils.renderObjectAsJson(response, dataResult);
			}else{
				response.getWriter().print(msg);
			}
		}
	}
}
