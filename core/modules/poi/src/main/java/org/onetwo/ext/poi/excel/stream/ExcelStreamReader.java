package org.onetwo.ext.poi.excel.stream;

import java.io.File;
import java.io.InputStream;

import org.onetwo.ext.poi.excel.stream.BaseExcelStreamReader.SheeDataModelConsumer;

/**
 * @author weishao zeng
 * <br/>
 */

public interface ExcelStreamReader {

	void from(String path, SheeDataModelConsumer consumer);

	void from(File file, SheeDataModelConsumer consumer);

	void from(InputStream inputStream, SheeDataModelConsumer consumer);

}