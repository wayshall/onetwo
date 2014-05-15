package org.onetwo.common.fish.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.common.web.filter.WebFilterAdapter;

public class ThreadLocalCleaner extends WebFilterAdapter{

	@Override
	public void onFinally(HttpServletRequest request, HttpServletResponse response) {
		JFishHolder.setJdbcContext(null);
	}

	

}
