package org.onetwo.ext.poi.utils;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class TableHeaderMapper {
	public static TableHeaderMapper newMapper(String...titleNames){
		return new TableHeaderMapper(titleNames);
	}
	//title->index
	private BiMap<String, Integer> indexMapper = HashBiMap.create();
	//index->name
	private BiMap<String, String> titleNameMapper = HashBiMap.create();
	
	public TableHeaderMapper(String...titleNames){
		for (int i = 0; i < titleNames.length; i++) {
			indexMapper.put(titleNames[i], i);
		}
	}

	public TableHeaderMapper mapAll(Map<String, String> tittleNames){
		this.titleNameMapper.putAll(tittleNames);
		return this;
	}

	public TableHeaderMapper map(String title, String name){
//		Integer index = getIndexByTitle(title, true);
		titleNameMapper.put(title, name);
		return this;
	}
	
	public Integer getIndexByTitle(String title){
		return getIndexByTitle(title, false);
	}
	public Integer getIndexByTitle(String title, boolean throwIfNotFoud){
		Integer index = indexMapper.get(title);
		if(index==null && throwIfNotFoud){
			throw new RuntimeException("no index found for title :" + title);
		}
		return index;
	}
	
	public Integer getIndexByName(String name){
		return getIndexByName(name, false);
	}
	public Integer getIndexByName(String name, boolean throwIfNotFoud){
		String title =  titleNameMapper.inverse().get(name);
		if((title==null || !indexMapper.containsKey(title)) && throwIfNotFoud){
			throw new RuntimeException("no index found for name :" + name);
		}
		return indexMapper.get(title);
	}
	public String getNameByIndex(int index, boolean throwIfNotFoud){
		String tittle = indexMapper.inverse().get(index);
		if((tittle==null || !titleNameMapper.containsKey(tittle)) && throwIfNotFoud){
			throw new RuntimeException("no name found for index :" + index);
		}
		return titleNameMapper.get(tittle);
	}
	
	public String getNameByTitle(String title){
		Integer index = getIndexByTitle(title, true);
		return titleNameMapper.get(index);
	}
	
	public String getNameByIndex(int index){
		return getNameByIndex(index, false);
	}
	
	public <T> MapperBean<T> createMapperBean(T bean){
		return new MapperBean<>(this, bean);
	}
	
	public static class MapperBean<T> {
		final private TableHeaderMapper mapper;
		final private BeanWrapper beanWrapper;
		private MapperBean(TableHeaderMapper mapper, T bean) {
			super();
			this.mapper = mapper;
			this.beanWrapper = ExcelUtils.newBeanWrapper(bean);
		}
		
		@SuppressWarnings("unchecked")
		public T getBean(){
			return (T)beanWrapper.getWrappedInstance();
		}
		
		public boolean isIgnore(String name, Object value){
			return StringUtils.isBlank(name) || value==null || StringUtils.isBlank(value.toString());
		}
		public void setValue(int index, Object value){
			String name = mapper.getNameByIndex(index);
			if(isIgnore(name, value)){
				return ;
			}
			this.beanWrapper.setPropertyValue(name, value);
		}
		
		/*public Object getValue(int index){
			String name = mapper.getNameByIndex(index);
			if(isIgnoreName(name)){
				return null;
			}
			return this.beanWrapper.getPropertyValue(name);
		}
		
		public Object getValue(String name){
			if(isIgnoreName(name)){
				return null;
			}
			return this.beanWrapper.getPropertyValue(name);
		}
		
		public void setValue(String name, Object value){
			if(isIgnoreName(name)){
				return ;
			}
			this.beanWrapper.setPropertyValue(name, value);
		}
		
		public Object getValueByTitle(String title){
			String name = mapper.getNameByTitle(title);
			if(isIgnoreName(name)){
				return null;
			}
			return this.beanWrapper.getPropertyValue(name);
		}

		public void setValueByTitle(String title, Object value){
			String name = mapper.getNameByTitle(title);
			if(isIgnoreName(name)){
				return ;
			}
			this.beanWrapper.setPropertyValue(name, value);
		}*/
	}

}
