package org.onetwo.ext.poi.excel.generator;

import java.util.List;

/****
 * 导出excel的数据源接口
 * 主要是兼容旧代码……
 * @author wayshall
 *
 */
public interface ExportDatasource extends SheetDatasource<List<?>> {

	public List<?> getSheetDataList(int sheetIndex);

}
