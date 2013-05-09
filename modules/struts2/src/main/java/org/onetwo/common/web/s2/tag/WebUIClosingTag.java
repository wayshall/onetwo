package org.onetwo.common.web.s2.tag;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.struts2.views.jsp.ui.AbstractClosingTag;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.TimeCounter;

import com.opensymphony.xwork2.inject.Container;

@SuppressWarnings("unchecked")
abstract public class WebUIClosingTag extends AbstractClosingTag{
	
	protected Logger logger = Logger.getLogger(this.getClass());
	
	protected String params;
	protected TimeCounter counter;
	protected boolean debug;
	
	public WebUIClosingTag(){
	}

	protected void populateParams(){
		super.populateParams();

		WebUIClosingBean uibean = (WebUIClosingBean) getComponent();
		if(StringUtils.isNotBlank(this.params)){
			this.params = this.params.trim();
			if(!this.params.startsWith("{"))
				this.params = "{" + this.params;
			if(!this.params.endsWith("}"))
				this.params = this.params + "}";
			JSONObject jsonObj = JSONObject.fromObject(this.params);
			Map map = (Map) JSONObject.toBean(jsonObj, HashMap.class);
			uibean.setParams(map); 
			if(map.get("debug")!=null)
				this.debug = (Boolean)map.get("debug");
			else
				this.debug = false;
		}
		if(this.debug){
			this.counter = new TimeCounter(this);
			this.counter.start();
		}
		
	}
	
	public void setParams(String params) {
		this.params = params;
	}
	
	public WebUIClosingBean getComponent(){
		return (WebUIClosingBean)component;
	}

    public int doStartTag() throws JspException {
        component = getBean(getStack(), (HttpServletRequest) pageContext.getRequest(), (HttpServletResponse) pageContext.getResponse());
        Container container = Dispatcher.getInstance().getContainer();
        container.inject(component);

        populateParams();
        getComponent().afterPropertySet();
        boolean evalBody = false;
        try {
            evalBody = component.start(pageContext.getOut());
		} catch (Exception e) {
			String msg = "parse tag["+this.getClass()+", id:"+id+", name:"+name+", value="+value+"] start error";
			logger.error(msg, e);
			throw new ServiceException(msg);
		}

        if (evalBody) {
            return component.usesBody() ? EVAL_BODY_BUFFERED : EVAL_BODY_INCLUDE;
        } else {
            return SKIP_BODY;
        }
    }

    public int doEndTag() throws JspException {
		if(this.debug){
			this.counter.stop();
		}
		int rs = EVAL_PAGE;
		try {
//	        component.end(pageContext.getOut(), getBody());
	        rs = super.doEndTag();
		} catch (Exception e) {
			String msg = "parse tag["+this.getClass()+", id:"+id+", name:"+name+", value="+value+"] end error";
			logger.error(msg, e);
			throw new ServiceException(msg);
		}
        return rs;
    }
}
