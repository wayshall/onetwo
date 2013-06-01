package org.onetwo.common.web.s2.tag.webtag.compenent;

import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.StrutsException;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.web.config.SiteConfig;
import org.onetwo.common.web.s2.tag.WebUIClosingBean;

import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings({"unchecked"})
public class Image extends WebUIClosingBean {
	
//	public static final String TEMPLATE_OPEN = "image-close";
	public static final String TEMPLATE_CLOSE = "image-close";
	public static final String TEMPLATE_NO_WIDTH_HEIGHT = "image-no-width-height-close";
	
	public static enum LocationType {
		local,
		resource
	}
	
	private String var;
	private String src;
	private Integer width;
	private Integer height;
	private String alt;
	
	private String actualSrc;
	
	private Integer dataIndex;
	private String _default;
	
	private Object nameValue;
	
	private boolean popValue = false;
	
	private FetchData parentFetcher;
	
	private LocationType location = LocationType.resource;
	private boolean lazy;
	private String lazySrc = "";
	
	public Image(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
		parentFetcher = (FetchData)this.findAncestor(FetchData.class);
	}

	@Override
	protected void evaluateExtraParams() {
		super.evaluateExtraParams();
		if(StringUtils.isBlank(alt))
			this.alt = this.title;
			
		if(StringUtils.isNotBlank(alt)){
			addParameter("alt", findString(alt));
		}

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
		
	}
	
	protected void findPropertyValue(){
		if(actualSrc == null && StringUtils.isNotBlank(src)) {
			actualSrc = (String)findValue(src);
		}
		if(StringUtils.isBlank(actualSrc)){
			if(StringUtils.isNotBlank(_default)){
				actualSrc = (String)findValue(_default);
				if(StringUtils.isBlank(actualSrc))
					actualSrc = _default;
			}else
				actualSrc = SiteConfig.getInstance().getSiteDefaultImage();
		}
		
		if(SiteConfig.getInstance().isProduct()){
			int lastIndex = actualSrc.lastIndexOf('.');
			if(lastIndex!=-1){
				String nameWithoutExt = actualSrc.substring(0, lastIndex);
				actualSrc = nameWithoutExt + "_" + width + "_" + height + actualSrc.substring(lastIndex);
			}
		}
		
		String siteDefaultImage = SiteConfig.getInstance().getSiteDefaultImage();
		if(LocationType.resource.equals(this.location)){
			actualSrc = SiteConfig.getInstance().getResourceImagePath(actualSrc);
			siteDefaultImage = SiteConfig.getInstance().getResourceImagePath(siteDefaultImage);
		}else{
			actualSrc = SiteConfig.getInstance().getContextPath() + actualSrc;
			siteDefaultImage = SiteConfig.getInstance().getContextPath() + siteDefaultImage;
		}
		
		if(lazy){
			lazySrc = actualSrc;
			actualSrc = siteDefaultImage;
		}

		addParameter("src", actualSrc);
		addParameter("width", width);
		addParameter("height", height);
		addParameter("lazySrc", lazySrc);
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
    	
    	return true;
    }
    
    public boolean usesBody(){
    	return true;
    }

    public boolean end(Writer writer, String body) {
    	ValueStack stack = getStack();
        try {
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
		if(SiteConfig.getInstance().isProduct()){
			return TEMPLATE_NO_WIDTH_HEIGHT;
		}
		return TEMPLATE_CLOSE;
	}

	public Object getActualHref() {
		return actualSrc;
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

	public LocationType getLocation() {
		return location;
	}

	public void setLocation(String location) {
		if(StringUtils.isBlank(location))
			return ;
		this.location = LocationType.valueOf(location);
	}
	
	public void setAlt(String alt){
		this.alt = alt;
	}

	public boolean isLazy() {
		return lazy;
	}

	public void setLazy(boolean lazy) {
		this.lazy = lazy;
	}
}
