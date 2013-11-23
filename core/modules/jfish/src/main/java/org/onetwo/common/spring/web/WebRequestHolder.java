package org.onetwo.common.spring.web;

import org.onetwo.common.fish.utils.ContextHolder;
import org.onetwo.common.log.DataChangedContext;
import org.onetwo.common.spring.web.utils.JFishWebUtils;
import org.springframework.web.context.request.RequestContextHolder;

public class WebRequestHolder implements ContextHolder {

	public static final String CONTEXT_KEY = "_ContextHolder_";
	public static final String CONTEXT_DATACHANGEDCONTEXT_KEY = "_ContextHolder_"+DataChangedContext.class;


	protected String getName(Class<?> clazz){
		return CONTEXT_KEY + clazz.getName();
	}
	protected String getName(String attrName){
		return CONTEXT_KEY + attrName;
	}


	@Override
	public DataChangedContext getDataChangedContext() {
		DataChangedContext context = JFishWebUtils.req(CONTEXT_DATACHANGEDCONTEXT_KEY);
		if(context==null){
			context = new DataChangedContext();
			JFishWebUtils.req(CONTEXT_DATACHANGEDCONTEXT_KEY, context);
		}
		return context;
	}


	@Override
	public void setDataChangedContext(DataChangedContext context) {
		JFishWebUtils.req(CONTEXT_DATACHANGEDCONTEXT_KEY, context);
	}


	@Override
	public <T> void setContextAttribute(String attrName, T attr) {
		JFishWebUtils.req(getName(attrName), attr);
	}


	@Override
	public <T> T getContextAttribute(String attrName) {
		if(RequestContextHolder.getRequestAttributes()==null)
			return null;
		T context = JFishWebUtils.req(getName(attrName));
		return context;
	}

	
}
