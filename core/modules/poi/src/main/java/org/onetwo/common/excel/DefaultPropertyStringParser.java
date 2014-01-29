package org.onetwo.common.excel;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.poi.ss.usermodel.CellStyle;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.convert.Types;
import org.slf4j.Logger;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class DefaultPropertyStringParser implements PropertyStringParser {
	
	private final Logger logger = MyLoggerFactory.getLogger(this.getClass());
	
	private LoadingCache<String, Map<Integer, Short>> columnwidthCaches = CacheBuilder.newBuilder()
																				.maximumSize(100)
																				.build(new CacheLoader<String, Map<Integer, Short>>(){

																					@Override
																					public Map<Integer, Short> load(String columnWidth) throws Exception {
																						String[] attrs = StringUtils.split(columnWidth, ";");
																						Map<Integer, Short> columnWidthMap = LangUtils.newHashMap(attrs.length);
																						for(String attr : attrs){
																							String[] av = StringUtils.split(attr.trim(), ":");
																							if(av!=null && av.length==2){
																								try {
																									columnWidthMap.put(Types.convertValue(av[0], Integer.class), Types.convertValue(av[1], Short.class));
																								} catch (Exception e) {
																									throw new ServiceException("parse sheet coluomnWidth error", e);
																								}
																							}
																						}
																						return columnWidthMap;
																					}
																					
																				});

	
	private LoadingCache<String, Map<String, String>> styleCaches = CacheBuilder.newBuilder()
																				.maximumSize(100)
																				.build(new CacheLoader<String, Map<String, String>>(){

																					@Override
																					public Map<String, String> load(String styleString) throws Exception {
																						String[] attrs = StringUtils.split(styleString, ";");
																						Map<String, String> styleMap = LangUtils.newHashMap(attrs.length);
																						for(String attr : attrs){
																							String[] av = StringUtils.split(attr.trim(), ":");
																							if(av!=null && av.length==2){
																								try {
																									styleMap.put(av[0], av[1]);
																								} catch (Exception e) {
																									throw new ServiceException("set ["+StringUtils.join(av, ":")+"] style error", e);
																								}
																							}
																						}
																						return styleMap;
																					}
																					
																				});

	@Override
	public Map<Integer, Short> parseColumnwidth(String columnWidth) {
		try {
			if(StringUtils.isNotBlank(columnWidth))
				return columnwidthCaches.get(columnWidth);
		} catch (ExecutionException e) {
			logger.error("parseColumnwidth error : " + e.getMessage());
		}
		return Collections.EMPTY_MAP;
	}
	
	public Map<String, String> parseStyle(String styleString){
		try {
			if(StringUtils.isNotBlank(styleString))
				return styleCaches.get(styleString);
		} catch (ExecutionException e) {
			logger.error("parseStyle error : " + e.getMessage());
		}
		return Collections.EMPTY_MAP;
	}
	
}
