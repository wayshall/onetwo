package org.onetwo.common.excel.etemplate.directive;

import java.util.List;
import java.util.regex.Matcher;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.onetwo.common.excel.etemplate.ETSheetContext.ETRowContext;
import org.onetwo.common.excel.etemplate.ETemplateContext;
import org.onetwo.common.excel.etemplate.ExcelTemplateValueProvider;
import org.onetwo.common.excel.etemplate.directive.ForeachRowDirectiveModel.ForeachRowInfo;
import org.onetwo.common.excel.exception.ExcelException;
import org.onetwo.common.excel.utils.ExcelUtils;

public class ForeachRowDirective extends AbstractRowDirective<ForeachRowDirectiveModel> {
		
	public ForeachRowDirective() {
		super("rowList", 
			"^(?i)\\[list\\s+([#]?[\\w]+)\\s+as\\s+(\\w+)\\s*(,\\s*([\\w]+)\\s*)?\\]$", 
				"^(?i)\\[/list\\s*\\]$");
	}

	protected ForeachRowDirectiveModel createModelByStartMatcher(String text, Matcher matcher){
		ForeachRowDirectiveModel model = new ForeachRowDirectiveModel(text, matcher.group(1), matcher.group(2));
		if(matcher.groupCount()>=4){
			model.setIndexVar(matcher.group(4));
		}
		return model;
	}
	
	protected boolean isMatchEndDirectiveText(String text){
		if(ExcelUtils.isBlank(text))
			return false;
		Matcher matcher = getEndTag().matcher(text);
		return matcher.matches();
	}
	

	@Override
	public void excecute(ETRowContext rowContext, ForeachRowDirectiveModel forModel, ExcelTemplateValueProvider provider){
		Row endRow = forModel.getEndRow();
		Sheet sheet = endRow.getSheet();
		List<ForeachRowInfo> foreachRows = forModel.getMatchRows();

		Object listObject = provider.parseValue(forModel.getDataSource());
		if(!ExcelUtils.isMultiple(listObject)){
			throw new ExcelException("the ["+forModel.getDataSource()+"] must be a Collection or Array object");
		}

		List<?> datalist = ExcelUtils.tolist(listObject);
		forModel.setDataList(datalist);
		if(datalist.isEmpty()){
			return ;
		}
		
		int rownumb = forModel.getStartRow().getRowNum();
		//add row space
		ExcelUtils.addRow(sheet, rownumb, datalist.size()*forModel.getMatchRows().size());
		int dataIndex = 0;

		ETemplateContext templateContext = provider.getTemplateContext();
		//TODO 覆盖了外部变量，以后修改
		for(Object data : datalist){
			templateContext.put(forModel.getItemVar(), data);
			templateContext.put(forModel.getIndexVar(), dataIndex);
			try {
				Row firstRow = null;
//				logger.info("sheet.getPhysicalNumberOfRows(): {}", sheet.getPhysicalNumberOfRows());
				for(ForeachRowInfo repeateRow : foreachRows){
					endRow = generateRow(repeateRow, provider, rownumb);
					processCommonRow(endRow, provider);
					if(firstRow==null){
						firstRow = endRow;
					}
					
					if(provider.isDebug()){
						logger.info("repeateRow: {}", repeateRow.getOriginRownum());
						logger.info("create row {} : {} ", rownumb);
						logger.info("firstRow: {}", firstRow.getRowNum());
					}
					rownumb++;
				}
//				logger.info("sheet.getPhysicalNumberOfRows(): {}", sheet.getPhysicalNumberOfRows());
				
//				rowContext.getSheetContext().getEngineer().parseRow(rowContext.getSheetContext(), firstRow);
			} finally{
				//clear
				templateContext.remove(forModel.getItemVar());
				templateContext.remove(forModel.getIndexVar());
			}
			dataIndex++;
		}
		
//		return endRow.getRowNum();
	}

	protected boolean isRemoveDiretiveRowAndCellRange(ForeachRowDirectiveModel model){
		//插入的数据行数大于指令的行数时，才移除指令行和cellrange
		return model.getDataList().size()>model.getLength();
	}
	

	protected Row generateRow(ForeachRowInfo repeateRow, final ExcelTemplateValueProvider provider, int rownumb){
		Sheet sheet = repeateRow.getRow().getSheet();
		Row newRow = sheet.createRow(rownumb);
		newRow.setHeight(repeateRow.getRow().getHeight());
		ExcelUtils.copyRow(sheet, newRow, repeateRow.getRow());
		return newRow;
	}
	protected Row generateRow2(ForeachRowInfo repeateRow, final ExcelTemplateValueProvider provider, int rownumb){
		Row endRow = repeateRow.getRow().getSheet().createRow(rownumb);
		endRow.setHeight(repeateRow.getRow().getHeight());
		for(Cell cell : repeateRow.getRow()){
			Cell newCell = endRow.createCell(cell.getColumnIndex());
			ExcelUtils.copyCellStyle(cell, newCell);
			Object newCellValue = provider.parseCellValue(cell, provider);
			ExcelUtils.setCellValue(newCell, newCellValue);
			
			CellRangeAddress cr = provider.getCellRange(repeateRow, cell);
			if(cr!=null){
				CellRangeAddress newCr = new CellRangeAddress(rownumb, 
												rownumb+(cr.getLastRow()-cr.getFirstRow()), 
												cr.getFirstColumn(), cr.getLastColumn());
				endRow.getSheet().addMergedRegion(newCr);
			}
		}
		return endRow;
	}

}
