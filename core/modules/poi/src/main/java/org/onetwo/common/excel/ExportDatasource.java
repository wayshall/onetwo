package org.onetwo.common.excel;

import java.util.List;

/****
 * 导出excel的数据源接口
 * 
 * @author wayshall
 *
 */
public interface ExportDatasource {

	public List<?> getSheetDataList(int sheetIndex);
	public String getSheetLabel(int sheetIndex);

}
