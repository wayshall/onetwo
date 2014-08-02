package org.onetwo.common.web.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.fish.utils.JFishHolder;
import org.onetwo.common.web.filter.WebFilterAdapter;
import org.springframework.core.Ordered;

public class WebHolderManager extends WebFilterAdapter implements Ordered {

	@Override
	public void onFilter(HttpServletRequest request, HttpServletResponse response) {
		WebHolder.setRequest(request);
	}

	@Override
	public void onFinally(HttpServletRequest request, HttpServletResponse response) {
		WebHolder.reset();
		JFishHolder.reset();
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

}
