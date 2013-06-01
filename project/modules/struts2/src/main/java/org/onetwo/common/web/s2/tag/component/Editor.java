package org.onetwo.common.web.s2.tag.component;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ognl.Ognl;
import ognl.OgnlException;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.StrutsException;
import org.apache.struts2.components.UIBean;
import org.onetwo.common.web.s2.tag.FCKUtils;
import org.onetwo.common.web.utils.StrutsUtils;

import com.opensymphony.xwork2.util.ValueStack;


@SuppressWarnings("unchecked")
public class Editor extends UIBean{

	public static final String DEFAULT_UI_THEME = "editor";
	
	public static final String TEMPLATE_SWITCH = "fck";

	public Editor(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
	}

	@Override
	protected void evaluateExtraParams() {
		super.evaluateExtraParams();
		if(StringUtils.isBlank(this.theme)){
			this.theme = DEFAULT_UI_THEME;
		}
		this.id = this.name.replace('.', '_');
		this.addParameter("id", id);
		this.addParameter("language", StrutsUtils.getSessionLanguage());
	}
	
    public boolean end(Writer writer, String body) {
        evaluateParams();
		
		String value = (String) this.parameters.get("nameValue");
		
//		getStack().getContext().put("fckeditor", new FCKeditorWrapper(this.request));
		value = FCKUtils.decodeFckText(value);
		this.addParameter("nameValue", value);
		
        try {
            super.end(writer, body, false);
            mergeTemplate(writer, buildTemplateName(template, getDefaultTemplate()));
        } catch (Exception e) {
            throw new StrutsException(e);
        }
        finally {
            popComponentStack();
        }

        return false;
    }

	@Override
	protected String getDefaultTemplate() {
		return TEMPLATE_SWITCH;
	}
	
	
	public static void main(String[] args) throws OgnlException{
		String r = "^/(?!fckeditor)(\\w+)/(\\w+)/(\\w+)\\.html$";
		String text = "/zh/project/index.html";
		text = text.replaceAll(r, "/$1/$2!$3.do");
		System.out.println("text : " + text);
		
		Map map = new HashMap();
		map.put("aa", "aa-text");
		Object value = Ognl.getValue("bb", (Object)map, String.class);
		System.out.println("value: " + value);
	}

}
