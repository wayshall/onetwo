package org.onetwo.common.web.s2.interceptor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.onetwo.common.utils.annotation.DataFilter;
import org.onetwo.common.web.data.Filter;
import org.onetwo.common.web.s2.CrudBaseAction;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

@SuppressWarnings({"serial", "unchecked"})
public class HtmlDataFilterInterceptor extends MethodFilterInterceptor {

	private final List<String> includeMethoed = new ArrayList<String>() {
		{
			add("save");
			add("update");
			add("draft");
			add("saveDraft");
		}
	};
	
	@Resource
	private Filter filter;

	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		if(includeMethoed.contains(invocation.getProxy().getMethod()) && invocation.getAction() instanceof CrudBaseAction) {
			Object model = ((CrudBaseAction)invocation.getAction()).getModel();
			
			Field[] filds = model.getClass().getDeclaredFields();
			
			for(Field f : filds) {
				if(f.getAnnotation(DataFilter.class) != null) {
					f.setAccessible(true);
					
					Object rs = filter.doFilter(f.get(model));
					f.set(model, rs);
				}
			}
			
		}
		return invocation.invoke();
	}
	

}
