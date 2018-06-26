package org.onetwo.boot.core.web.view;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.onetwo.common.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

/**
 * 把渲染逻辑封装成一个服务，逻辑主要参考dispatcherServlet
 * @author wayshall
 * <br/>
 */
@Slf4j
public class MvcViewRender {

	@Autowired(required=false)
	private LocaleResolver localeResolver;
	@Autowired(required=false)
	private List<ViewResolver> viewResolvers;
	

	public void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response)  {
		try {
			doRender(mv, request, response);
		}
		catch (Exception ex) {
			if (log.isDebugEnabled()) {
				log.debug("Error rendering view [" + mv + "] in DispatcherServlet with name '" + getClass().getSimpleName() + "'", ex);
			}
			try {
				response.sendError(HttpStatus.NOT_FOUND.value(), ex.getMessage());
			} catch (IOException e) {
				log.error("send http status error.", e);
			}
		}
	}
	public void doRender(ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(localeResolver!=null){
			Locale locale = this.localeResolver.resolveLocale(request);
			response.setLocale(locale);
		}
		
		View view;
		if (mv.isReference()) {
			view = resolveViewName(mv.getViewName(), mv.getModel(), response.getLocale(), request);
			if (view == null) {
				throw new BaseException("Could not resolve view with name '" + mv.getViewName() + "' in '" + getClass().getSimpleName() + "'");
			}
		}
		else {
			view = mv.getView();
			if (view == null) {
				throw new BaseException("ModelAndView [" + mv + "] neither contains a view name nor a " + "View object in '" + getClass().getSimpleName() + "'");
			}
		}

		if (mv.getStatus() != null) {
			response.setStatus(mv.getStatus().value());
		}
		view.render(mv.getModel(), request, response);
	}
	
	protected View resolveViewName(String viewName, Map<String, Object> model, Locale locale,
			HttpServletRequest request) throws Exception {

		for (ViewResolver viewResolver : this.viewResolvers) {
			View view = viewResolver.resolveViewName(viewName, locale);
			if (view != null) {
				return view;
			}
		}
		return null;
	}

}
