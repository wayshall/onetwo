package org.onetwo.common.web.s2.interceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.DefaultWorkflowInterceptor;

@SuppressWarnings("serial")
public class SimpleWorkflowInterceptor extends DefaultWorkflowInterceptor {

    @Override
    protected String doIntercept(ActionInvocation invocation) throws Exception {
        return super.doIntercept(invocation);
    }
}
