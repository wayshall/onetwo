package org.onetwo.ext.poi.utils;

import java.util.List;

public interface TableRowMapper<DATA, TABLE> {

	public int getNumberOfRows(TABLE table);
	
//	String getMapperName();
	
	/******
	 * 读取标题行
	 * @param sheet
	 * @return
	 */
	public List<String> mapTitleRow(TABLE table);
	
//	public CellValueConvertor getCellValueConvertor(String name);
	/******
	 * 数据开始行号 0-based
	 * @return
	 */
	public int getDataRowStartIndex();
	public DATA mapDataRow(TABLE sheet, List<String> names, int rowIndex);

	/*public SSFRowMapper<T> register(String type, CellValueConvertor convertor);
	public SSFRowMapper<T> register(Map<String, CellValueConvertor> convertors);*/

}
