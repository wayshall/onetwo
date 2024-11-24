package org.onetwo.ext.poi.excel.etemplate;
/**
 * @author wayshall
 * <br/>
 */

import java.util.List;
import java.util.Map;

public interface TemplateExcelDataProvider {
	
	/***
	 * 每个map作为一行数据作为生成excel的上下文
	 * @author wayshall
	 * @return
	 */
	List<Map<String, Object>> readDataList();

}
