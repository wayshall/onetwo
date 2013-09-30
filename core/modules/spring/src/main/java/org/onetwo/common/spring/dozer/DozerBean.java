package org.onetwo.common.spring.dozer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

@SuppressWarnings("unchecked")
public class DozerBean {

	public static final String GLOBAL_CONFIG = "dozer/global-mapping.xml";

	private DozerBeanMapper dozer;


	public DozerBean() {
		this(null);
	}
	public DozerBean(List<String> mappingFiles) {
		dozer = new DozerBeanMapper();
		if(mappingFiles!=null){
			mappingFiles.add(0, GLOBAL_CONFIG);
		}else{
			mappingFiles = new ArrayList<String>(3);
			mappingFiles.add(GLOBAL_CONFIG);
		}
		dozer.setMappingFiles(mappingFiles);
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


}
