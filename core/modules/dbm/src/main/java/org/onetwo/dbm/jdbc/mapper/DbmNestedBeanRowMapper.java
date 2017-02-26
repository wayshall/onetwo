package org.onetwo.dbm.jdbc.mapper;

import java.beans.PropertyDescriptor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.onetwo.common.convert.Types;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.reflect.ClassIntroManager;
import org.onetwo.common.reflect.Intro;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.utils.JFishProperty;
import org.onetwo.dbm.annotation.DbmCascadeResult;
import org.onetwo.dbm.exception.DbmException;
import org.onetwo.dbm.jdbc.JdbcResultSetGetter;
import org.onetwo.dbm.jdbc.JdbcUtils;
import org.onetwo.dbm.utils.DbmUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.util.Assert;

import com.google.common.collect.Maps;

public class DbmNestedBeanRowMapper<T> implements RowMapper<T> {
	final protected Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	protected ConversionService conversionService = new DefaultConversionService();

	protected Class<T> mappedClass;
//	private DbmTypeMapping sqlTypeMapping;
	protected JdbcResultSetGetter jdbcResultSetGetter;
	protected ResultClassMapper resultClassMapper;

	/*public DbmBeanPropertyRowMapper() {
		super();
	}*/

	public DbmNestedBeanRowMapper(JdbcResultSetGetter jdbcResultSetGetter, Class<T> mappedClass, DbmCascadeResult dbmCascadeResult) {
		this.mappedClass = mappedClass;
		this.jdbcResultSetGetter = jdbcResultSetGetter;
		this.resultClassMapper = new ResultClassMapper(dbmCascadeResult.idField(), dbmCascadeResult.columnPrefix(), mappedClass);
	}

	public ConversionService getConversionService() {
		return conversionService;
	}

	public void setConversionService(ConversionService conversionService) {
		this.conversionService = conversionService;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		Assert.state(this.mappedClass != null, "Mapped class was not specified");
		ResultSetWrappingSqlRowSet resutSetWrapper = new ResultSetWrappingSqlRowSet(rs);
		SqlRowSetMetaData rsmd = resutSetWrapper.getMetaData();
		Map<String, Integer> names = DbmUtils.lookupColumnNames(rsmd);
		
		T mappedObject = (T)this.resultClassMapper.mapResult(names, resutSetWrapper);
		return mappedObject;
	}

	protected void initBeanWrapper(BeanWrapper bw) {
		ConversionService cs = getConversionService();
		if (cs != null) {
			bw.setConversionService(cs);
		}
	}


	
	class PropertyResultClassMapper extends ResultClassMapper {
		final private JFishProperty belongToProperty;
		public PropertyResultClassMapper(String idField, String columnPrefix, JFishProperty belongToProperty) {
			this(idField, columnPrefix, belongToProperty, belongToProperty.getType());
		}
		public PropertyResultClassMapper(String idField, String columnPrefix, JFishProperty belongToProperty, Class<?> propertyType) {
			super(idField, columnPrefix, propertyType);
			this.belongToProperty = belongToProperty;
		}
		public JFishProperty getBelongToProperty() {
			return belongToProperty;
		}
		public void linkToParent(BeanWrapper parent, Object propertyValue){
			if(propertyValue!=null){
				parent.setPropertyValue(belongToProperty.getName(), propertyValue);
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	class CollectionPropertyResultClassMapper extends PropertyResultClassMapper {
		private Intro<? extends Collection> collectionClassIntro;
		public CollectionPropertyResultClassMapper(
				String idField, String columnPrefix,
				JFishProperty belongToProperty) {
			super(idField, columnPrefix, belongToProperty, (Class<?>)belongToProperty.getFirstParameterType());
			if(belongToProperty.getFirstParameterType()==null){
				throw new DbmException("the collection property must be a parameterType: " + belongToProperty.getName());
			}
			collectionClassIntro = (Intro<? extends Collection>)belongToProperty.getBeanClassWrapper();
		}
		
		public void linkToParent(BeanWrapper parent, Object propertyValue){
			if(propertyValue==null){
				return ;
			}
			Collection values = (Collection)parent.getPropertyValue(getBelongToProperty().getName());
			if(values==null){
				values = collectionClassIntro.newInstance();
			}
			values.add(propertyValue);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	class MapPropertyResultClassMapper extends PropertyResultClassMapper {
		private Intro<? extends Map> collectionClassIntro;
		private Class<?> keyClass;
		public MapPropertyResultClassMapper(
				String idField, String columnPrefix,
				JFishProperty belongToProperty) {
			super(idField, columnPrefix, belongToProperty, (Class<?>)belongToProperty.getParameterType(1));
			this.keyClass = (Class<?>)belongToProperty.getParameterType(0);
			if(keyClass==null || getResultClass()==null){
				throw new DbmException("the Map property must be a parameterType: " + belongToProperty.getName());
			}
			collectionClassIntro = (Intro<? extends Map>)belongToProperty.getBeanClassWrapper();
		}
		
		public void linkToParent(BeanWrapper parent, Object propertyValue){
			if(propertyValue==null){
				return ;
			}
			if(!hasIdField()){
				throw new DbmException("no id configured for map : " + this.getBelongToProperty().getName());
			}
			Object id = ReflectUtils.getPropertyValue(propertyValue, getIdProperty().getName());
			if(id==null){
				throw new DbmException("id value can not be null for map : " + this.getBelongToProperty().getName());
			}
			id = Types.convertValue(id, keyClass);
			Map values = (Map)parent.getPropertyValue(getBelongToProperty().getName());
			if(values==null){
				values = collectionClassIntro.newInstance();
			}
			values.put(id, propertyValue);
		}
	}
	
	class ResultClassMapper {
		private String idField = "";
		private String columnPrefix = "";
		private Intro<?> classIntro;
		private JFishProperty idProperty;
		private Map<String, JFishProperty> simpleFields = Maps.newHashMap();
		private Map<String, PropertyResultClassMapper> complexFields = Maps.newHashMap();
		private Map<Integer, BeanWrapper> datas = Maps.newHashMap();
		private Class<?> resultClass;
		
		public ResultClassMapper(String idField, String columnPrefix, Class<?> mappedClass) {
			super();
			this.idField = idField;
			this.columnPrefix = columnPrefix;
//			this.classIntro = ClassIntroManager.getInstance().getIntro(mappedClass);
//			this.initialize(mappedClass);
			this.resultClass = mappedClass;
		}

		public Class<?> getResultClass() {
			return resultClass;
		}

		public JFishProperty getIdProperty() {
			return idProperty;
		}

		public boolean hasIdField(){
			return idProperty!=null;
		}
		
		public void initialize() {
			this.classIntro = ClassIntroManager.getInstance().getIntro(resultClass);
			this.idProperty = this.classIntro.getJFishProperty(idField, false);
			for (PropertyDescriptor pd : classIntro.getProperties()) {
				if(pd.getWriteMethod()==null){
					continue;
				}
				JFishProperty jproperty = classIntro.getJFishProperty(pd.getName(), false);
				if(jproperty.hasAnnotation(DbmCascadeResult.class)){
					DbmCascadeResult result = jproperty.getAnnotation(DbmCascadeResult.class);
					PropertyResultClassMapper propertyMapper = null;
					if(jproperty.isCollectionType()){
						propertyMapper = new CollectionPropertyResultClassMapper(result.idField(), appendPrefix(result.columnPrefix()), jproperty);
					}else if(jproperty.isMapType()){
						
					}else{
						propertyMapper = new PropertyResultClassMapper(result.idField(), appendPrefix(result.columnPrefix()), jproperty);
					}
					propertyMapper.initialize();
					complexFields.put(jproperty.getName(), propertyMapper);
				}else{
					this.simpleFields.put(JdbcUtils.lowerCaseName(pd.getName()), jproperty);
				}
			}
		}
		
		protected String appendPrefix(String name){
			return columnPrefix + name;
		}
		
		public Object mapResult(Map<String, Integer> names, ResultSetWrappingSqlRowSet resutSetWrapper){
			Integer hash = null;
			Object entity = null;
			BeanWrapper bw = null;
			if(hasIdField()){
				Object idValue = getPropertyValue(names, resutSetWrapper, idProperty);
				hash = idValue==null?null:idValue.hashCode();
				if(!datas.containsKey(hash)){
					bw = mapClassObject(names, resutSetWrapper);
					datas.put(hash, bw);
					entity = bw.getWrappedInstance();
				}else{
					bw = datas.get(hash);
				}
			}else{
				bw = mapClassObject(names, resutSetWrapper);
				hash = HashCodeBuilder.reflectionHashCode(entity);
				if(!datas.containsKey(hash)){
					datas.put(hash, bw);
					entity = bw.getWrappedInstance();
				}else{
					bw = datas.get(hash);
				}
			}
			for(PropertyResultClassMapper pm : this.complexFields.values()){
				Object propertyValue = pm.mapResult(names, resutSetWrapper);
				pm.linkToParent(bw, propertyValue);
			}
			return entity;
		}
		protected BeanWrapper mapClassObject(Map<String, Integer> names, ResultSetWrappingSqlRowSet resutSetWrapper){
			Object mappedObject = classIntro.newInstance();
			BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);
			initBeanWrapper(bw);
			
			for(Entry<String, JFishProperty> entry : simpleFields.entrySet()){
				JFishProperty jproperty = entry.getValue();
				Object value = getPropertyValue(names, resutSetWrapper, jproperty);
				if(value!=null){
					bw.setPropertyValue(jproperty.getName(), value);
				}
			}
			return bw;
		}
		public Object getPropertyValue(Map<String, Integer> names, ResultSetWrappingSqlRowSet resutSetWrapper, JFishProperty jproperty){
			Object value = null;
			String field = JdbcUtils.lowerCaseName(appendPrefix(jproperty.getName()));
			if(names.containsKey(field)){
				int index = names.get(field);
				value = getColumnValue(resutSetWrapper, index, jproperty.getPropertyDescriptor());
			}else if(names.containsKey(JdbcUtils.underscoreName(field))){
				int index = names.get(field);
				value = getColumnValue(resutSetWrapper, index, jproperty.getPropertyDescriptor());
			}
			return value;
		}

	}


	protected Object getColumnValue(ResultSetWrappingSqlRowSet rs, int index, PropertyDescriptor pd) {
		return jdbcResultSetGetter.getColumnValue(rs, index, pd);
	}
}
