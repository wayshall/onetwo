package org.onetwo.common.web.s2.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.struts2.components.Component;
import org.onetwo.common.web.s2.tag.component.StrutsRow;

import com.opensymphony.xwork2.util.ValueStack;

public class StrutsRowTag extends AbstractTableTag {

	public String type;
	public boolean evalbody;
	
	public StrutsRowTag(){
		this.evalbody = true;
	}
	
	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		return new StrutsRow(stack, req, res);
	}

	@Override
	protected void populateParams() {
		super.populateParams();
		StrutsRow row  = (StrutsRow) component;
		row.setType(type);
		row.setEvalbody(evalbody);
	}
    
    public int doAfterBody() throws JspException {
        boolean again = component.end(pageContext.getOut(), getBody());

        if (again) {
            return EVAL_BODY_AGAIN;
        } else {
            if (bodyContent != null) {
                try {
                    bodyContent.writeOut(bodyContent.getEnclosingWriter());
                } catch (Exception e) {
                    throw new JspException(e.getMessage());
                }
            }
            return SKIP_BODY;
        }
    }
	
    public int doEndTag() throws JspException {
    	this.getComponent().endTag(pageContext.getOut(), getBody());
        component = null;
        return EVAL_PAGE;
    }
    
    public StrutsRow getComponent(){
    	return (StrutsRow) component;
    }

	public void setType(String type) {
		this.type = type;
	}

	public void setEvalbody(boolean evalbody) {
		this.evalbody = evalbody;
	}


}
