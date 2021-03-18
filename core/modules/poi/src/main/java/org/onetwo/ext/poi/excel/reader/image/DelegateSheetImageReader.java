package org.onetwo.ext.poi.excel.reader.image;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Sheet;
import org.onetwo.common.exception.BaseException;

import com.google.common.collect.Lists;

/**
 * @author weishao zeng
 * <br/>
 */

public class DelegateSheetImageReader {
	
	final private List<SheetImageReader> readers = Lists.newArrayList();
	
	public DelegateSheetImageReader() {
		this.readers.add(new HSSFSheetImageReader());
		this.readers.add(new XSSFSheetImageReader());
	}
	

	public Map<Integer, Map<Integer, PictureData>> readPictureDatas(Sheet s) {
		SheetImageReader reader = this.readers.stream()
											.filter(r -> r.match(s))
											.findAny()
											.orElseThrow(() -> new BaseException("reader not found for: " + s.getClass()));
		return reader.readPictureDatas(s);
	}
	

}
