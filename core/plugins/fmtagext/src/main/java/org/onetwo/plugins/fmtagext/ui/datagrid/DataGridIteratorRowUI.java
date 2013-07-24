package org.onetwo.plugins.fmtagext.ui.datagrid;

import java.util.Collections;
import java.util.List;

import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.fmtagext.ui.UI.TemplateKeys;
import org.onetwo.plugins.fmtagext.ui.valuer.AbstractComponentUIValuer;
import org.onetwo.plugins.fmtagext.ui.valuer.UIValuer;

public class DataGridIteratorRowUI extends DataGridRowUI {

	
	public DataGridIteratorRowUI(DataGridUI parent) {
		super(parent);
	}

	@Override
	public String getTemplate() {
		return TemplateKeys.UI_GRID_ITERATOR_ROW;
	}

	
	protected UIValuer<?> createUIValuer(){
		return new AbstractComponentUIValuer<Object>(this){

			@Override
			public List<?> getUIValue(Object viewValue) {
				if(viewValue==null)
					return Collections.EMPTY_LIST;
				List<?> list = null;
				if(StringUtils.isNotBlank(value)){
					Object val;
					if(isProperty()){
						val = createUIValueProvider(viewValue).getValue(asProperty());
					}else{
						val = value;
					}
					list = LangUtils.asList(val);
				}else if(Page.class.isInstance(viewValue)){
					list = ((Page<?>)viewValue).getResult();
				}else{
					list = LangUtils.asList(viewValue);
				}
				return list;
			}
			
		};
	}

}
