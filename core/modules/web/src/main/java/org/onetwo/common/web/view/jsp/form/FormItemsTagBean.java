package org.onetwo.common.web.view.jsp.form;

import java.util.List;
import java.util.Map;

import org.onetwo.common.utils.CUtils;

public class FormItemsTagBean extends FormFieldTagBean {

	private List<?> items;
	private String itemLabel;
	private String itemValue;
	

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
	public Object getFieldValue(){
		if(this.items!=null)
			return items;
		Object fd = this.getFormBean().getProvider().getFieldValue(getValue());
		this.items = object2List(fd);
		return this.items;
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
}
