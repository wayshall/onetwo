package org.onetwo.dbm.ui.core;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.onetwo.common.db.spi.BaseEntityManager;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.dbm.mapping.DbmEnumValueMapping;
import org.onetwo.dbm.mapping.DbmMappedField;
import org.onetwo.dbm.ui.core.UISelectDataProvider.SelectQueryRequest;
import org.onetwo.dbm.ui.exception.DbmUIException;
import org.onetwo.dbm.ui.meta.DUIEntityMeta;
import org.onetwo.dbm.ui.meta.DUIFieldMeta;
import org.onetwo.dbm.ui.meta.DUIFieldMeta.DUISelectMeta;
import org.onetwo.dbm.ui.spi.DUIMetaManager;
import org.onetwo.dbm.ui.spi.DUISelectDataProviderService;
import org.onetwo.dbm.ui.vo.EnumDataVO;
import org.onetwo.dbm.ui.vo.UISelectDataRequest;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author weishao zeng
 * <br/>
 */

public class DefaultUISelectDataProviderService implements DUISelectDataProviderService {
	
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private DUIMetaManager uiclassMetaManager;
	@Autowired
	private BaseEntityManager baseEntityManager;
	@Autowired
	private DefaultUISelectDataProviderService _this;
	
	public Object getDatas(UISelectDataRequest request) {
		DUIEntityMeta meta = uiclassMetaManager.get(request.getEntity());
		DUIFieldMeta uifield = meta.getField(request.getField());
		DUISelectMeta uiselect = uifield.getSelect();
		if (uiselect==null) {
			throw new DbmUIException("ui select not found, entity name: " + request.getEntity() + ", field: " + request.getField());
		}
		return getDatas(uifield, request);
	}
	
	/***
	 * 获取列表显示时的值
	 * @author weishao zeng
	 * @param uiselect
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Object getListValue(DUIFieldMeta field, Object value) {
		DUISelectMeta uiselect = field.getSelect();
		if (uiselect.isTreeSelect()) {
			return "";
		}
		
		UISelectDataRequest request = new UISelectDataRequest();
//		request.setQuery(value==null?null:value.toString());
		request.setSelectedValue(value==null?null:value.toString());
		List<EnumDataVO> list = (List<EnumDataVO>)getDatas(field, request);
		Object compareValue = getCompareValue(field, value);		
		return list.stream()
				.filter(d -> d.getValue().equals(compareValue))
				.findAny()
				.map(d -> d.getLabel())
				.orElse("");
	}
	
	/***
	 * 枚举类型时，根据配置获取对象实际需要比较的值
	 * @param uiselect
	 * @param value
	 * @return
	 */
	public Object getCompareValue(DUIFieldMeta field, Object value) {
		DUISelectMeta uiselect = field.getSelect();
		Object compareValue = value;
		if (uiselect.useEnumData()) {
			DbmMappedField dbmField = uiselect.getField().getDbmField();
			BeanWrapper bw = SpringUtils.newBeanWrapper(value);
			if (dbmField.getEnumType()!=null) {
				compareValue = dbmField.getEnumType().forStore(dbmField, compareValue);
			} else if (value instanceof DbmEnumValueMapping) {
				compareValue = ((DbmEnumValueMapping<?>)value).getEnumMappingValue();
				
			} else if (bw.isReadableProperty("value")) {
				compareValue = bw.getPropertyValue("value");
			}
		}
		return compareValue;
	}
	
//	public List<?> getDatas(DUISelectMeta uiselect, Object query, boolean loadById) {
	public List<?> getDatas(DUIFieldMeta field, UISelectDataRequest request) {
		DUISelectMeta uiselect = field.getSelect();
		if (uiselect.useDataProvider()) {
			Class<? extends UISelectDataProvider> dataProviderClass = uiselect.getDataProvider();
			UISelectDataProvider dataProvider = (UISelectDataProvider)SpringUtils.getBean(applicationContext, dataProviderClass);
			if (dataProvider==null) {
				throw new DbmUIException("UISelectDataProvider not found for class: " + dataProviderClass);
			}
			
			SelectQueryRequest queryRequest = new SelectQueryRequest();
			queryRequest.setQuery(request.getQuery());
			queryRequest.setSelectedValue(request.getSelectedValue());
			if (uiselect.isTreeSelect()) {
				return dataProvider.findDatas(queryRequest);
			} else {
				return dataProvider.findDatas(queryRequest)
								.stream().map(d -> toEnumDataVO(field, d))
								.collect(Collectors.toList());
			}
			
		} else if (uiselect.getCascadeEntity()!=null) {
			boolean loadById = (StringUtils.isBlank(request.getQuery()) || request.isInitQuery()) && 
								StringUtils.isNotBlank(request.getSelectedValue());
			List<EnumDataVO> list = null;
			if (loadById) {
				list = _this.findByValueField(field, request.getSelectedValue());
			} else {
				list = _this.queryFromCascade(field, request.isInitQuery()?null:request.getQuery());
			}
			return list;
		} else if (uiselect.useEnumData()) {
			Enum<?>[] values = (Enum<?>[]) uiselect.getDataEnumClass().getEnumConstants();
//			DataBase[] vals = DataBase.class.getEnumConstants();
			List<EnumDataVO> list = Stream.of(values).filter(ev -> {
				return !ArrayUtils.contains(uiselect.getExcludeEnumNames(), ev.name());
			}).map(ev -> {
				EnumDataVO data = toEnumDataVO(field, ev);
				return data;
			}).collect(Collectors.toList());
			return list;
			
		} else {
			throw new DbmUIException("Neither enum nor dataProvider, field: " + uiselect.getField().getName());
		}
	}
	
	@Transactional
	public List<EnumDataVO> findByValueField(DUIFieldMeta field, Object value) {
		DUISelectMeta uiselect = field.getSelect();
		List<EnumDataVO> dataList = baseEntityManager.from(uiselect.getCascadeEntity())
				.where()
					.field(uiselect.getValueField())
						.when(()->value!=null)
						.is(value)
					.end()
				.limit(0, 10)
				.toQuery().list()
				.stream().map(e -> {
					return toEnumDataVO(field, e);
				}).collect(Collectors.toList());
		return dataList;
	}
	
	@Transactional
	public List<EnumDataVO> queryFromCascade(DUIFieldMeta field, String query) {
		DUISelectMeta uiselect = field.getSelect();
		List<EnumDataVO> dataList = baseEntityManager.from(uiselect.getCascadeEntity())
				.where()
					.field(uiselect.getCascadeQueryFields())
						.when(()->StringUtils.isNotBlank(query))
						.like(query)
					.end()
				.limit(0, field.getSelect().getQueryLimit())
				.toQuery().list()
				.stream().map(e -> {
					EnumDataVO d = toEnumDataVO(field, e);
					if (uiselect.isWithRawModel()) {
						d.setRawModel(e);
					}
					return d;
				}).collect(Collectors.toList());
		return dataList;
	}

	private EnumDataVO toEnumDataVO(DUIFieldMeta field, Object beanData) {
		DUISelectMeta uiselect = field.getSelect();
		EnumDataVO enumData = new EnumDataVO();
		BeanWrapper bw = SpringUtils.newBeanWrapper(beanData);
		String label = (String)bw.getPropertyValue(uiselect.getLabelField());
		
		if (field.getDbmField()!=null && field.getDbmField().isEnumerated()) {
			Object value = field.getDbmField().getActualStoreValue(beanData);
			enumData.setValue(value);
		} 
//		else if (beanData instanceof DbmEnumValueMapping) {
//			Object value = ((DbmEnumValueMapping<?>)beanData).getEnumMappingValue();
//			enumData.setValue(value);
//		} 
		else if (bw.isReadableProperty(uiselect.getValueField())) {
			Object value = bw.getPropertyValue(uiselect.getValueField());
			enumData.setValue(value);
		} else if (beanData instanceof Enum<?>){
			enumData.setValue(((Enum<?>)beanData).name());
		} else {
			throw new DbmUIException("property["+uiselect.getValueField()+"] not found for DUI select data: " + beanData);
		}
		enumData.setLabel(label);
		return enumData;
	}
}
