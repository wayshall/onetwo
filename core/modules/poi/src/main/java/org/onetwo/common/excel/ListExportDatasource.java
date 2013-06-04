package org.onetwo.common.excel;

import java.util.List;

public class ListExportDatasource implements ExportDatasource {
	private TemplateModel tempalte;
	private List<?> datalist;
	
	public ListExportDatasource(TemplateModel tempalte, List<?> datalist) {
		super();
		this.tempalte = tempalte;
		this.datalist = datalist;
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
		if(tempalte.isMultiSheet()){
			return tempalte.getLabel() + sheetIndex;
		}else{
			return tempalte.getLabel();
		}
	}
	
	
}
