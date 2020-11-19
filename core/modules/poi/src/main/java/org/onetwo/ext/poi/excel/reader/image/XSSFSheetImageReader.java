package org.onetwo.ext.poi.excel.reader.image;

import java.util.List;
import java.util.Map;

import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTMarker;

import com.google.common.collect.Maps;

/**
 * @author weishao zeng
 * <br/>
 */

public class XSSFSheetImageReader implements SheetImageReader {
	
	@Override
	public boolean match(Sheet s) {
		return s instanceof XSSFSheet;
	}

	@Override
	public Map<Integer, Map<Integer, PictureData>> readPictureDatas(Sheet s) {
		XSSFSheet sheet = (XSSFSheet) s;
		Map<Integer, Map<Integer, PictureData>> sheetDatas = Maps.newHashMap();
		List<POIXMLDocumentPart> parts = sheet.getRelations();
		parts.stream().filter(part -> {
			return part instanceof XSSFDrawing;
		}).forEach(part -> {
			XSSFDrawing drawing = (XSSFDrawing) part;
			List<XSSFShape> shapes = drawing.getShapes();
			for (XSSFShape shape : shapes) {
                XSSFPicture picture = (XSSFPicture) shape;
                XSSFClientAnchor anchor = picture.getPreferredSize();
                CTMarker marker = anchor.getFrom();
                Map<Integer, PictureData> row = sheetDatas.get(marker.getRow());
                if (row==null) {
                	row = Maps.newHashMap();
                	sheetDatas.put(marker.getRow(), row);
                }
                row.put(marker.getCol(), picture.getPictureData());
            }
		});
		return sheetDatas;
	}

}
