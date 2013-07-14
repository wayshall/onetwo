package org.onetwo.plugins.codegen;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.onetwo.plugins.codegen.generator.DefaultTableManagerFactory;
import org.onetwo.plugins.codegen.model.entity.TemplateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

public class DatabaseInterceptor implements HandlerInterceptor{

	@Autowired
	private DefaultTableManagerFactory tableManagerFactory;

	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		List<String> tables = this.tableManagerFactory.getDefaultTableManager().getTableNames(true);
		if(!tables.contains(TemplateEntity.TABLE_NAME)){
			ModelAndView mv = new ModelAndView("codegen-index");
			throw new ModelAndViewDefiningException(mv);
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
