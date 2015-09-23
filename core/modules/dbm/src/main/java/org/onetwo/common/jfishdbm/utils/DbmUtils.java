package org.onetwo.common.jfishdbm.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.onetwo.common.convert.Types;
import org.onetwo.common.date.Dates;
import org.onetwo.common.jfishdbm.annotation.DbmFieldListeners;
import org.onetwo.common.jfishdbm.event.DbmEntityFieldListener;
import org.onetwo.common.jfishdbm.exception.DbmException;
import org.onetwo.common.jfishdbm.mapping.SqlTypeFactory;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.JFishProperty;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;

final public class DbmUtils {
	private DbmUtils(){
	}
	public static List<DbmEntityFieldListener> initDbmEntityFieldListeners(DbmFieldListeners listenersAnntation){
		Assert.notNull(listenersAnntation);
		Class<? extends DbmEntityFieldListener>[] flClasses = listenersAnntation.value();
		List<DbmEntityFieldListener> fieldListeners = Lists.newArrayList();
		for(Class<? extends DbmEntityFieldListener> flClass : flClasses){
			DbmEntityFieldListener fl = ReflectUtils.newInstance(flClass);
			fieldListeners.add(fl);
		}
		return fieldListeners;
	}
	
	public static Object convertPropertyValue(JFishProperty propertyInfo, Object value){
		Object actualValue = value;
		Class<?> propType = propertyInfo.getType();
		if(Enum.class.isAssignableFrom(propType)){
			Enumerated enumerated = propertyInfo.getAnnotation(Enumerated.class);
			if(enumerated!=null){
				EnumType etype = enumerated.value();
				if(etype==EnumType.ORDINAL){
					actualValue = Types.asValue(Integer.valueOf(value.toString()), propType);
				}else if(etype==EnumType.STRING){
					actualValue = Types.asValue(value.toString(), propType);
				}else{
					throw new DbmException("error enum type: " + etype);
				}
			}
			
		}else if(Date.class.isInstance(value) && Temporal.class.isAssignableFrom(propType)){
			Date date = (Date) value;
			if(LocalDate.class.isAssignableFrom(propType)){
				actualValue = Dates.toLocalDate(date);
			}else if(LocalTime.class.isAssignableFrom(propType)){
				actualValue = Dates.toLocalTime(date);
			}else if(LocalDateTime.class.isAssignableFrom(propType)){
				actualValue = Dates.toLocalDateTime(date);
			}
		}
		return actualValue;
	}
	
	public static SqlParameterValue convertSqlParameterValue(JFishProperty propertyInfo, Object value){
		if(value==null)
			return null;
		int sqlType = SqlTypeFactory.getType(propertyInfo.getType());
		SqlParameterValue sqlValue = new SqlParameterValue(sqlType, getActualSqlValue(value));
		return sqlValue;
	}

	public static Object getActualSqlValue(Object value){
		if(Enum.class.isAssignableFrom(value.getClass())){
//			return Types.asValue(value.toString(), value.getClass());
			return ((Enum<?>)value).name();
		}
		return value;
	}
}
