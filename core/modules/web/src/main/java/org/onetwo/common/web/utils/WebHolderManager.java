package org.onetwo.common.web.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.onetwo.common.web.filter.WebFilterAdapter;
import org.springframework.core.Ordered;

@Deprecated
public class WebHolderManager extends WebFilterAdapter implements Ordered {

	@Override
	public void onFilter(HttpServletRequest request, HttpServletResponse response) {
//		System.out.println("url: " + request.getRequestURL()+" " + RequestUtils.getBrowerByAgent(request));
		WebHolder.initHolder(request, response);
//		System.out.println("url: " + request.getRequestURL());
	}

	@Override
	public void onFinally(HttpServletRequest request, HttpServletResponse response) {
		WebHolder.reset();
//		JdbcContextHolder.reset();
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

}
