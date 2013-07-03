package org.onetwo.common.web.s2.tag.webtag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.struts2.components.Component;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.web.s2.tag.WebUIClosingTag;
import org.onetwo.common.web.s2.tag.webtag.compenent.Image;

import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings("serial")
public class ImageTag extends WebUIClosingTag {

	private String var;
	private String src;
	private Integer width;
	private Integer height;
	private String alt;
	
	private Integer dataIndex;
	private String _default;

	private String location;
	
	private boolean lazy;
	
	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		return new Image(stack, req, res);
	}

	protected void populateParams(){
		super.populateParams();
		Image image = (Image)getComponent();
		image.setWidth(width);
		image.setHeight(height);
		image.setVar(var);
		image.setSrc(src);
		image.setAlt(alt);
		image.setTitle(title);
		
		image.setDefault(_default);
		image.setDataIndex(dataIndex);
		image.setLocation(location);
		
		image.setLazy(lazy);
	}
	

    public int doEndTag() throws JspException {
		int rs = EVAL_PAGE;
		try {
//	        component.end(pageContext.getOut(), getBody());
	        rs = super.doEndTag();
		} catch (Exception e) {
			String msg = "parse image tag["+this.getClass()+", name:"+name+", src="+src+"] end error";
			logger.error(msg, e);
			throw new ServiceException(msg);
		}
        return rs;
    }
	
	public void setVar(String var) {
		this.var = var;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public void setDataIndex(Integer dataIndex) {
		this.dataIndex = dataIndex;
	}

	public void setDefault(String _default) {
		this._default = _default;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	public void setLazy(boolean lazy) {
		this.lazy = lazy;
	}
	
}
