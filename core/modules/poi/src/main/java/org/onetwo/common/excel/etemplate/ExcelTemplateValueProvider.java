package org.onetwo.common.excel.etemplate;

import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.ValueProvider;

public class ExcelTemplateValueProvider implements ValueProvider {
	
	final private ETemplateContext context;
	
	public ExcelTemplateValueProvider(ETemplateContext context) {
		super();
		this.context = context;
	}

	public String findString(String var){
		Object val = context.get(var);
		return StringUtils.emptyIfNull(val);
	}

}
