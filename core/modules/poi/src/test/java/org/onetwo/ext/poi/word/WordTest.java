package org.onetwo.ext.poi.word;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.junit.Test;
import org.onetwo.ext.poi.utils.ExcelUtils;
import org.onetwo.ext.poi.utils.ListExcelDataExtractor;
import org.onetwo.ext.poi.word.JavaClassInfo.FieldInfo;
import org.onetwo.ext.poi.wpf.BeanWpfRowMapper;

public class WordTest {
	
	private String filePath = "org/onetwo/common/word/test.docx";
	@Test
	public void testRead() throws Exception {
		String path = ExcelUtils.class.getClassLoader().getResource("").getPath()+filePath;
		OPCPackage docPackage = ExcelUtils.readPackage(path);

		Map<String, String> headerMapper = new HashMap<String, String>(){{
			put("名称", "name");
			put("类型", "type");
			put("是否必须", "required");
			put("描述", "comment");
		}};
		BeanWpfRowMapper<FieldInfo> mapper = new BeanWpfRowMapper<>(headerMapper, FieldInfo.class);
		ListExcelDataExtractor<FieldInfo, XWPFTable> paramExtractor = new ListExcelDataExtractor<>(mapper);
		
		System.out.println("=================================================================");
		XWPFDocument xwpf = new XWPFDocument(docPackage);
		List<XWPFTable> tables = xwpf.getTables();
		int index = 0;
		for(XWPFTable table : tables){
			if(index==0){
				JavaClassInfo clazz = new JavaClassInfo("Test");
				List<FieldInfo> fields = paramExtractor.extractData(table);
				clazz.setFields(fields);
//				System.out.println(clazz.toJavaString());
				System.out.println(clazz.toEsMappingString());
			}
			index++;
		}
		
	}

}
