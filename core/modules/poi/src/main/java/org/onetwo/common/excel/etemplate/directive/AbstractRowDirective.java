package org.onetwo.common.excel.etemplate.directive;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.onetwo.common.excel.etemplate.AbstractDirectiveModel;
import org.onetwo.common.excel.etemplate.ETSheetContext.ETRowContext;
import org.onetwo.common.excel.etemplate.ExcelTemplateValueProvider;
import org.onetwo.common.excel.etemplate.directive.ForeachRowDirectiveModel.ForeachRowInfo;
import org.onetwo.common.excel.exception.ExcelException;
import org.onetwo.common.excel.utils.ExcelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

abstract public class AbstractRowDirective<T extends AbstractDirectiveModel> implements ETRowDirective {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final Pattern startTag;
	private final Pattern endTag;
	private final String name;
	
	public AbstractRowDirective(Pattern startTag, Pattern endTag, String name) {
		super();
		Assert.hasText(name);
		Assert.notNull(startTag);
		Assert.notNull(endTag);
		this.startTag = startTag;
		this.endTag = endTag;
		this.name = name;
	}

	public AbstractRowDirective(String name, String startTagText, String endTagText) {
		super();
		Assert.hasText(name);
		Assert.hasText(startTagText);
		Assert.hasText(endTagText);
		this.name = name;
		this.startTag = Pattern.compile(startTagText);
		this.endTag = Pattern.compile(endTagText);
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public boolean isMatch(ETRowContext rowContext){
		String cellString = getRowString(rowContext.getTagRow());
		/*RowForeachDirectiveModel model =  matchStartDirectiveText(cellString);
		if(model!=null)
			model.setStartRow(row);
		return model;*/
		if(ExcelUtils.isBlank(cellString))
			return false;
		Matcher matcher = startTag.matcher(cellString);
		return matcher.matches();
	}
	
	protected T createModel(ETRowContext rowContext){
		String text = getRowString(rowContext.getTagRow());
		return createModel(text);
	}
	
	protected T createModel(String text){
		Matcher matcher = getStartTag().matcher(text);
		if(!matcher.matches()){
			throw new RuntimeException("error tag: " + text);
		}
		T model = createModelByStartMatcher(text, matcher);
		return model;
	}
	
	abstract protected T createModelByStartMatcher(String tagText, Matcher matcher);
	
	public boolean isMatchEnd(T model, Row row){
		/*Cell cell = row.getCell(0);
		Object cellValue = ExcelUtils.getCellValue(cell);
		if(cellValue==null)
			return false;
		String cellString = cellValue.toString();*/
		String cellString = getRowString(row);
		if(isMatchEndDirectiveText(cellString)){
			model.setEndRow(row);
			model.setDirectiveEnd(cellString);
			logger.info("find diretive[{}] end tag row: {}", getName(), row.getRowNum());
			return true;
		}
		return false;
	}
	
	protected boolean isMatchEndDirectiveText(String text){
		if(ExcelUtils.isBlank(text))
			return false;
		Matcher matcher = getEndTag().matcher(text);
		return matcher.matches();
	}
	
	public boolean matchEnd(T model, Row row){
		Row lastRow = row;
		Sheet sheet = row.getSheet();
		while(!isMatchEnd(model, lastRow)){
			logger.info("find diretive[{}] list row: {}", getName(), lastRow.getRowNum());
			model.addMatchRow(lastRow);
			
			if(lastRow.getRowNum()+1>sheet.getPhysicalNumberOfRows())
				throw new ExcelException("not end tag matched for: " + model.getDirectiveStart());
			
			lastRow = row.getSheet().getRow(lastRow.getRowNum()+1);
		}
		return true;
	}
	
	@Override
	public boolean excecute(ETRowContext rowContext){
		//打印整个sheet用于调试……
		Row row = rowContext.getTagRow();
		T forModel = createModel(rowContext);
		forModel.setStartRow(row);
		
		
		Row nextRow = row.getSheet().getRow(row.getRowNum()+1);
		ExcelTemplateValueProvider provider = rowContext.getSheetContext().getValueProvider();
		if(matchEnd(forModel, nextRow)){

//			int endRowIndex = forModel.getEndRow().getRowNum();
//			logger.info("directive[{}] start row: {}", getName(), row.getRowNum());
//			logger.info("directive[{}] end row: {}", getName(), endRowIndex);
			
			
			int nextRowIndex = forModel.getEndRow().getRowNum()+1;
//			logger.info("sheet.getPhysicalNumberOfRows(): {}", row.getSheet().getPhysicalNumberOfRows());
//			logger.info("directive[{}] crrent nextRow num: {}", getName(), nextRow.getRowNum());
//			logger.info("directive[{}] nextRowIndex: {}", getName(), nextRowIndex);
			nextRow = row.getSheet().getRow(nextRowIndex);
			
			try {
				excecute(rowContext, forModel, provider);
			} catch (Exception e) {
				throw ExcelUtils.wrapAsUnCheckedException(e);
//				throw new RuntimeException("row:"+nextRow, e);
			}
			
			if(isRemoveDiretiveRowAndCellRange(forModel)){
				this.removeDirectiveRowAndCellRange(forModel);
			}else{
				this.clearDirectiveRowText(forModel);
			}
			
			int lastRownumbAfterExecuteTag = nextRow.getRowNum()-1;
//			int lastRownumbAfterExecuteTag = endRowIndex;
//			return rownumb;
			rowContext.setLastRownumbAfterExecuteTag(lastRownumbAfterExecuteTag);
			return true;
		}
		return false;
	}
	
	protected boolean isRemoveDiretiveRowAndCellRange(T model){
		return true;
	}
	
	protected void clearDirectiveRowText(T forModel){
		ExcelUtils.clearRowValue(forModel.getStartRow());
		for(ForeachRowInfo repeateRow : forModel.getMatchRows()){
			ExcelUtils.clearRowValue(repeateRow.getRow());
		}
		ExcelUtils.clearRowValue(forModel.getEndRow());
	}

	protected void removeDirectiveRowAndCellRange(T forModel){
		Sheet sheet = forModel.getStartRow().getSheet();
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
	
	abstract protected void excecute(ETRowContext rowContext, T forModel, ExcelTemplateValueProvider provider);

	protected String getRowString(Row row){
		Cell cell = row.getCell(0);
		Object cellValue = ExcelUtils.getCellValue(cell);
		if(cellValue==null)
			return null;
		String cellString = cellValue.toString();
		return cellString;
	}
	
	protected void processCommonRow(Row row, ExcelTemplateValueProvider provider){
		ExcelUtils.parseCommonRow(row, provider);
	}

	public Pattern getStartTag() {
		return startTag;
	}

	public Pattern getEndTag() {
		return endTag;
	}
	
	protected Object getCellValue(Cell cell){
		return ExcelUtils.getCellValue(cell);
	}
}
