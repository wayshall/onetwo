package org.onetwo.plugins.codegen.generator;

import org.junit.Test;
import org.onetwo.plugins.codegen.CodegenBaseTest;
import org.onetwo.plugins.codegen.db.SqlTypeFactory.DataBase;
import org.springframework.beans.factory.annotation.Autowired;

public class CodegenServiceImplTest extends CodegenBaseTest {

	@Autowired
	private DefaultCodegenServiceImpl codegenServiceImpl;
	
	@Test
	public void testGen(){
		GenContext context = new GenContext();
//		context.setBasePackage("org.onetw3.test");
		context.setGenerateOutDir("e:/test/java/");
		context.setTablePrefix("cm_");
		
		CommonlContextBuilder b = new CommonlContextBuilder(DataBase.MySQL);
		this.codegenServiceImpl.addContextBuilder(b);
		this.codegenServiceImpl.generateFile("cm_users", context);
	}
}
