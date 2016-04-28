package org.onetwo.common.excel.etemplate;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.onetwo.common.excel.ExcelUtils;
import org.onetwo.common.excel.etemplate.ETSheetContext.ETRowContext;
import org.onetwo.common.excel.etemplate.RowForeachDirectiveModel.ForeachRowInfo;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;

public class RowForeachDirective implements ETRowDirective {
		
	public final static Pattern PATTERN_START = Pattern.compile("^(?i)\\[list\\s+([#]?[\\w]+)\\s+as\\s+(\\w+)\\s*(,\\s*([\\w]+)\\s*)?\\]$");
	public final static Pattern PATTERN_END = Pattern.compile("^(?i)\\[/list\\s*\\]$");
	
	private final Logger logger = JFishLoggerFactory.logger(this.getClass());

	
	@Override
	public String getName() {
		return "rowList";
	}

	@Override
	public boolean isMatch(ETRowContext rowContext){
		String cellString = getRowString(rowContext.getTagRow());
		/*RowForeachDirectiveModel model =  matchStartDirectiveText(cellString);
		if(model!=null)
			model.setStartRow(row);
		return model;*/
		if(StringUtils.isBlank(cellString))
			return false;
		Matcher matcher = PATTERN_START.matcher(cellString);
		return matcher.matches();
	}
	
	protected String getRowString(Row row){
		Cell cell = row.getCell(0);
		Object cellValue = ExcelUtils.getCellValue(cell);
		if(cellValue==null)
			return null;
		String cellString = cellValue.toString();
		return cellString;
	}

	public boolean isMatchEnd(RowForeachDirectiveModel model, Row row){
		Cell cell = row.getCell(0);
		Object cellValue = ExcelUtils.getCellValue(cell);
		if(cellValue==null)
			return false;
		String cellString = cellValue.toString();
		if(isMatchEndDirectiveText(cellString)){
			model.setEndRow(row);
			model.setDirectiveEnd(cellString);
			logger.info("find end tag row: {}", row.getRowNum());
			return true;
		}
		return false;
	}
	
	/*protected RowForeachDirectiveModel matchStartDirectiveText(String text){
		if(StringUtils.isBlank(text))
			return null;
		Matcher matcher = PATTERN_START.matcher(text);
		if(!matcher.matches()){
			return null;
		}
		RowForeachDirectiveModel model = new RowForeachDirectiveModel(text, matcher.group(1), matcher.group(2));
		if(matcher.groupCount()>=4){
			model.setIndexVar(matcher.group(4));
		}
		return model;
	}*/
	
	protected RowForeachDirectiveModel createModelByDirectiveText(String text){
		Matcher matcher = PATTERN_START.matcher(text);
		if(!matcher.matches()){
			throw new RuntimeException("error tag: " + text);
		}
		RowForeachDirectiveModel model = new RowForeachDirectiveModel(text, matcher.group(1), matcher.group(2));
		if(matcher.groupCount()>=4){
			model.setIndexVar(matcher.group(4));
		}
		return model;
	}
	
	protected boolean isMatchEndDirectiveText(String text){
		if(StringUtils.isBlank(text))
			return false;
		Matcher matcher = PATTERN_END.matcher(text);
		return matcher.matches();
	}
	
//	@Override
	public boolean matchEnd(RowForeachDirectiveModel model, Row row){
		Row lastRow = row;
		Sheet sheet = row.getSheet();
		while(!isMatchEnd(model, lastRow)){
			logger.info("find list row: {}", lastRow.getRowNum());
			model.addMatchRow(lastRow);
			
			if(lastRow.getRowNum()+1>sheet.getPhysicalNumberOfRows())
				throw new BaseException("not end tag matched for: " + model.getDirectiveStart());
			
			lastRow = row.getSheet().getRow(lastRow.getRowNum()+1);
		}
		return true;
	}
	
	protected void removeDirectiveRowAndCellRange(Sheet sheet, RowForeachDirectiveModel forModel){
		ExcelUtils.removeCellRange(forModel.getStartRow());
		ExcelUtils.addRow(sheet, forModel.getStartRow().getRowNum(), -1);
		for(ForeachRowInfo repeateRow : forModel.getMatchRows()){
//			logger.info("repeateRow getOriginRownum: {}", repeateRow.getOriginRownum());
//			logger.info("repeateRow: {}", repeateRow.getRow().getRowNum());
			//移除指令里的cellrange，否则，当行上移的时候，指令行的cellrangge会被上移的行使用，样式变形
			ExcelUtils.removeCellRange(repeateRow.getRow());
			//remove expresson row
			ExcelUtils.addRow(sheet, repeateRow.getRow().getRowNum(), -1);
		}
		ExcelUtils.removeCellRange(forModel.getEndRow());
		ExcelUtils.addRow(sheet, forModel.getEndRow().getRowNum(), -1);
	}
	

	protected void clearDirectiveRowText(RowForeachDirectiveModel forModel){
		ExcelUtils.clearRowValue(forModel.getStartRow());
		for(ForeachRowInfo repeateRow : forModel.getMatchRows()){
			ExcelUtils.clearRowValue(repeateRow.getRow());
		}
		ExcelUtils.clearRowValue(forModel.getEndRow());
	}

	@Override
	public boolean excecute(ETRowContext rowContext){
		Row row = rowContext.getTagRow();
		String cellString = getRowString(rowContext.getTagRow());
		RowForeachDirectiveModel forModel = createModelByDirectiveText(cellString);
		forModel.setStartRow(row);
		
		Row nextRow = row.getSheet().getRow(row.getRowNum()+1);
		if(matchEnd(forModel, nextRow)){
			nextRow = row.getSheet().getRow(forModel.getEndRow().getRowNum()+1);
			
			try {
				excecute(rowContext, forModel);
			} catch (Exception e) {
				throw ExcelUtils.wrapAsUnCheckedException(e);
//				throw new RuntimeException("row:"+nextRow, e);
			}
			
			int lastRownumbAfterExecuteTag = nextRow.getRowNum()-1;
//			return rownumb;
			rowContext.setLastRownumbAfterExecuteTag(lastRownumbAfterExecuteTag);
			return true;
		}
		return false;
	}
	
	public void excecute(ETRowContext rowContext, RowForeachDirectiveModel forModel){
		final ExcelTemplateValueProvider provider = rowContext.getSheetContext().getValueProvider();
		
		Row endRow = forModel.getEndRow();
		Sheet sheet = endRow.getSheet();
		List<ForeachRowInfo> foreachRows = forModel.getMatchRows();

		Object listObject = provider.parseValue(forModel.getDataSource());
		if(!LangUtils.isMultiple(listObject)){
			throw new BaseException("the ["+forModel.getDataSource()+"] must be a Collection or Array object");
		}

		List<?> datalist = LangUtils.asList(listObject);
		if(LangUtils.isEmpty(datalist)){
			clearDirectiveRowText(forModel);
			return ;
		}
		
		int rownumb = forModel.getStartRow().getRowNum();
		//add row space
		ExcelUtils.addRow(sheet, rownumb, datalist.size());
		int dataIndex = 0;

		ETemplateContext templateContext = provider.getTemplateContext();
		//TODO 覆盖了外部变量，以后修改
		for(Object data : datalist){
			templateContext.put(forModel.getItemVar(), data);
			templateContext.put(forModel.getIndexVar(), dataIndex);
			try {
				for(ForeachRowInfo repeateRow : foreachRows){
					if(provider.isDebug()){
						logger.info("repeateRow: {}", repeateRow.getOriginRownum());
						logger.info("create row: " + rownumb);
					}
					endRow = generateRow(repeateRow, provider, rownumb);
					rownumb++;
				}
			} finally{
				//clear
				templateContext.remove(forModel.getItemVar());
				templateContext.remove(forModel.getIndexVar());
			}
			dataIndex++;
		}

		//插入的数据行数大于指令的行数时，才移除指令行和cellrange
		if(datalist.size()>forModel.getLength()){
			removeDirectiveRowAndCellRange(sheet, forModel);
//			clearDirectiveRowText(forModel);
		}else{
			clearDirectiveRowText(forModel);
		}
//		return endRow.getRowNum();
	}
	
	protected Row generateRow(ForeachRowInfo repeateRow, final ExcelTemplateValueProvider provider, int rownumb){
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

	
	protected Object getCellValue(Cell cell){
		return ExcelUtils.getCellValue(cell);
	}
}
