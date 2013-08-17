package org.onetwo.common.jdbc;

import java.util.Collection;
import java.util.List;

import org.onetwo.common.utils.LangUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;

public class SimpleRowMapperFactory implements RowMapperFactory {

	
	public SimpleRowMapperFactory() {
		super();
	}
	
	public boolean isSimpleType(Class<?> type){
		return LangUtils.isSimpleType(type);
	}

	@Override
	public RowMapper<?> createDefaultRowMapper(Class<?> type) {
		RowMapper<?> rowMapper = null;
		if(type==null || type==Object.class){
//			rowMapper = new SingleColumnRowMapper(Object.class);
			rowMapper = new UnknowTypeRowMapper();
		}else if(isSimpleType(type) || LangUtils.isTimeClass(type)){
			//唯一，而且返回类型是简单类型，则返回单列的RowMapper
			rowMapper = new SingleColumnRowMapper(type);
		}else if(LangUtils.isMapClass(type)){
			rowMapper = new ColumnMapRowMapper();
		}else if(Object[].class==type){
			rowMapper = new ObjectArrayRowMapper();
		}else if(List.class.isAssignableFrom(type)){
			rowMapper = new ListRowMapper();
		}else if(Collection.class.isAssignableFrom(type)){
			rowMapper = new HashsetRowMapper();
		}else{
			rowMapper = getBeanPropertyRowMapper(type);
		}
		return rowMapper;
	}

	protected RowMapper<?> getBeanPropertyRowMapper(Class<?> entityClass) {
		return new BeanPropertyRowMapper(entityClass);
	}
	

}
