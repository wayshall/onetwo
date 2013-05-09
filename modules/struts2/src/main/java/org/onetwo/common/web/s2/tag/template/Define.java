package org.onetwo.common.web.s2.tag.template;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.StrutsConstants;
import org.apache.struts2.StrutsException;
import org.apache.struts2.components.ActionComponent;
import org.apache.struts2.components.template.Template;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.FileUtils;

import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.profiling.UtilTimerStack;

public class Define extends BaseTemplateBean {
	
	public static class DefineTemplate extends Template {

		public DefineTemplate(String path) {
			super("", path, "");
		}
		
	    public String toString() {
	        return this.getTheme();
	    }
		
	}

	private String defaultPage;
	
	private boolean includeBody;

    protected List<String> extensions;
	
    @Inject(StrutsConstants.STRUTS_ACTION_EXTENSION)
    public void setExtensions(String extensions) {
        if (extensions != null && !"".equals(extensions)) {
            List<String> list = new ArrayList<String>();
            String[] tokens = extensions.split(",");
            for (String token : tokens) {
                list.add(token);
            }
            if (extensions.endsWith(",")) {
                list.add("");
            }
            this.extensions = Collections.unmodifiableList(list);
        } else {
            this.extensions = null;
        }
    }
	
	public Define(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
	}

	public String getDefaultPage() {
		return defaultPage;
	}

	public void setDefaultPage(String defaultPage) {
		this.defaultPage = defaultPage;
	}

	@Override
	public boolean start(Writer writer) {
		return true;
	} 

	@Override
	public boolean end(Writer writer, String body) {
		String content = getBodyInContext(name);
		try {
			if(StringUtils.isNotBlank(content)){
	            super.end(writer, content, true);
			}else{
				processDefault(writer, body);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("render define error : " + e.getMessage());
		} 
		return false;
	}
	
	protected void processDefault(Writer writer, String body){
		if(StringUtils.isBlank(defaultPage))
			includeBody = true;
		
		if(includeBody){
			super.end(writer, body, true);
		}else{
			String ext = FileUtils.getExtendName(defaultPage);
			if(this.extensions.contains(ext)){
				try {
					ActionComponent action = new ActionComponent(stack, request, response);
					action.end(writer, "");
				} catch (Exception e) {
		            throw new StrutsException(e);
		        } finally {
		            popComponentStack();
		        }
			}else{
				super.end(writer, body);
			}
		}
	}

    protected Template buildTemplateName(String myTemplate, String myDefaultTemplate) {
		DefineTemplate t = new DefineTemplate(defaultPage);
		return t;
    }

}
