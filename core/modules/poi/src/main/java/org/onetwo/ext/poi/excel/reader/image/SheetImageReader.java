package org.onetwo.ext.poi.excel.reader.image;

import java.util.Map;

import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * @author weishao zeng
 * <br/>
 */

public interface SheetImageReader {
	
	boolean match(Sheet s);

	Map<Integer, Map<Integer, PictureData>> readPictureDatas(Sheet s);

}