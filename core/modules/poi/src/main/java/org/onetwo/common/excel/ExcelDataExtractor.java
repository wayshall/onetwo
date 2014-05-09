package org.onetwo.common.excel;

import org.apache.poi.ss.usermodel.Sheet;

/****
 * sheet数据抽取接口
 * @author weishao
 *
 * @param <T>
 */
public interface ExcelDataExtractor<T> {

	public T extractData(Sheet sheet, int sheetIndex);

}