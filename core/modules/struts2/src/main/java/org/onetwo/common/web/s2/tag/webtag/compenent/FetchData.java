package org.onetwo.common.web.s2.tag.webtag.compenent;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.components.IteratorComponent;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.utils.ObjectUtils;
import org.onetwo.common.utils.params.TagParamsMap;
import org.onetwo.common.web.s2.tag.webtag.TagExecutor;
import org.onetwo.common.web.s2.tag.webtag.TemplateBuilder;
import org.onetwo.common.web.s2.tag.webtag.TemplateTagManagerFactory;
import org.onetwo.common.web.utils.StrutsUtils;

import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings({"unchecked", "serial"})
public class FetchData extends IteratorComponent {
	
	public static final String DATA_KEY = "fetchDatas";
	
	public static class ListType {
		public static final String CONTAINER = "container";
		public static final String ITERATOR = "iterator";
		
		public static boolean isIterator(String type){
			return ITERATOR.equals(type);
		}
		
		public static boolean isContainer(String type){
			return CONTAINER.equals(type);
		}
		
		private static Map<String, String> typeMapper = new HashMap<String, String>(){
			{
				put(CONTAINER, CONTAINER);
				put("不循环", CONTAINER);
				put(ITERATOR, ITERATOR);
				put("循环", ITERATOR);
			}
		};
		
		public static String getListType(String type){
			String listType = typeMapper.get(type);
			if(StringUtils.isBlank(listType))
				listType = CONTAINER;
			return listType;
		}
	}

	protected Logger logger = Logger.getLogger(this.getClass());
	
	protected String dataSource;
	protected Integer firstResult;
	protected Integer maxResults;
	protected String orderAsc;
	protected String orderDesc;
	protected String type;
	
	protected Object datas;
	protected Integer index;
	protected boolean popValue;
	
	protected TemplateBuilder templateBuilder;
	protected TagExecutor executor;
	protected boolean templateRendered;
	
	public FetchData(ValueStack stack) {
		super(stack);
		this.parameters = new TagParamsMap();
	}

    public boolean start(Writer writer) {
    	getParameters().setFirstResult(firstResult);
    	getParameters().setMaxResults(maxResults);
    	getParameters().setDataSource(dataSource);
    	getParameters().setOrderAsc(orderAsc);
    	getParameters().setOrderDesc(orderDesc);
    	
    	templateRendered = false;
    	this.value = this.dataSource;
    	if(ListType.isIterator(type)){
        	return super.start(writer);
    	}
    	
		datas = this.findValue(value);
		
    	if(datas==null){
			super.end(writer, "", true);
			return false;
    	}
    	
        if (beginStr != null)
            begin = (Integer) findValue(beginStr,  Integer.class);

        if (endStr != null)
            end = (Integer) findValue(endStr,  Integer.class);
        
        if(begin!=null || index!=null){
        	List list = null;
        	if(List.class.isAssignableFrom(datas.getClass())){
            	list = (List)datas;
        	}else if(Map.class.isAssignableFrom(datas.getClass())){
        		list = new ArrayList(((Map)datas).entrySet());
        	}else{
        		throw new ServiceException("the value of ["+this.value+"] is not a iterator, it can not sublist!");
        	}
	        
	        if(index!=null){
	        	if(index<0 || (index+1)>list.size())
	        		datas = null;
	        	else
	        		datas = list.get(index);
	        }else{
	        	if(end==null || end>=list.size())
	        		end = list.size()-1;
	        	datas = list.subList(begin, end+1);
	        }
	        
        }
    	
        if(datas!=null){
        	if(ObjectUtils.isObjectEmpty(datas)){
    			super.end(writer, "", true);
    			return false;
        	}
        	stack.push(datas);
			this.putInContext(datas);
			popValue = true;
			addParameter(DATA_KEY, datas);
//			addParameter("datas", datas);
			return true;
        }else{
			super.end(writer, "", true);
			return false;
        }
	        
    }

    public boolean endTag(Writer writer, String body) {
    	this.renderTemplate(writer);
    	return super.end(writer, body, false);
    }
    
    protected void renderTemplate(Writer writer){
    	if(!isTemplateRendered() && ListType.isContainer(type) && executor != null && executor.needMergeTemplate(this.getParameters())){
    		this.templateBuilder.mergeTemplate(this, writer, executor.getTemplate(this.getParameters()));
    		this.templateRendered = true;
    	}
    }

    public boolean end(Writer writer, String body) {
    	this.renderTemplate(writer);
    	if(popValue)
    		stack.pop();
    	return super.end(writer, body);
    }

    protected void putInContext(Object value) {
    	if(value!=null && datas!=value)
    		datas = value;
        super.putInContext(value);
    }

    protected Object findValue(String expr) {
    	return this.findValue(expr, null);
    }
    
    protected Object findValue(String expr, Class type) {
    	if(type==String.class){
    		return super.findValue(expr, type);
    	}
        if (StringUtils.isBlank(expr)) {
        	FetchData fd = (FetchData)this.findAncestor(FetchData.class);
        	if(fd==null)
        		return null;
        	this.value = "#"+fd.var;
            return fd.datas;
        }
        Object result = null;
        executor = TemplateTagManagerFactory.getDefault().getTagExcutor(expr);
    	try {
	        if(executor!=null){
	            	result = executor.execute(getParameters());
	        }else{
	        	if(logger.isInfoEnabled())
	        		logger.info("can not find executor : " + expr);
	        	if(type!=null)
	        		result = super.findValue(expr, type);
	        	else
	        		result = super.findValue(expr);
	        }
    	} catch (Exception e) {
			String msg = "标签解释出错:"+e.getMessage()+", dataSource:"+dataSource;
			logger.error(msg, e);
			new ServiceException(msg, e);
		}
        return result;
    }
    
    public TagParamsMap getParameters(){
    	return (TagParamsMap)super.getParameters();
    }

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	
    protected String getVar() {
        return StringUtils.isBlank(var)?"element":var;
    }

	public void setParams(String params) {
		Map map = StrutsUtils.getJsonMap(params);
		if(map!=null){
			for (Map.Entry<String, String> entry : (Collection<Map.Entry<String, String>>) map.entrySet()) {
				Object value = entry.getValue();// this.findValue(entry.getValue());
				if (value == null)
					continue;
				if(String.class.isAssignableFrom(value.getClass())){
					value = super.findValue(entry.getValue(), String.class);
				}
				this.getParameters().put(entry.getKey(), value == null ? entry.getValue() : value);
			}
		}
	}

	public void setFirstResult(Integer firstResult) {
		this.firstResult = firstResult;
	}

	public void setMaxResults(Integer maxResults) {
		this.maxResults = maxResults;
	}

	public void setType(String type) {
		this.type = ListType.getListType(type);
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	@Inject("templateBuilder")
	public void setTemplateBuilder(TemplateBuilder templateBuilder) {
		this.templateBuilder = templateBuilder;
	}

	public Object getDatas() {
		return datas;
	}

	public boolean isTemplateRendered() {
		return templateRendered;
	}

	public void setOrderAsc(String orderAsc) {
		this.orderAsc = orderAsc;
	}

	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}
}
