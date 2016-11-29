package org.onetwo.common.web.view.tag;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.DynamicAttributes;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.tags.form.TagWriter;
import org.springframework.web.util.HtmlUtils;

import com.google.common.collect.Maps;

@SuppressWarnings("serial")
abstract public class BaseBodyTag extends BodyTagSupport implements DynamicAttributes {
	
	private Map<String, Object> dynamicAttributes = Maps.newHashMap();
	private String tagName;
	private TagWriter tagWriter;
	
	public BaseBodyTag(String tagName) {
		super();
		this.tagName = tagName;
	}

	@Override
	public int doStartTag() throws JspException {
		tagWriter = createTagWriter();
		writeStartTag();
		writeCustomTagAttributes(tagWriter);
		writeDynamicAttributes();
		tagWriter.forceBlock();
		return super.doStartTag();
	}
	
	protected TagWriter createTagWriter(){
		return new TagWriter(pageContext);
	}

	@Override
	public int doEndTag() throws JspException {
		writeBodyContent();
		writeEndTag();
		return super.doEndTag();
	}

	protected void writeCustomTagAttributes(TagWriter tagWriter) throws JspException{
	}
	
	protected void writeDynamicAttributes() throws JspException{
		try {
			StringBuilder buf = new StringBuilder();
			buildDynamicAttributesHtml(buf);
			write(buf.toString());
		} finally{
			this.dynamicAttributes.clear();
		}
	}

	protected void writeStartTag() throws JspException{
		tagWriter.startTag(tagName);
	}

	protected void writeBodyContent() throws JspException{
		write(getBodyContent().getString());
	}

	protected void writeEndTag() throws JspException{
		/*write("</");
		write(tagName);
		write(">");*/
		tagWriter.endTag();
	}
	protected void write(String content) throws JspException{
		try {
			this.pageContext.getOut().write(content);
		} catch (IOException e) {
			throw new JspException("write content error:"+e.getMessage(), e);
		}
	}
	
	protected ServletRequest getRequest(){
		return this.pageContext.getRequest();
	}

	@Override
	public void setDynamicAttribute(String uri, String localName, Object value) throws JspException {
		this.dynamicAttributes.put(localName, value);
	}
	

	public void buildDynamicAttributesHtml(StringBuilder attributesBuf) {
		for(Entry<String, Object> entry : this.dynamicAttributes.entrySet()){
			buildAttributeTag(attributesBuf, entry.getKey(), entry.getValue());
		}
	}
	
	protected StringBuilder buildAttributeTag(StringBuilder attributesBuf, String attr, Object val){
		String valStr = val==null?"":val.toString();
		if(StringUtils.isBlank(valStr))
			return attributesBuf;
		valStr = HtmlUtils.htmlEscape(valStr);
		attributesBuf.append(" ").append(attr).append("=\"").append(valStr).append("\"");
		return attributesBuf;
	}
	
}
