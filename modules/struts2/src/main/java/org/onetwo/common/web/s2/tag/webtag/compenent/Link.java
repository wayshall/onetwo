package org.onetwo.common.web.s2.tag.webtag.compenent;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.StrutsException;
import org.apache.struts2.components.Component;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.web.config.SiteConfig;
import org.onetwo.common.web.s2.tag.WebUIClosingBean;
import org.onetwo.common.web.s2.tag.webtag.LinkAdapter;
import org.onetwo.common.web.s2.tag.webtag.TemplateTagManagerFactory;
import org.onetwo.common.web.s2.tag.webtag.VarKey;

import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings({"unchecked", "serial"})
public class Link extends WebUIClosingBean {
	public static final String VALUE_KEY = "link_value";
	
	public static final String TEMPLATE_OPEN = "link-open";
	public static final String TEMPLATE_CLOSE = "link-close";
	
	public static class Type {
		public static final String TEXT = "link-text";
		public static final String IMG = "link-img";
		
		public static final List<String> VALUES = new ArrayList<String>(){
			{
				add(TEXT);
				add(IMG);
			}
		};
		
		public static boolean contains(String type){
			return VALUES.contains(type);
		}
		
		public static boolean isImageLink(String type){
			return IMG.equals(type);
		}
		
		public static boolean isTextLink(String type){
			return TEXT.equals(type);
		}
	}

	private String var;
	private String target;
	private String image;
	private String href;
	private String text;
	private String type;
	
	private Integer dataIndex;
	
	private Object actualHref;
	private String actualImage;
	private String actualText;
	
	private String condition;
	
	private Object nameValue;
	
	private boolean popValue = false;
	
	private FetchData parentFetcher;
	private Component parentComponent;
	
	private LinkAdapter linkAdapter;
	
	private boolean ignore;
	
	public Link(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
		parentFetcher = (FetchData)this.findAncestor(FetchData.class);
		if(parentFetcher==null)
			parentComponent = this.findAncestor(Component.class);
	}

	@Override
	protected void evaluateExtraParams() {
		super.evaluateExtraParams();
		linkAdapter = (LinkAdapter) TemplateTagManagerFactory.getDefault().getLinkAdapter(href);
		
		nameValue = getParameters().get("nameValue");
		
		if(nameValue==null){
			if(parentFetcher != null)
				nameValue = parentFetcher.datas;
		}
		
		if(dataIndex!=null && nameValue != null){
			if(!List.class.isAssignableFrom(nameValue.getClass()))
				throw new ServiceException("the data["+nameValue+"] must be a list!");
			List datalist = (List) nameValue;
			if(datalist.isEmpty() || dataIndex>datalist.size()-1){
				nameValue = null;
				return;
			}
				//throw new IndexOutOfBoundsException("the dataIndex is out of index. [size:"+datalist.size()+", dataIndex:"+dataIndex + "]");
			nameValue = datalist.get(dataIndex);
		}
		
		if(linkAdapter != null && nameValue == null) {
			nameValue = super.findValue(value);
		}
	}
	
	protected void findPropertyValue(){
		this.type = (String)getParameters().get("type", true, Type.TEXT);
		boolean showImg = Type.isImageLink(type);
		
		actualText = text;
		if(linkAdapter!=null){
			if(nameValue==null){
				String msg = String.format("link tag error: [id=%s, name=%s, href=%s, value=%s, text=%s], parent:[dataSource=%s]", id, name, href, value, text, parentFetcher!=null?parentFetcher.dataSource:"null");
//				throw new ServiceException(msg);
				logger.error(msg);
				ignore = true;
				return ;
			}
			
			try {
				linkAdapter.adapter(nameValue);
			} catch (Exception e) {
				String msg = String.format("link tag error: [id=%s, name=%s, href=%s, value=%s, text=%s], parent:[dataSource=%s]", id, name, href, value, text, parentFetcher!=null?parentFetcher.dataSource:"null");
				throw new ServiceException(msg, e);
			}
			
			actualText = linkAdapter.getText();
			Object linkValue = linkAdapter.getLinkValue();
			if(linkAdapter.needParseLink())
				linkValue = this.parseLink(linkValue, href);
			actualHref = linkValue;
//			addParameter("href", linkValue);
			
			title = linkAdapter.getTitle();
			if(StringUtils.isNotBlank(title)){
				addParameter("title", title);
			}
			
			String target = linkAdapter.getTarget();
			if(StringUtils.isNotBlank(target)){
				this.target = target;
			}
			
			if(showImg)
				actualImage = linkAdapter.getImage();
		}else{
//			actualHref = this.parseLink(nameValue, href);
			actualHref = findValue(href);
		}
		
		if(showImg){
			if(StringUtils.isNotBlank(image)){
				if(image.startsWith("/")){
					actualImage = SiteConfig.getInstance().getImagePath()+image;
				}else{
					actualImage = (String)this.findValue(image);
					actualImage = SiteConfig.getInstance().getResourceImagePath(actualImage);
				}
			}
			ignore = false;
			addParameter("image", actualImage);
		}
		
		addParameter("href", actualHref);
		
		if(actualImage==null && !VarKey.DISPALY_NONE.equals(getConstantValue(text))){
			if(StringUtils.isNotBlank(text)){
				actualText = (String)this.findValue(text);
			}
			addParameter("text", actualText);
		}
		addParameter("target", target);
	}
	
	
	protected Component getParent(){
		return (parentFetcher==null?parentComponent:parentFetcher);
	}
	
	protected String parseLink(Object linkValue, String href){
		ValueStack stack = getStack();
		String hrefStr = null;
		try{
			stack.getContext().put(VALUE_KEY, linkValue);
			if(StringUtils.isNotBlank(href))
				hrefStr = SiteConfig.getInstance().getProperty(href, href, false);
			else
				throw new ServiceException("not link mapped with the key:"+href);
		}catch(Exception e){
			logger.error("parse link error: href["+href+"]", e);
		}finally{
			stack.getContext().remove(VALUE_KEY);
		}
		return hrefStr;
	}

    protected Class getValueClassType() {
        return null;
    }

    public boolean start(Writer writer) {
        evaluateParams();
    	if(StringUtils.isNotBlank(var))
    		stack.getContext().put(var, this);
    	
    	Object top = stack.peek();
    	if(nameValue!=null && !top.equals(nameValue)){
    		stack.push(nameValue);
    		popValue = true;
    	}
    	findPropertyValue();
    	
    	if(ignore)
    		return false;
    	
    	return true;
    }
    
    public boolean usesBody(){
    	return true;
    }

    public boolean end(Writer writer, String body) {
    	ValueStack stack = getStack();
        try {
    		if(Type.isTextLink(type) && actualHref==null)
            	return false;
    		
        	if(ignore)
        		return false;
    		
    		/*if(Type.isImageLink(type) && actualImage==null)
    			return false;*/
    		
    		if(condition!=null){
	    		boolean cond = (Boolean)getStack().findValue(condition);
	    		if(!cond)
	    			return false;
    		}
        	
        	
            addParameter("body", body);
            mergeTemplate(writer, buildTemplateName(template, getDefaultTemplate()));
        } catch (Exception e) {
            throw new StrutsException(e);
        }
        finally {
        	if(StringUtils.isNotBlank(var))
        		stack.getContext().remove(var);
        	if(popValue)
        		stack.pop();
            popComponentStack();
        }

        return false;
    }
	
	@Override
	protected String getDefaultTemplate() {
		return TEMPLATE_CLOSE;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setType(String type) {
		if(StringUtils.isBlank(type))
			type = Type.TEXT;
		this.type = type;
		getParameters().put("type", type);
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public void setDataIndex(Integer dataIndex) {
		this.dataIndex = dataIndex;
	}

	public Object getActualHref() {
		return actualHref;
	}

	public String getActualImage() {
		return actualImage;
	}

	public String getActualText() {
		return actualText;
	}

	public void setVar(String var) {
		this.var = var;
	}

}
