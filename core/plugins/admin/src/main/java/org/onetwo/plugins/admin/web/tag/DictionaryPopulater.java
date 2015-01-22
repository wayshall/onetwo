package org.onetwo.plugins.admin.web.tag;

import java.util.List;

import javax.annotation.Resource;

import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.web.view.jsp.form.AbstractExtFormFieldPopulater;
import org.onetwo.common.web.view.jsp.form.FormFieldTag;
import org.onetwo.common.web.view.jsp.form.FormFieldTagBean;
import org.onetwo.common.web.view.jsp.form.FormItemsTagBean;
import org.onetwo.common.web.view.jsp.tools.ValueTag;
import org.onetwo.common.web.view.jsp.tools.ValueTagProvider;
import org.onetwo.plugins.admin.model.data.entity.DictionaryEntity;
import org.onetwo.plugins.admin.model.data.service.DictionaryService;

public class DictionaryPopulater extends AbstractExtFormFieldPopulater implements ValueTagProvider {
	
	@Resource
	private DictionaryService dictionaryService;

	@Override
	public String getProvider() {
		return "dictionary";
	}
	
	@Override
	public FormItemsTagBean createTagBean(FormFieldTag tag) {
		return new FormItemsTagBean();
	}



	@Override
	public void populateFieldComponent(FormFieldTag tag, FormFieldTagBean tagBean) {
		if(FormItemsTagBean.class.isInstance(tagBean)){
			FormItemsTagBean itemsBean = (FormItemsTagBean) tagBean;
			
			String typeCode = tag.getItems().toString();
			List<DictionaryEntity> dicts = dictionaryService.findDataByPrefixCode(typeCode);
			itemsBean.setItemDatas(dicts);
		}else{
			throw new IllegalArgumentException("error type for dictionary: " + tag.getType());
		}
	}

	@Override
	public String getValueProvider() {
		return getProvider();
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
