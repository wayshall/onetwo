package org.onetwo.plugins.admin.web.tag;

import java.util.List;

import javax.annotation.Resource;

import org.onetwo.common.web.view.jsp.form.FormFieldTag;
import org.onetwo.common.web.view.jsp.form.FormFieldType;
import org.onetwo.common.web.view.jsp.form.FormFieldTypePopulater;
import org.onetwo.common.web.view.jsp.form.FormItemsTagBean;
import org.onetwo.plugins.admin.model.data.entity.DictionaryEntity;
import org.onetwo.plugins.admin.model.data.service.DictionaryService;

public class DictionaryPopulater implements FormFieldTypePopulater<FormItemsTagBean> {
	
	@Resource
	private DictionaryService dictionaryService;

	@Override
	public String getFieldType() {
		return "dictionary";
	}
	
	@Override
	public FormItemsTagBean createTagBean(FormFieldTag tag) {
		return new FormItemsTagBean();
	}



	@Override
	public void populateFieldComponent(FormFieldTag tag, FormItemsTagBean itemsBean) {
		itemsBean.setType(FormFieldType.select);
		itemsBean.setItemLabel(tag.getItemLabel());
		itemsBean.setItemValue(tag.getItemValue());
		itemsBean.setEmptyOptionLabel(tag.getEmptyOptionLabel());
		
		String typeCode = tag.getItems().toString();
		List<DictionaryEntity> dicts = dictionaryService.findByPrefixCode(typeCode);
		itemsBean.setItemDatas(dicts);
	}
	
	

}
