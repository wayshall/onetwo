package org.onetwo.common.excel;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.onetwo.common.profiling.UtilTimerStack;
import org.onetwo.common.utils.LangUtils;

@SuppressWarnings("unchecked")
public class IteratorRowProcessor extends DefaultRowProcessor {

	private RowProcessor titleRowProcessor;
	
	public IteratorRowProcessor(PoiExcelGenerator excelGenerator, RowProcessor titleRowProcessor) {
		super(excelGenerator);
		this.titleRowProcessor = titleRowProcessor;
	}

	public void processRow(HSSFSheet sheet, RowModel rowModel) {
		if(rowModel.isRenderHeader()){
//			RowModel header = rowModel.copy();
//			header.setType(RowModel.Type.HEADER_KEY);
			this.titleRowProcessor.processRow(sheet, rowModel);
		}
		processIterator(sheet, rowModel);
	}
	
	@SuppressWarnings("rawtypes")
	public void processIterator(HSSFSheet sheet, RowModel iterator) {
		Object dataSourceValue = this.getGenerator().getExcelValueParser().parseValue(iterator.getDatasource(), null, null);
		Iterator it = LangUtils.convertIterator(dataSourceValue);
		if(it==null)
			return ;

		int index = 0;
		Map context = this.generator.getExcelValueParser().getContext();
		String indexName = StringUtils.isBlank(iterator.getIndex())?"rowIndex":iterator.getIndex();

		for (Object ele = null; it.hasNext(); index++) {
			UtilTimerStack.push(iterator.getName());
			
			ele = it.next();
			HSSFRow row = createRow(sheet, iterator, ele);
			
			if(context!=null){
				context.put(iterator.getName(), ele);
				context.put(indexName, index);
			}

			FieldModel field = null;

			for (int i = 0; i < iterator.size(); i++) {
				
				field = iterator.getField(i);
				field.setParentRow(iterator);

				/*if(profile){
					UtilTimerStack.push(field.getName());
				}*/
				
				//Cell cell = createCell(sheet, row, field);

				this.processField(getFieldRootValue(ele, field), row, field, "");
				// putInContext(field.getName(), v);

				if (field.getExecutors() == null)
					continue;

				for (FieldExecutor exe : field.getExecutors()) {
					if (exe != null)
						exe.process(context, field, dataSourceValue);
				}
				
				/*if(profile){
					UtilTimerStack.pop(field.getName());
				}*/

			}

			UtilTimerStack.pop(iterator.getName());
		}
		if(context!=null){
			context.remove(iterator.getName());
			context.remove(indexName);
		}
	}
}
