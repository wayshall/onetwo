package org.onetwo.common.excel;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.LangUtils;
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
																						int index = 0;
																						for(String attr : attrs){
																							String[] av = StringUtils.split(attr.trim(), ":");
																							if(LangUtils.isEmpty(av))
																								continue;
																							try {
																								if(av.length==1){
																									columnWidthMap.put(index, Types.convertValue(av[0], Short.class));
																								}else if(av.length==2){
																									index = Types.convertValue(av[0], Integer.class);
																									columnWidthMap.put(index, Types.convertValue(av[1], Short.class));
																								}
																							} catch (Exception e) {
																								throw new ServiceException("parse sheet coluomnWidth error", e);
																							}
																							index++;
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
