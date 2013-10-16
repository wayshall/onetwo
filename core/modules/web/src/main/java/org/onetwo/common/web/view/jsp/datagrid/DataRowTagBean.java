package org.onetwo.common.web.view.jsp.datagrid;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.web.view.jsp.grid.FieldTagBean;
import org.onetwo.common.web.view.jsp.grid.RowTagBean;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessor;

public class DataRowTagBean extends RowTagBean {
	private Iterator<?> iterator;
	private List<Object> datas;
	private CurrentRowData currentRowData;
	
	private boolean fieldTagCompletion;
	
	public DataRowTagBean(RowType type) {
		super(type);
	}
	public void addField(FieldTagBean field){
		if(fieldTagCompletion)
			return ;
		super.addField(field);
		if(field.isSearchable()){
			getGridTagBean().getSearchFormBean().addField(field);
		}
	}
	
	public Iterator<?> getIterator() {
		return iterator;
	}
	public void setIterator(Iterator<?> iterator) {
		this.iterator = iterator;
	}

	public List<Object> getDatas() {
		return datas;
	}

	public void setDatas(List<Object> datas) {
		this.datas = datas;
	}

	public CurrentRowData getCurrentRowData() {
		return currentRowData;
	}

	public void setCurrentRowData(CurrentRowData currentRowData) {
		this.currentRowData = currentRowData;
	}


	public boolean isFieldTagCompletion() {
		return fieldTagCompletion;
	}

	public void setFieldTagCompletion(boolean fieldTagCompletion) {
		this.fieldTagCompletion = fieldTagCompletion;
	}


	public static class CurrentRowData {
		private final Object originData;
		private final PropertyAccessor accessor;
		private final int index;
		private final Map<String, Object> translateData = LangUtils.newHashMap();
		public CurrentRowData(Object originData, int index) {
			super();
			this.originData = originData;
			this.index = index;
			BeanWrapper bw = SpringUtils.newBeanWrapper(originData);
			this.accessor = bw;
		}
		public int getIndex() {
			return index;
		}
		public Object getOriginData() {
			return originData;
		}
		public Map<String, Object> getTranslateData() {
			return translateData;
		}

		protected Object formatValue(Object value, String dataFormat){
			return LangUtils.formatValue(value, dataFormat);
		}
		
		public void translateValue(String name, String dataFormat){
			try {
				Object value = this.accessor.getPropertyValue(name);
				this.translateData.put(name, formatValue(value, dataFormat));
			} catch (Exception e) {
				throw new BaseException("translate value error, name["+name+"], original["+originData+"]", e);
			}
		}
		
		public void putValue(String name, Object value, String dataFormat){
			this.translateData.put(name, formatValue(value, dataFormat));
		}
		
	}

}
