package org.onetwo.boot.core.web;

import javax.servlet.http.HttpServletResponse;

public interface WebRender {

	void render(HttpServletResponse response, String name, Object dataModel);

}