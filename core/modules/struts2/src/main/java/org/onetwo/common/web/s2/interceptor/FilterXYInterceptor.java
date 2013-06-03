package org.onetwo.common.web.s2.interceptor;

import java.util.Iterator;
import java.util.Map;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/***
 * 过滤按钮x，y参数的过滤器
 * @author weishao
 *
 */
@SuppressWarnings({"serial", "unchecked"})
public class FilterXYInterceptor extends AbstractInterceptor{

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		Map<String, Object> params = invocation.getInvocationContext().getParameters();
		for(Iterator it = params.keySet().iterator(); it.hasNext() ; ){
			String key = (String) it.next();
			if(key.endsWith(".x") || key.endsWith(".y"))
				it.remove();
		}
		return invocation.invoke();
	}

	
}
