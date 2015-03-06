package org.onetwo.common.web.view.jsp.form;

import java.util.List;
import java.util.Map;

import org.onetwo.common.utils.CUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;

public class FormItemsTagBean extends FormFieldTagBean {

	private List<?> items;
	private String itemLabel;
	private String itemValue;

	private String emptyOptionLabel;
	

	public FormItemsTagBean() {
	}
	public FormItemsTagBean(Object itemDatas) {
		super();
		this.items = object2List(itemDatas);
	}
	
	private List<?> object2List(Object itemDatas){
		List<?> datas = null;
		if(Map.class.isInstance(itemDatas)){
			datas = CUtils.map2List((Map<Object, Object>)itemDatas);
		}else if(itemDatas!=null){
			datas = CUtils.tolist(itemDatas, false);
		}
		return datas;
	}
	

	public void setItemDatas(Object datas) {
		this.items = object2List(datas);
	}
	
	public List<?> getFieldValueList(){
		Object fd = this.getFormBean().getProvider().getFieldValue(this);
		return LangUtils.asList(fd);
	}
	
	public String getItemLabel() {
		return itemLabel;
	}
	public void setItemLabel(String itemLabel) {
		this.itemLabel = itemLabel;
	}
	public String getItemValue() {
		return itemValue;
	}
	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}
	public List<?> getItems() {
		return items;
	}
	public boolean isEmptyOption() {
		return StringUtils.isNotBlank(emptyOptionLabel);
	}
	public String getEmptyOptionLabel() {
		return emptyOptionLabel;
	}
	public void setEmptyOptionLabel(String emptyOptionLabel) {
		this.emptyOptionLabel = emptyOptionLabel;
	}
}
