package org.onetwo.common.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.onetwo.common.excel.data.WorkbookData;
import org.onetwo.common.excel.interfaces.TemplateGenerator;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.utils.LangUtils;

abstract public class AbstractWorkbookExcelGenerator implements TemplateGenerator {

//	protected Workbook workbook;
	
	
	public void write(OutputStream out) {
		try {
			this.getWorkbook().write(out);
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			//LangUtils.closeIO(out);
		}
	}

	@Override
	public File generateTo(String path) {
		this.generateIt();
		return this.write(path);
	}

	public File write(String path) {
		File file = new File(path);
		if(!file.getParentFile().exists()){
			FileUtils.makeDirs(file.getParentFile().getPath(), false);
		}

		FileOutputStream fos = null;
		try {
			
			fos = new FileOutputStream(path);
			this.getWorkbook().write(fos);
		} catch (Exception e) {
			throw new ServiceException(e);
		} finally {
			LangUtils.closeIO(fos);
		}
		return file;
	}

	abstract public Workbook getWorkbook();
	
	abstract public WorkbookData getWorkbookData();

	@Override
	public String getFormat() {
		return getWorkbookData().getWorkbookModel().getFormat();
	}
	
	


}
