package org.onetwo.plugins.admin.web.tag;

import java.util.List;

import javax.annotation.Resource;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.view.jsp.form.FormFieldTag;
import org.onetwo.common.web.view.jsp.form.FormFieldType;
import org.onetwo.common.web.view.jsp.form.FormFieldTypePopulater;
import org.onetwo.common.web.view.jsp.form.FormItemsTagBean;
import org.onetwo.common.web.view.jsp.tools.ValueTag;
import org.onetwo.common.web.view.jsp.tools.ValueTagProvider;
import org.onetwo.plugins.admin.model.data.entity.DictionaryEntity;
import org.onetwo.plugins.admin.model.data.service.DictionaryService;

public class DictionaryPopulater implements FormFieldTypePopulater<FormItemsTagBean>, ValueTagProvider {
	
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
		List<DictionaryEntity> dicts = dictionaryService.findDataByPrefixCode(typeCode);
		itemsBean.setItemDatas(dicts);
	}

	@Override
	public String getValueType() {
		return getFieldType();
	}

	@Override
	public Object getValue(ValueTag tag) {
		Object type = tag.getDynamicAttribute("type");
		Object property = tag.getDynamicAttribute("property");
		DictionaryEntity result = dictionaryService.getData(tag.getValue(), StringUtils.emptyIfNull(type));
		if(result==null)
			return LangUtils.EMPTY_STRING;
		if(property==null){
			return result.getName();
		}else{
			return SpringUtils.newBeanWrapper(result).getPropertyValue(property.toString());
		}
	}
	
	
	

}
