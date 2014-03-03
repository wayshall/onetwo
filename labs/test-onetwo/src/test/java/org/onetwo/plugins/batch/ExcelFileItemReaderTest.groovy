package org.onetwo.plugins.batch;

import org.junit.Assert;
import org.junit.Test
import org.onetwo.common.spring.SpringUtils

public class ExcelFileItemReaderTest {
	
	@Test
	public void testReadExcel() throws Exception{
		String path = "org/onetwo/plugins/batch/qingchengqu.xls";
		ExcelFileItemReader<BoxNumberEntity> reader = new ExcelFileItemReader<BoxNumberEntity>();
		reader.fastOpen(SpringUtils.classpath(path), new SimpleExcelMapper());
		BoxNumberEntity bn = null;
		int count = 0; 
		while((bn=reader.read())!=null){ 
			count++;
		}
		Assert.assertEquals(28, reader.getCurrentRowNumber());
		Assert.assertEquals(11, reader.getCurrentItemCount());//加标题
		Assert.assertEquals(10, count);
	}

	
}
