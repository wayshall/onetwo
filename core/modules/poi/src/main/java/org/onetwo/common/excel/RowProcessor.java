package org.onetwo.common.excel;

import org.onetwo.common.excel.data.RowContextData;


public interface RowProcessor {
	public void processRow(RowContextData rowContext);
}
