package org.onetwo.ext.poi.excel.generator;

import org.onetwo.ext.poi.excel.data.RowContextData;


public interface RowProcessor {
	public void processRow(RowContextData rowContext);
}
