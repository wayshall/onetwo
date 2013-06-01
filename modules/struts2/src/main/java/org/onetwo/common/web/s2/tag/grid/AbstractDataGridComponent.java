package org.onetwo.common.web.s2.tag.grid;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.web.s2.tag.WebUIClosingBean;
import org.onetwo.common.web.utils.RequestUtils;

import com.opensymphony.xwork2.util.ValueStack;

public class AbstractDataGridComponent extends WebUIClosingBean {

	public static final String DATAGRID_THEME = "datagrid";
	

	protected int colspan = 0;
	protected String bodyContent;
	
	public AbstractDataGridComponent(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
	}

	/*@Override
	protected void evaluateExtraParams() {
		super.evaluateExtraParams();
	}*/
	
	public boolean end(Writer writer, String body) {
    	this.bodyContent = body;
    	return super.end(writer, body);
	}

	public int getColspan() {
		return colspan;
	}

	public void setColspan(int colspan) {
		this.colspan = colspan;
	}
	public boolean usesBody(){
		return true;
	}

	public String getBodyContent() {
		return bodyContent;
	}
	public String getTemplate(){
		if(StringUtils.isBlank(template))
			return template;
		if(template.startsWith("/"))
			return template;
		if(template.startsWith("@"))
			return template.substring(1);//parameters.getTemplateDir....
		String path = this.request.getRequestURI();
		path = RequestUtils.noContextPath(request, path);
		path = FileUtils.getParentpath(path);
		template = path + "/" + template;
		return template;
	}

    public String getTheme() {
        String theme = null;

        if (this.theme != null) {
            theme = findString(this.theme);
        }

        if ((theme == null) || (theme.equals(""))) {
            theme = DATAGRID_THEME;
        }

        return theme;
    }

}
