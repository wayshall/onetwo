package org.onetwo.ext.poi.excel.reader.image;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Sheet;

import com.google.common.collect.Maps;

/**
 * @author weishao zeng
 * <br/>
 */

public class HSSFSheetImageReader implements SheetImageReader {
	
	@Override
	public boolean match(Sheet s) {
		return s instanceof HSSFSheet;
	}

	public Map<Integer, Map<Integer, PictureData>> readPictureDatas(Sheet s) {
		HSSFSheet sheet = (HSSFSheet) s;
		Map<Integer, Map<Integer, PictureData>> sheetDatas = Maps.newHashMap();
		HSSFPatriarch patriarch = sheet.getDrawingPatriarch();
		List<HSSFShape> shapes = patriarch.getChildren();
		for (HSSFShape shape : shapes) {
            if (shape instanceof HSSFPicture) {
            	HSSFPicture picture = (HSSFPicture) shape;
            	HSSFClientAnchor cAnchor = (HSSFClientAnchor) picture.getAnchor();
	            Map<Integer, PictureData> row = sheetDatas.get(cAnchor.getRow1());
                if (row==null) {
                	row = Maps.newHashMap();
                	sheetDatas.put(cAnchor.getRow1(), row);
                }
                row.put((int)cAnchor.getCol1(), picture.getPictureData());
            }
        }
		return sheetDatas;
	}

}
