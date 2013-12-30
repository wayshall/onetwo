package org.onetwo.common.excel;

import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public interface SSFRowMapper<T> {

	String getMapperName();
	
	/******
	 * 读取标题行
	 * @param sheet
	 * @return
	 */
	public List<String> mapTitleRow(int sheetIndex, Sheet sheet);
	
//	public CellValueConvertor getCellValueConvertor(String name);
	/******
	 * 数据开始行号 0-based
	 * @return
	 */
	public int getDataRowStartIndex();
	public T mapDataRow(Sheet sheet, List<String> names, Row row, int rowIndex);

	/*public SSFRowMapper<T> register(String type, CellValueConvertor convertor);
	public SSFRowMapper<T> register(Map<String, CellValueConvertor> convertors);*/

}
