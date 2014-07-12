package org.onetwo.common.excel;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.LangUtils;

public class AcardAcceptExcelTest {


	private String outputPath = "org/onetwo/common/excel/";
	
	@Test
	public void test(){
		String path = FileUtils.getResourcePath(outputPath)+"acard-accept2.xlsx";
		
		final int startRow=1;
		Object[] prarm = new Object[]{ 0, "name", 1, "cardNo"};
		/****
		 * 把某列映射到对应对象的属性上
		 */
		BeanRowMapper<OldManCardVo> rowMapper = new BeanRowMapper<OldManCardVo>(OldManCardVo.class, prarm ){
			
			@Override
			protected boolean isIgnoreRow(Row row) {
				if(row==null){
					return true;
				}
				
				Cell cell = row.getCell(1);
				int lineNumb = cell.getRowIndex()+1;
				boolean res = Cell.CELL_TYPE_BLANK==cell.getCellType(); //第一格为空时，忽略改行
				if(!res&&lineNumb<startRow){
					return true;
				}
				return res;
			}
			
		};
		WorkbookReader reader = WorkbookReaderFactory.createWorkbookByMapper(rowMapper);
		//读取第一个sheet
		List<OldManCardVo> poslist = reader.readFirstSheet(path);
		for(OldManCardVo old : poslist){
			System.out.println("old: " + LangUtils.toString(old));
		}
	}
}
