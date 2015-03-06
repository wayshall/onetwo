package org.ajaxanywhere.jsf;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentBodyTag;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.Tag;

/**
 * @jsp.tag name = "zoneJSF"
 * display-name = "zoneJSF"
 * description = ""
 */ 
public class ZoneJSFTag implements Tag, BodyTag {
    public String id;
    private String idSuffix;

    private static final String COMPONENT_TYPE = "org.ajaxanywhere.ZoneUIComponent";
    private UIComponentBodyTag uiComponentBodyTag;


    public ZoneJSFTag() {
        uiComponentBodyTag = new UIComponentBodyTag() {
        	
        	private String idSuffix;
        	
            public String getComponentType() {
                return COMPONENT_TYPE;
            }

            public String getRendererType() {
                return null;
            }
            
            protected void setProperties(UIComponent component) {
            	super.setProperties(component);
            	setValueBindingProperty(component, "idSuffix", idSuffix);
            }
            
            protected UIComponent findComponent(FacesContext context) throws JspException {
            	return super.findComponent(context);
            }               
            
        	void setValueBindingProperty(UIComponent component, String attrName, String value)
        	{
        		if (value==null) return;
        		
        		if (!UIComponentTag.isValueReference(value))
        		{
        			throw new IllegalArgumentException("idSuffix must be an EL expression!");
        		}
        		else
        		{
        			component.setValueBinding(attrName, createValueBinding(value));
        		}
        	}                
        	
        	ValueBinding createValueBinding(String value)
        	{
        		FacesContext ctx = FacesContext.getCurrentInstance();
        		Application app = ctx.getApplication();
        		return app.createValueBinding(value);
        	}

			public String getIdSuffix() {
				return idSuffix;
			}

			public void setIdSuffix(String idSuffix) {
				this.idSuffix = idSuffix;
			}        
        };
    }

    public void release() {
        id = null;
        idSuffix = null;
        uiComponentBodyTag.release();
    }

    public String getId() {
        return id;
    }

    public void setPageContext(PageContext pageContext) {
        id = null;
        idSuffix = null;
        uiComponentBodyTag.setPageContext(pageContext);
    }

    public Tag getParent() {
        return uiComponentBodyTag.getParent();
    }

    public void setParent(Tag parent) {
        uiComponentBodyTag.setParent(parent);
    }

    public int doStartTag() throws JspException {
        return uiComponentBodyTag.doStartTag();
    }

    public int doEndTag() throws JspException {
        return uiComponentBodyTag.doEndTag();
    }


    public int doAfterBody() throws JspException {
        return uiComponentBodyTag.doAfterBody();
    }

    public void doInitBody() throws JspException {
        uiComponentBodyTag.doInitBody();
    }


    public void setBodyContent(BodyContent bodyContent) {
        uiComponentBodyTag.setBodyContent(bodyContent);
    }

    /**
     * @param id String
     * @jsp.attribute required="false"
     * rtexprvalue="true"
     * type="java.lang.String"
     * description="name description"
     */
    public void setId(String id) {
        this.id = id;
        uiComponentBodyTag.setId(id);
    }

	public String getIdSuffix() {
		return idSuffix;
	}

    /**
     * @param idSuffix String
     * @jsp.attribute required="false"
     * rtexprvalue="true"
     * type="java.lang.String"
     * description="the idSuffix can be set to en EL expression, which is appended to the id to form the resulting id of the aazone span html element. This allows to create dynamic zones e.g. in a datatable"
     */	
	public void setIdSuffix(String idSuffix) {
		this.idSuffix = idSuffix;
		try {
			uiComponentBodyTag.getClass().getMethod("setIdSuffix",	new Class[] { String.class }).invoke(uiComponentBodyTag, new Object[] { idSuffix });
		} catch (Exception e) {
			IllegalStateException ise = new IllegalStateException("Couldn't invoke setDynamicId on inner class");
			ise.initCause(e);
			throw ise;
		}        
	}	
}
