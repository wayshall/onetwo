package org.onetwo.ext.poi.excel.generator;

import java.util.Map;

import org.onetwo.ext.poi.utils.ExcelUtils;

@SuppressWarnings("unchecked")
public class ColumnTotalExecutor {

//	@Override
	public void process(Map context, FieldModel field, Object dataSourceValue) {
		String key = field.getParentRow().getName()+"_"+field.getName()+"_columnTotal";
		Number total = (Number) context.get(key);
		if(total==null){
			total = 0;
			context.put(key, total);
		}
		String exp = ExcelUtils.strings("#", key, "+", "#", field.getValue());
		try{
			total = (Number) ExcelUtils.getValue(exp, context, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		context.put(key, total);
	}

}
