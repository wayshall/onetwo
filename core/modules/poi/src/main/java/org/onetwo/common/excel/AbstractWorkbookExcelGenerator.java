package org.onetwo.common.excel;

import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.interfaces.excel.ExcelValueParser;
import org.onetwo.common.utils.LangUtils;

abstract public class AbstractWorkbookExcelGenerator implements PoiExcelGenerator {

	protected Workbook workbook;
	
	/* (non-Javadoc)
	 * @see com.project.base.excel.ExcelGenerator#write2File(java.io.OutputStream)
	 */
	public void write(OutputStream out) {
		try {
			this.getWorkbook().write(out);
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			//LangUtils.closeIO(out);
		}
	}

	/* (non-Javadoc)
	 * @see com.project.base.excel.ExcelGenerator#write2File(java.lang.String)
	 */
	public void write(String path) {
		FileOutputStream file = null;
		try {
			file = new FileOutputStream(path);
			this.getWorkbook().write(file);
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			LangUtils.closeIO(file);
		}
	}



}
