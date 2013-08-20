package org.onetwo.common.web.view.jsp.datagrid;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.view.jsp.grid.FieldTagBean;
import org.onetwo.common.web.view.jsp.grid.RowTagBean;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;

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
			this.accessor = PropertyAccessorFactory.forBeanPropertyAccess(originData);
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
			if(StringUtils.isBlank(dataFormat))
				return value;
			Object actualValue;
			if(value instanceof Date){
				actualValue = DateUtil.format(dataFormat, (Date)value);
			}else if(value instanceof Number && dataFormat != null) {
				NumberFormat nf = new DecimalFormat(dataFormat);
				nf.setRoundingMode(RoundingMode.HALF_UP);
				actualValue = nf.format(value);
			}else{
				actualValue = value;
			}
			return actualValue;
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
