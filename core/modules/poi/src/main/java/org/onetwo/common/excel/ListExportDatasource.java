package org.onetwo.common.excel;

import java.util.List;

import org.onetwo.common.excel.data.WorkbookData;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.StringUtils;

public class ListExportDatasource implements ExportDatasource {
//	public static final Expression EXT_EXP = ExpressionFacotry.PERCENT;
	
	protected WorkbookData workbookData;
	private TemplateModel tempalte;
	private List<?> datalist;
	
	public ListExportDatasource(WorkbookData workbookData, TemplateModel tempalte, List<?> datalist) {
		super();
		this.tempalte = tempalte;
		this.datalist = datalist;
		this.workbookData = workbookData;
	}
	
	protected boolean isExpr(String str){
		if(StringUtils.isBlank(str))
			return false;
		str = str.trim();
		return str.startsWith("%{") && str.endsWith("}");
	}
	
	protected String getExpr(String str){
		return str.substring(2, str.length()-1);
	}

	@Override
	public List<?> getSheetDataList(int i) {
		if(tempalte.isMultiSheet()){
			int fromIndex = tempalte.getSizePerSheet()*i;
			int toIndex = tempalte.getSizePerSheet()*(i+1);
			if(toIndex>datalist.size())
				toIndex = datalist.size();

			if(fromIndex>toIndex)
				return null;
			return datalist.subList(fromIndex, toIndex);
		}else{
			return datalist;
		}
	}

	@Override
	public String getSheetLabel(int sheetIndex) {
		String label = tempalte.getLabel();
		/*if(EXT_EXP.isExpresstion(label)){
			label = EXT_EXP.parseByProvider(label, new ValueProvider() {
				
				@Override
				public String findString(String var) {
					Object val = workbookData.getExcelValueParser().parseValue(var);
					return StringUtils.emptyIfNull(val);
				}
			});
		}*/
		//以前label是不能写表达式的，为了兼容旧的写法，增加显式表达式语法
		if(isExpr(label)){
			label = getExpr(label);
			Object val = workbookData.parseValue(label);
			label = StringUtils.emptyIfNull(val);
		}
		Assert.hasText(label, "sheetName must not be null, empty, or blank. label:"+tempalte.getLabel());
		if(tempalte.isMultiSheet()){
			return label + sheetIndex;
		}else{
			return label;
		}
	}
	
	
}
