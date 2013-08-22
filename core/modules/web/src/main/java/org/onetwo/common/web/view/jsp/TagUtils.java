package org.onetwo.common.web.view.jsp;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.view.jsp.form.FormTagBean;

final public class TagUtils {

	public static String BASE_TAG_DIR = "/WEB-INF/tags/";
	public static String BASE_VIEW_DIR = "/WEB-INF/views/";


	public static String getViewPage(String path){
		return getDirPage(BASE_VIEW_DIR, path);
	}
	public static String getTagPage(String path){
		return getDirPage(BASE_TAG_DIR, path);
	}
	public static String getDirPage(String baseDir, String path){
		if(StringUtils.isBlank(path))
			return path;
		
		if(path.startsWith(BASE_VIEW_DIR))
			return path;
		
		if(path.startsWith("/"))
			path = path.substring(1);
		
		return baseDir + path;
	}
	
	public static Page<Object> toPage(Object dsValue){
		Page<Object> page = null;
		if(dsValue==null){
			page = Page.create();
		}else if (dsValue instanceof Page) {
			page = (Page<Object>) dsValue;
		}else {
			List<Object> list = null;
			if(Map.class.isInstance(dsValue)){
				Map<?, ?> dataMap = (Map<?, ?>)dsValue;
				list = LangUtils.newArrayList(dataMap.size());
				for(Entry<?, ?> entry : dataMap.entrySet()){
//					list.add(KVEntry.create(entry));
					list.add(entry);
				}
			}else{
				list = LangUtils.asList(dsValue);
			}
			if (list == null)
				list = Collections.EMPTY_LIST;
			page = Page.create();
			page.setResult(list);
			page.setTotalCount(list.size());
			page.setPageSize(list.size());
		}
		return page;
	}
	
	public static String pageLink(String action, int numb){
		String result = action;
		if(numb<=1)
			return result;
		if (action.indexOf("?")!=-1){
			result += "&pageNo="+numb;
		}else{
			result += "?pageNo="+numb;
		}
		return result;
	}
	

	public static String getFormVarName(){
		return FormTagBean.class.getSimpleName();
	}
	
	private TagUtils(){
	}
}
