package org.onetwo.common.excel;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.LangUtils;

public class FormulaTest {

	private String outputPath = "org/onetwo/common/excel/";
	
	@Test
	public void testFormula(){
		String path = FileUtils.getResourcePath(outputPath)+"公式.xlsx";
		
		
		WorkbookReader reader = WorkbookReaderFactory.createWorkbookByMapper(BeanRowMapper.map(FormulaVo.class, 3, "gender"));
		List<FormulaVo> list = reader.readFirstSheet(path);
		LangUtils.println("list:${0}", list);
		Assert.assertNotNull(list);
		Assert.assertEquals(2, list.size());
		Assert.assertEquals("女", list.get(0).getGender());
	}

	public static class FormulaVo {
		private String gender;

		public String getGender() {
			return gender;
		}

		public void setGender(String gender) {
			this.gender = gender;
		}
		
		
	}
}
