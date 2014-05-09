package org.onetwo.common.excel;


/****
 * 导出excel的数据源接口
 * 
 * @author wayshall
 *
 */
public interface SheetDatasource<T> {

	public T getSheetDataList(int sheetIndex);
	public String getSheetLabel(int sheetIndex);

}
