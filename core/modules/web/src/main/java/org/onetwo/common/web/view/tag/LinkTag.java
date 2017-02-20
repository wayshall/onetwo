package org.onetwo.common.web.view.tag;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.apache.utils.URLEncoder;
import org.onetwo.common.web.utils.WebHolder;
import org.springframework.web.servlet.tags.form.TagWriter;

@SuppressWarnings("serial")
public class LinkTag extends BaseBodyTag {
	
	private String href;
	private boolean keepQueryString = true;
	
	public LinkTag() {
		super("a");
	}

	@Override
	public void writeCustomTagAttributes(TagWriter tagWriter) throws JspException {
		String linkAddress = href;
		
		if(keepQueryString){
			String qs = WebHolder.getSpringContextHolderRequest().get().getQueryString();
			if(StringUtils.isBlank(qs)){
				writeHref(tagWriter, linkAddress);
				return ;
			}
			linkAddress += linkAddress.contains("?")?"&":"?";
			linkAddress += qs;
		}
		writeHref(tagWriter, linkAddress);
	}
	
	private void writeHref(TagWriter tagWriter, String linkAddress) throws JspException{
		linkAddress = URLEncoder.SAFE_PARAMS_ENCODER.encode(linkAddress);
		tagWriter.writeAttribute("href", linkAddress);
	}

	public void setHref(String href) {
		this.href = href;
	}

	public void setKeepQueryString(boolean keepQueryString) {
		this.keepQueryString = keepQueryString;
	}

}
