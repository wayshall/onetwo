package org.onetwo.dbm.utils;

import java.beans.PropertyDescriptor;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.apache.commons.lang3.ArrayUtils;
import org.onetwo.common.convert.Types;
import org.onetwo.common.date.Dates;
import org.onetwo.common.db.dquery.annotation.DbmPackages;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.reflect.Intro;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.JFishProperty;
import org.onetwo.dbm.annotation.DbmFieldListeners;
import org.onetwo.dbm.event.DbmEntityFieldListener;
import org.onetwo.dbm.exception.DbmException;
import org.onetwo.dbm.exception.UpdateCountException;
import org.onetwo.dbm.jdbc.JdbcUtils;
import org.onetwo.dbm.mapping.DbmTypeMapping;
import org.onetwo.dbm.spring.EnableDbm;
import org.onetwo.dbm.support.DbmTransaction;
import org.slf4j.Logger;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;

final public class DbmUtils {
	
	private static final Logger logger = JFishLoggerFactory.getLogger(DbmUtils.class);
	
	public static void throwIfEffectiveCountError(String errorMsg, int expectCount, int effectiveCount){
		if(effectiveCount!=expectCount)
			throw new UpdateCountException(errorMsg, expectCount, effectiveCount);
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
	

	public static Object convertPropertyValue(PropertyDescriptor pd, Object value){
		JFishProperty jproperty = Intro.wrap(pd.getWriteMethod().getDeclaringClass()).getJFishProperty(pd.getName(), false);
//		JFishMappedProperty mappedProperty = new JFishMappedProperty(null, jproperty);
		return convertPropertyValue(jproperty, value);
	}
	
	public static Object convertPropertyValue(JFishProperty propertyInfo, Object value){
		if(value==null)
			return value;
		Object actualValue = value;
		Class<?> propType = propertyInfo.getType();
		if(Enum.class.isAssignableFrom(propType) && !Enum.class.isInstance(actualValue)){
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
			}else{
				actualValue = Types.asValue(value.toString(), propType);
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
	

	public static SqlParameterValue convertSqlParameterValue(PropertyDescriptor pd, Object value, DbmTypeMapping mapping){
//		ClassIntroManager.getInstance().getIntro(pd.getWriteMethod().getDeclaringClass()).getJFishProperty(pd.getName(), false);
//		return convertSqlParameterValue(jproperty, value, mapping);
		SqlParameterValue sqlValue = new SqlParameterValue(DBUtils.TYPE_UNKNOW, value);
		return sqlValue;
	}
	
	public static SqlParameterValue convertSqlParameterValue(JFishProperty propertyInfo, Object value, DbmTypeMapping mapping){
		if(value==null)
			return null;
		int sqlType = mapping.getType(propertyInfo.getType());
		
		Object actualValue = value;
		Class<?> propType = propertyInfo.getType();
		if(Enum.class.isAssignableFrom(propType)){
			Enum<?> enumValue = (Enum<?>)value;
			Enumerated enumerated = propertyInfo.getAnnotation(Enumerated.class);
			if(enumerated!=null){
				EnumType etype = enumerated.value();
				if(etype==EnumType.ORDINAL){
					actualValue = enumValue.ordinal();
				}else if(etype==EnumType.STRING){
					actualValue = enumValue.name();
				}else{
					throw new DbmException("error enum type: " + etype);
				}
			}else{
				actualValue = enumValue.name();
			}
		}
		
		SqlParameterValue sqlValue = new SqlParameterValue(sqlType, actualValue);
		return sqlValue;
	}

	public static Object getActualSqlValue(Object value){
		if(value!=null && Enum.class.isAssignableFrom(value.getClass())){
//			return Types.asValue(value.toString(), value.getClass());
			return ((Enum<?>)value).name();
		}
		return value;
	}


	public static Map<String, Integer> lookupColumnNames(SqlRowSetMetaData resultSetMetaData) throws SQLException {
		int columnCount = resultSetMetaData.getColumnCount();
		Map<String, Integer> names = new HashMap<String, Integer>();
		for (int index = 1; index <= columnCount; index++) {
			names.put(JdbcUtils.lowerCaseName(lookupColumnName(resultSetMetaData, index)), index);
		}
		return names;
	}

	public static String lookupColumnName(SqlRowSetMetaData resultSetMetaData, int columnIndex) throws SQLException {
		String name = resultSetMetaData.getColumnLabel(columnIndex);
		if (name == null || name.length() < 1) {
			name = resultSetMetaData.getColumnName(columnIndex);
		}
		return name;
	}
	
	public static Object getActualValue(Object value){
		if(SqlParameterValue.class.isInstance(value)){
			return ((SqlParameterValue)value).getValue();
		}else if(Enum.class.isInstance(value)){
			return ((Enum<?>)value).name();
		}else if(value instanceof LocalDate){
			final LocalDate localDate = (LocalDate) value;
			return new SqlTypeValue(){

				@Override
				public void setTypeValue(PreparedStatement ps, int paramIndex, int sqlType, String typeName) throws SQLException {
					ps.setDate(paramIndex, new java.sql.Date(Dates.toDate(localDate).getTime()));
				}
				
			};
		}else if(value instanceof LocalDateTime){
			final LocalDateTime localDateTime = (LocalDateTime) value;
			return new SqlTypeValue(){

				@Override
				public void setTypeValue(PreparedStatement ps, int paramIndex, int sqlType, String typeName) throws SQLException {
					ps.setTimestamp(paramIndex, new Timestamp(Dates.toDate(localDateTime).getTime()));
				}
				
			};
			
		}
		return value;
	}
	
	public static Collection<String> getAllDbmPackageNames(ApplicationContext applicationContext){
		ListableBeanFactory bf = (ListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();
		return getAllDbmPackageNames(bf);
	}
	public static Collection<String> getAllDbmPackageNames(ListableBeanFactory beanFactory){
		Collection<String> packageNames = new HashSet<>();
		packageNames.addAll(scanEnableDbmPackages(beanFactory));
		packageNames.addAll(scanDbmPackages(beanFactory));
		return packageNames;
	}
	
	public static List<String> scanEnableDbmPackages(ApplicationContext applicationContext){
		ListableBeanFactory bf = (ListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();
		return scanEnableDbmPackages(bf);
	}
	public static List<String> scanEnableDbmPackages(ListableBeanFactory beanFactory){
		List<String> packageNames = new ArrayList<String>();
		SpringUtils.scanAnnotationPackages(beanFactory, EnableDbm.class, (beanDef, beanClass)->{
			EnableDbm enableDbm = beanClass.getAnnotation(EnableDbm.class);
			String[] modelPacks = enableDbm.packagesToScan();
			if(ArrayUtils.isNotEmpty(modelPacks)){
				for(String pack : modelPacks){
					packageNames.add(pack);
				}
			}else{
				String packageName = beanClass.getPackage().getName();
				if(!packageName.startsWith("org.onetwo.")){
					packageNames.add(packageName);
				}
			}
		});
		return packageNames;
	}
	public static List<String> scanDbmPackages(ApplicationContext applicationContext){
		ListableBeanFactory bf = (ListableBeanFactory)applicationContext.getAutowireCapableBeanFactory();
		return scanDbmPackages(bf);
	}
	public static List<String> scanDbmPackages(ListableBeanFactory beanFactory){
		List<String> packageNames = new ArrayList<String>();
		SpringUtils.scanAnnotationPackages(beanFactory, DbmPackages.class, (beanDef, beanClass)->{
			DbmPackages dbmPackages = beanClass.getAnnotation(DbmPackages.class);
			String[] modelPacks = dbmPackages.value();
			if(ArrayUtils.isNotEmpty(modelPacks)){
				for(String pack : modelPacks){
					packageNames.add(pack);
				}
			}else{
				String packageName = beanClass.getPackage().getName();
				packageNames.add(packageName);
			}
		});
		return packageNames;
	}
	

	public static void rollbackOnException(DbmTransaction transaction, Throwable ex) throws TransactionException {
		logger.debug("Initiating transaction rollback on application exception", ex);
		try {
			transaction.rollback();
		}
		catch (TransactionSystemException ex2) {
			logger.error("Application exception overridden by rollback exception", ex);
			ex2.initApplicationException(ex);
			throw ex2;
		}
		catch (RuntimeException ex2) {
			logger.error("Application exception overridden by rollback exception", ex);
			throw ex2;
		}
		catch (Error err) {
			logger.error("Application exception overridden by rollback error", ex);
			throw err;
		}
	}

	private DbmUtils(){
	}
}
