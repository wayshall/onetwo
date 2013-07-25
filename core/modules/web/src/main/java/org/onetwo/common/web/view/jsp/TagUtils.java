package org.onetwo.common.web.view.jsp;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.StringUtils;

final public class TagUtils {
	
	public static String BASE_VIEW_DIR = "/WEB-INF/views/";

	public static String getViewPage(String path){
		if(StringUtils.isBlank(path))
			return path;
		
		if(path.startsWith(BASE_VIEW_DIR))
			return path;
		
		if(path.startsWith("/"))
			path = path.substring(1);
		
		return BASE_VIEW_DIR + path;
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
	
	private TagUtils(){
	}
}
