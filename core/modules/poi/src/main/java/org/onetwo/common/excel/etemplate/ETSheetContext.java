package org.onetwo.common.excel.etemplate;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class ETSheetContext {

	private Sheet sheet;
	
	final private ExcelTemplateEngineer engineer;
	final private ExcelTemplateValueProvider valueProvider;
	final private ETemplateContext templateContext;
	
	
	public ETSheetContext(ExcelTemplateEngineer engineer,
			ExcelTemplateValueProvider valueProvider,
			ETemplateContext templateContext) {
		super();
		this.engineer = engineer;
		this.valueProvider = valueProvider;
		this.templateContext = templateContext;
	}
	
	public Sheet getSheet() {
		return sheet;
	}

	public void setSheet(Sheet sheet) {
		this.sheet = sheet;
	}

	public ExcelTemplateEngineer getEngineer() {
		return engineer;
	}
	public ExcelTemplateValueProvider getValueProvider() {
		return valueProvider;
	}
	public ETemplateContext getTemplateContext() {
		return templateContext;
	}
	
	public class ETRowContext {
		private Row row;
		private int lastRownumbAfterExecuteTag;
		
		public ETRowContext(Row currentRow) {
			super();
			this.row = currentRow;
		}

		public ETSheetContext getSheetContext(){
			return ETSheetContext.this;
		}

		public Row getRow() {
			return row;
		}

		public int getLastRownumbAfterExecuteTag() {
			return lastRownumbAfterExecuteTag;
		}
		public void setLastRownumbAfterExecuteTag(int lastRownumbAfterExecuteTag) {
			this.lastRownumbAfterExecuteTag = lastRownumbAfterExecuteTag;
		}

	}
	
	public class ETCellContext {
		private Cell cell;
	}

}
