package org.onetwo.common.project;

import org.junit.Test;

/**
 * @author wayshall
 * <br/>
 */
public class JFishZipTest {
	
	@Test
	public void testZipTBCJavaFiles(){
		String dir = "D:\\mydev\\java\\basedir";
		
		ProjectRefactor refactor = new ProjectRefactor(dir);
		refactor.newZipFile("C:\\Users\\way\\Desktop\\data\\java-sources.zip")
					.andSubDirIs("project1 project2", " ")
					.andPostfixIsAnyOf(".java")
					.andIgnoreDirContains("src/test", "target")
				.end()
				.execute();
	}
	
}
