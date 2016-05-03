package org.onetwo.common.excel;

import java.sql.Types;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.excel.exception.ExcelException;
import org.onetwo.common.excel.utils.ExcelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class DefaultPropertyStringParser implements PropertyStringParser {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private LoadingCache<String, Map<Integer, Short>> columnwidthCaches = CacheBuilder.newBuilder()
																				.maximumSize(100)
																				.build(new CacheLoader<String, Map<Integer, Short>>(){

																					@Override
																					public Map<Integer, Short> load(String columnWidth) throws Exception {
																						String[] attrs = StringUtils.split(columnWidth, ";");
																						Map<Integer, Short> columnWidthMap = new HashMap<Integer, Short>(attrs.length);
																						int index = 0;
																						for(String attr : attrs){
																							String[] av = StringUtils.split(attr.trim(), ":");
																							if(ExcelUtils.isEmpty(av))
																								continue;
																							try {
																								if(av.length==1){
																									columnWidthMap.put(index, Short.parseShort(av[0]));
																								}else if(av.length==2){
																									index = Integer.parseInt(av[0]);
																									columnWidthMap.put(index, Short.parseShort(av[1]));
																								}
																							} catch (Exception e) {
																								throw new ExcelException("parse sheet coluomnWidth error", e);
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
																						Map<String, String> styleMap = new HashMap<String, String>(attrs.length);
																						for(String attr : attrs){
																							String[] av = StringUtils.split(attr.trim(), ":");
																							if(av!=null && av.length==2){
																								try {
																									styleMap.put(av[0], av[1]);
																								} catch (Exception e) {
																									throw new ExcelException("set ["+StringUtils.join(av, ":")+"] style error", e);
																								}
																							}
																						}
																						return styleMap;
																					}
																					
																				});

	@Override
	public Map<Integer, Short> parseColumnwidth(String columnWidth) {
		try {
			if(!ExcelUtils.isBlank(columnWidth))
				return columnwidthCaches.get(columnWidth);
		} catch (ExecutionException e) {
			logger.error("parseColumnwidth error : " + e.getMessage());
		}
		return Collections.EMPTY_MAP;
	}
	
	public Map<String, String> parseStyle(String styleString){
		try {
			if(!ExcelUtils.isBlank(styleString))
				return styleCaches.get(styleString);
		} catch (ExecutionException e) {
			logger.error("parseStyle error : " + e.getMessage());
		}
		return Collections.EMPTY_MAP;
	}
	
}
