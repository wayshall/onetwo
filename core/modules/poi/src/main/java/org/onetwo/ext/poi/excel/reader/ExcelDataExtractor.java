package org.onetwo.ext.poi.excel.reader;

import org.apache.poi.ss.usermodel.Sheet;
import org.onetwo.ext.poi.utils.TableDataExtractor;


/****
 * sheet数据抽取接口
 * @author weishao
 *
 * @param <T>
 */
public interface ExcelDataExtractor<DATA> extends TableDataExtractor<DATA, Sheet>{

	public DATA extractData(Sheet sheet, int sheetIndex);

}