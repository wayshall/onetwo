package org.onetwo.common.spring.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.slf4j.Logger;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MultipartFilter;

public class SpringMultipartFilterProxy extends MultipartFilter {
	
	private final Logger logger = MyLoggerFactory.getLogger(SpringMultipartFilterProxy.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			super.doFilterInternal(request, response, filterChain);
		} catch (MaxUploadSizeExceededException e) {
			String msg = "文件超过限制："+LangUtils.getCauseServiceException(e).getMessage();
			response.getWriter().print(msg);
		} catch (Exception e){
			logger.error("上传出错："+e.getMessage(), e);
		}
	}
}
