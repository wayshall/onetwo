package org.onetwo.ext.poi.excel.generator;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.onetwo.common.utils.Page;
import org.onetwo.common.utils.PageRequest;
import org.onetwo.ext.poi.excel.data.RowContextData;
import org.onetwo.ext.poi.excel.exception.ExcelException;
import org.onetwo.ext.poi.utils.ExcelUtils;
import org.onetwo.ext.poi.utils.PagableExcelDataReader;

public class IteratorRowProcessor extends DefaultRowProcessor {

	private RowProcessor titleRowProcessor;
	
	public IteratorRowProcessor(PoiExcelGenerator excelGenerator, RowProcessor titleRowProcessor) {
		super(excelGenerator);
		this.titleRowProcessor = titleRowProcessor;
	}
	
	public IteratorRowProcessor(PoiExcelGenerator excelGenerator, RowProcessor titleRowProcessor, FieldProccessor fieldProccessor) {
		super(excelGenerator, fieldProccessor);
		this.titleRowProcessor = titleRowProcessor;
	}

	public void processRow(RowContextData rowContext) {
		
		if(rowContext.getRowModel().isRenderHeader()){
//			RowModel header = rowModel.copy();
//			header.setType(RowModel.Type.HEADER_KEY);
			this.titleRowProcessor.processRow(rowContext);
		}
		processIterator(rowContext);
	}
	
	public int processIterator(RowContextData rowContext) {
		RowModel iterator = rowContext.getRowModel();
		Object dataSourceValue = rowContext.parseValue(iterator.getDatasource());
		int count = 0;
		if (dataSourceValue instanceof PagableExcelDataReader) {
			PagableExcelDataReader<?> pageReader = (PagableExcelDataReader<?>) dataSourceValue;
			PageRequest pageRequest = pageReader.initPageRequest();
			
			Page<?> page = pageReader.getNextPage(pageRequest);
			doProcessIterator(rowContext, page);
			while(page.isHasNext()) {
				pageRequest.setPage(pageRequest.getPage()+1);
				page = pageReader.getNextPage(pageRequest);
				count = count + doProcessIterator(rowContext, page);
			}
		} else {
			count = doProcessIterator(rowContext, dataSourceValue);
		}
		return count;
	}
	
	/****
	 * 
	 * @author weishao zeng
	 * @param rowContext
	 * @deprecated 使用支持分页的 {@link #processIterator(RowContextData)} 代替
	 * @return
	 */
	@Deprecated
	private int processIteratorBak(RowContextData rowContext) {
		RowModel iterator = rowContext.getRowModel();
		Object dataSourceValue = rowContext.parseValue(iterator.getDatasource());
//		Object dataSourceValue = rowContext.getSheetDatas();
		
		doProcessIterator(rowContext, dataSourceValue);
		int size = ExcelUtils.size(dataSourceValue);
		return size;
	}
	
	protected int doProcessIterator(RowContextData rowContext, Object dataSourceValue) {
//		Sheet sheet = rowContext.getSheet();
		RowModel iterator = rowContext.getRowModel();
		
		Iterator<?> it = ExcelUtils.convertIterator(dataSourceValue);
		if(it == null) {
			return 0;
		}
		
		int index = 0;
		Map<String, Object> context = rowContext.getSelfContext();
		String indexName = StringUtils.isBlank(iterator.getIndex())?"rowIndex":iterator.getIndex();

//		FieldProcessor fieldProcessor = getFieldProcessor(iterator, context);
		boolean hasRowCondition = StringUtils.isNotBlank(iterator.getCondition());
		
		long timeAt = System.currentTimeMillis();
		for (Object ele = null; it.hasNext(); index++) {
//			UtilTimerStack.push(iterator.getName());
			if(index!=0 && index%1000==0) {
				long costInMillis = System.currentTimeMillis() - timeAt;
				logger.info("create row " + index + ", cost time in Millis: " + costInMillis);
			}
			

//			String pname = "pre-field-"+index;
//			UtilTimerStack.push(pname);
			ele = it.next();
			rowContext.setCurrentRowObject(ele);
			

			if(context!=null){
				context.put(iterator.getName(), ele);
				context.put(indexName, index);
			}
			if(hasRowCondition){
				if(!conditionCanCreateRow(iterator.getCondition(), rowContext)){
					continue;
				}
			}
			
			Row row = createRow(rowContext);
			rowContext.setCurrentRow(row);
			
//			UtilTimerStack.pop(pname);

			FieldModel field = null;
			try {
				for (int i = 0; i < iterator.size(); i++) {
//					String name = "field-"+i;
//					UtilTimerStack.push(name);
					field = iterator.getField(i);
					Object rootValue = getFieldRootValue(rowContext, field);
//					this.processField(rootValue, rowContext, field);
					this.fieldProccessor.processField(rootValue, rowContext, field);
//					UtilTimerStack.pop(name);
				}
			} catch (Exception e) {
				throw new ExcelException("generate field["+iterator.getTemplate().getLabel()+","+iterator.getName()+","+field.getName()+"] error: "+e.getMessage() , e);
			}finally{
				rowContext.setCurrentRow(null);
				rowContext.setCurrentRowObject(null);
				if(context!=null){
					context.remove(iterator.getName());
					context.remove(indexName);
				}
			}

//			UtilTimerStack.pop(iterator.getName());
		}
		int size = ExcelUtils.size(dataSourceValue);
		return size;
	}
}
