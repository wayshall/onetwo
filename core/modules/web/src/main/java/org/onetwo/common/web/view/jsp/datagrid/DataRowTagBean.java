package org.onetwo.common.web.view.jsp.datagrid;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.tagext.BodyContent;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.view.jsp.grid.FieldTagBean;
import org.onetwo.common.web.view.jsp.grid.RowTagBean;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessor;

public class DataRowTagBean extends RowTagBean {
	private Iterator<?> iterator;
	private List<GridRowData> datas;
	private GridRowData currentRowData;
	
	private boolean fieldTagCompletion;
	
	public DataRowTagBean(RowType type) {
		super(type);
	}
	public void addField(FieldTagBean field){
		if(fieldTagCompletion)
			return ;
		super.addField(field);
		if(field.isSearchable()){
			getGridTagBean().addSearchField(field);
		}
	}
	
	public Iterator<?> getIterator() {
		return iterator;
	}
	public void setIterator(Iterator<?> iterator) {
		this.iterator = iterator;
	}

	public List<GridRowData> getDatas() {
		return datas;
	}

	public void addRowData(GridRowData rowData) {
		datas.add(rowData);
	}

	public void setDatas(List<GridRowData> datas) {
		this.datas = datas;
	}

	public GridRowData getCurrentRowData() {
		return currentRowData;
	}

	public void setCurrentRowData(GridRowData currentRowData) {
		this.currentRowData = currentRowData;
	}


	public boolean isFieldTagCompletion() {
		return fieldTagCompletion;
	}

	public void setFieldTagCompletion(boolean fieldTagCompletion) {
		this.fieldTagCompletion = fieldTagCompletion;
	}


	public static class GridRowData implements Map<String, Object>{
		public static GridRowData create(RowTagBean row, Object originData, int index){
			return new GridRowData(row, originData, index);
		}
		
		private RowTagBean row;
		private final Object originData;
		private final PropertyAccessor accessor;
		private final int index;
		private final Map<String, Object> translateData = LangUtils.newHashMap();
		private GridRowData(RowTagBean row, Object originData, int index) {
			super();
			this.originData = originData;
			this.index = index;
			BeanWrapper bw = SpringUtils.newBeanWrapper(originData);
			this.accessor = bw;
			this.row = row;
		}
		public int getIndex() {
			return index;
		}
		public Object getOriginData() {
			return originData;
		}
		private Map<String, Object> getTranslateData() {
			return translateData;
		}

		private Object formatValue(Object value, String dataFormat){
			return LangUtils.formatValue(value, dataFormat);
		}
		
		/***
		 * 根据属性解释tag列值
		 * @param dft
		 * @return
		 */
		public Object translateFieldValue(DataFieldTag dft){
			FieldTagBean component = dft.getComponent();
			Object dataFieldValue= null;
			if(!component.isAutoRender()){
				BodyContent bc = dft.getBodyContent();
				if(bc!=null){
					dataFieldValue = putValue(component.getValue(), bc.getString(), component.getDataFormat());
				}
			}else{
				dataFieldValue = translateValue(component.getValue(), component.getDataFormat());
			}
			return dataFieldValue;
		}
		
		private Object translateValue(String name, String dataFormat){
			Object value = "";
			try {
				if(StringUtils.isNotBlank(name)){
					value = this.accessor.getPropertyValue(name);
					value = formatValue(value, dataFormat);
				}
			} catch (Exception e) {
				throw new BaseException("translate value error, name["+name+"], original["+originData+"]", e);
			}
			this.translateData.put(name, value);
			return value;
		}
		
		private Object putValue(String name, Object value, String dataFormat){
			Object actualValue = formatValue(value, dataFormat);
			this.translateData.put(name, actualValue);
			return actualValue;
		}
		
		/***
		 * 根据列名获取列值
		 * @param name
		 * @return
		 */
		public Object getByName(String name) {
			if(!row.containsField(name))
				return "";
			return get(row.getField(name).getValue());
		}
		
		public int size() {
			return translateData.size();
		}
		public boolean isEmpty() {
			return translateData.isEmpty();
		}
		public boolean containsKey(Object key) {
			return translateData.containsKey(key);
		}
		public boolean containsValue(Object value) {
			return translateData.containsValue(value);
		}
		public Object get(Object key) {
			if("translateData".equals(key)){//兼容tag里的${entity.translateData[field.value]}
				return getTranslateData();
			}
			return translateData.get(key);
		}
		public Object put(String key, Object value) {
			return translateData.put(key, value);
		}
		public Object remove(Object key) {
			return translateData.remove(key);
		}
		public void putAll(Map<? extends String, ? extends Object> m) {
			translateData.putAll(m);
		}
		public void clear() {
			translateData.clear();
		}
		public Set<String> keySet() {
			return translateData.keySet();
		}
		public Collection<Object> values() {
			return translateData.values();
		}
		public Set<java.util.Map.Entry<String, Object>> entrySet() {
			return translateData.entrySet();
		}
		public boolean equals(Object o) {
			return translateData.equals(o);
		}
		public int hashCode() {
			return translateData.hashCode();
		}
		
		
		
	}

}
