package org.onetwo.common.excel;

import java.util.Map;

@SuppressWarnings("unchecked")
public interface RowExecutor {

	public void process(Map context, RowModel field, Object value);

}
