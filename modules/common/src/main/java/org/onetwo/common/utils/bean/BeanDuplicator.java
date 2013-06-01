package org.onetwo.common.utils.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.dozer.DozerBeanMapper;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

@SuppressWarnings("unchecked")
public class BeanDuplicator {

	public static final String PERSIST_PREFIX = "-persist";
	public static final String UPDATE_PREFIX = "-update";
	public static final String MAPPING_FILES = "mappingFiles.";
	public static final String DOZER_MAP_CONF = "dozer.map.conf";

	private DozerBeanMapper dozer;

	public BeanDuplicator(Properties props) {
		dozer = new DozerBeanMapper();
		Enumeration<String> names = (Enumeration<String>)props.propertyNames();
		String propName = null;
		List<String> fields = new ArrayList<String>();
		while(names.hasMoreElements()){
			propName = names.nextElement();
			if(propName.startsWith(MAPPING_FILES) || propName.startsWith(DOZER_MAP_CONF)){
				fields.add(props.getProperty(propName));
			}
		}
		dozer.setMappingFiles(fields);
//		dozer.setEventListeners((List<? extends DozerEventListener>)LangUtils.asList(new PropertyUnderlineListener()));
//		dozer.setMappings(mappingBuilder)
//		this.config(dozer);
	}
	
	public void initDozer(){
		
	}

	public DozerBeanMapper getDozer() {
		return dozer;
	}

	public void setDozer(DozerBeanMapper dozer) {
		this.dozer = dozer;
	}

	public void map(Object source, Object dest) {
		map(source, dest, "");
	}

	public void map(Object source, Object dest, String mapid) {
		if(StringUtils.isBlank(mapid))
			dozer.map(source, dest);
		else
			dozer.map(source, dest, mapid);
	}
	
	public <T> List<T> mapList(List<?> sources, Class<T> destClass){
		if(LangUtils.isEmpty(sources))
			return Collections.EMPTY_LIST;
		List<T> list = new ArrayList<T>();
		T dest = null;
		for(Object s : sources){
			dest = map(s, destClass);
			list.add(dest);
		}
		return list;
	}

	public <T> T map(Object source, Class<T> destinationClass){
		return map(source, destinationClass, "");
	}

	public <T> T map(Object source, Class<T> destinationClass, String mapid){
		if(StringUtils.isBlank(mapid))
			return dozer.map(source, destinationClass);
		else
			return dozer.map(source, destinationClass, mapid);
	}
	
	private static String getPersistMapId(Class<?> persistenceClass){
		return persistenceClass.getSimpleName() + PERSIST_PREFIX;
	}
	
	private static String getUpdateMapId(Class<?> persistenceClass){
		return persistenceClass.getSimpleName() + UPDATE_PREFIX;
	}

	public <T> T map4Persist(Object source, Class<T> persistenceClass){
		String mapId = getPersistMapId(persistenceClass);
		T result = null;
		try {
			result = dozer.map(source, persistenceClass, mapId);
		} catch (Exception e) {
			result = dozer.map(source, persistenceClass);
		}
		return result;
	}

	public <T> T map4Update(Object source, Class<T> persistenceClass) {
		String mapId = getUpdateMapId(persistenceClass);
		T result = null;
		try {
			result = dozer.map(source, persistenceClass, mapId);
		} catch (Exception e) {
			result = dozer.map(source, persistenceClass);
		}
		return result;
	}

	public <T> void map4Persist(Object source, T dest){
		String mapId = getPersistMapId(dest.getClass());
		try {
			dozer.map(source, dest, mapId);
		} catch (Exception e) {
			dozer.map(source, dest); 
		}
	}

	public <T> void map4Update(Object source, T dest){
		String mapId = getUpdateMapId(dest.getClass());
		try {
			dozer.map(source, dest, mapId);
		} catch (Exception e) {
			dozer.map(source, dest);
		}
	}

}
