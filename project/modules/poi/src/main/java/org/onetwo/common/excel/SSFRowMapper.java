package org.onetwo.common.excel;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.onetwo.common.excel.ExcelReader.CellValueConvertor;

public interface SSFRowMapper<T> {

	String getMapperName();
	
	/******
	 * 读取标题行
	 * @param sheet
	 * @return
	 */
	public List<String> mapTitleRow(int sheetIndex, Sheet sheet);
	/********
	 * 类型转换
	 * @param name
	 * @return
	 */
	public CellValueConvertor getCellValueConvertor(String name);
	/******
	 * 数据开始行号
	 * @return
	 */
	public int getDataRowStartIndex();
	public T mapDataRow(Sheet sheet, List<String> names, Row row, int rowIndex);

	public SSFRowMapper<T> register(String type, CellValueConvertor convertor);
	public SSFRowMapper<T> register(Map<String, CellValueConvertor> convertors);

}
