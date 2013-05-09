package org.onetwo.common.web.s2.tag.webtag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.onetwo.common.web.s2.tag.WebUIClosingTag;
import org.onetwo.common.web.s2.tag.webtag.compenent.Out;

import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings("serial")
public class OutTag extends WebUIClosingTag {

    private String defaultValue;
    private String value;
    private boolean escapeHtml = true;
    private boolean escapeJavaScript = false;
    private boolean escapeXml = false;
    private boolean escapeCsv = false;
    
	protected Integer textLength;
	protected String condition;
	protected String otherwise;
	protected String format;
	protected Integer dataIndex;
	protected boolean filterHTML;
	
    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new Out(stack, req, res);
    }

    protected void populateParams() {
        super.populateParams();
        Out out = getComponent();

        out.setDefault(defaultValue);
        out.setValue(value);
        out.setEscape(escapeHtml);
        out.setEscapeJavaScript(escapeJavaScript);
        out.setEscapeXml(escapeXml);
        out.setEscapeCsv(escapeCsv);
        
        out.setEscapeHtml(false);
        out.setTextLength(textLength);
        out.setCondition(condition);
        out.setOtherwise(otherwise);
        out.setFormat(format);
        
        out.setDataIndex(dataIndex);
        
        out.setFilterHTML(filterHTML);
        
        out.setTemplate(template);
    }
    
    public Out getComponent() {
        return (Out)component;
    }

    public void setDefault(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setEscape(boolean escape) {
        this.escapeHtml = escape;
    }

    public void setEscapeHtml(boolean escapeHtml) {
        this.escapeHtml = escapeHtml;
    }

    public void setEscapeJavaScript(boolean escapeJavaScript) {
        this.escapeJavaScript = escapeJavaScript;
    }
    
    public void setValue(String value) {
        this.value = value;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setEscapeCsv(boolean escapeCsv) {
        this.escapeCsv = escapeCsv;
    }

    public void setEscapeXml(boolean escapeXml) {
        this.escapeXml = escapeXml;
    }

	public void setTextLength(Integer textLength) {
		this.textLength = textLength;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public void setOtherwise(String otherwise) {
		this.otherwise = otherwise;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public void setDataIndex(Integer dataIndex) {
		this.dataIndex = dataIndex;
	}

	public void setFilterHTML(boolean filterHTML) {
		this.filterHTML = filterHTML;
	}
}
