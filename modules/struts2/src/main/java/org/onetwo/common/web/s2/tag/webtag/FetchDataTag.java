package org.onetwo.common.web.s2.tag.webtag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;
import org.apache.struts2.views.jsp.IteratorTag;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.web.s2.tag.webtag.compenent.FetchData;

import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings("serial")
public class FetchDataTag extends IteratorTag {
	protected Logger logger = Logger.getLogger(this.getClass());
	
	protected String dataSource;
	protected Integer firstResult = 0;
	protected Integer maxResults = -1;
	protected String orderAsc;
	protected String orderDesc;
	protected String params;
	protected String type;
	protected Integer index;

    public FetchData getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new FetchData(stack);
    }

    protected void populateParams() {
    	super.populateParams();
    	FetchData dlist = (FetchData) getComponent();
    	dlist.setDataSource(dataSource);
    	dlist.setParams(params);
    	dlist.setFirstResult(firstResult);
    	dlist.setMaxResults(maxResults);
    	dlist.setOrderAsc(orderAsc);
    	dlist.setOrderDesc(orderDesc);
    	dlist.setType(type);
    	dlist.setIndex(index);
    }
    

    public int doStartTag() throws JspException {
    	int rs = SKIP_BODY;
    	try {
			rs = super.doStartTag();
		} catch (Exception e) {
			String msg = "parse tag["+this.getClass()+", dataSource:"+this.dataSource+"] start error";
			logger.error(msg, e);
			throw new ServiceException(msg);
		}
		return rs;
    }
	
    public int doEndTag() throws JspException {
    	int rs = EVAL_PAGE;
    	try {
    		getComponent().endTag(pageContext.getOut(), getBody());
            rs = super.doEndTag();
		} catch (Exception e) {
			String msg = "parse tag["+this.getClass()+", dataSource:"+this.dataSource+"] end error";
			logger.error(msg, e);
			throw new ServiceException(msg);
		}
		return rs;
    }
    
    public int doAfterBody() throws JspException {
    	int rs = SKIP_BODY;
    	try {
            rs = super.doAfterBody();
		} catch (Exception e) {
			String msg = "parse tag["+this.getClass()+", dataSource:"+this.dataSource+"] after error";
			logger.error(msg, e);
			throw new ServiceException(msg);
		}
		return rs;
    }

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public void setFirstResult(int firstResult) {
		this.firstResult = firstResult;
	}

	public void setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}


    public FetchData getComponent() {
        return (FetchData)component;
    }

	public void setOrderAsc(String orderAsc) {
		this.orderAsc = orderAsc;
	}

	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}
}
