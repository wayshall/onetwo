package org.onetwo.ext.poi.excel.etemplate;

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
		private Row tagRow;
//		private Row currentRow;
//		private Row currentRownumb;
		private int lastRownumbAfterExecuteTag;
		
		public ETRowContext(Row currentRow) {
			super();
			this.tagRow = currentRow;
		}

		public ETSheetContext getSheetContext(){
			return ETSheetContext.this;
		}

		public Row getTagRow() {
			return tagRow;
		}

		public int getLastRownumbAfterExecuteTag() {
			return lastRownumbAfterExecuteTag;
		}
		public void setLastRownumbAfterExecuteTag(int lastRownumbAfterExecuteTag) {
			this.lastRownumbAfterExecuteTag = lastRownumbAfterExecuteTag;
		}


	}
}
