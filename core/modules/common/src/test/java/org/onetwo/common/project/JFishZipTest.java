package org.onetwo.common.project;

import java.io.FileNotFoundException;

import org.junit.Test;
import org.onetwo.common.file.BufferedWriterWrapper;
import org.onetwo.common.file.FileUtils;

/**
 * @author wayshall
 * <br/>
 */
public class JFishZipTest {
	
	@Test
	public void testZipTBCJavaFiles(){
		String dir = "D:\\mydev\\java\\basedir";
		
		ProjectRefactor refactor = new ProjectRefactor(dir);
		refactor.newZipFile("C:\\Users\\way\\Desktop\\data\\tbc-sources.zip")
					.andSubDirIs("project1 project2", " ")
					.andPostfixIsAnyOf(".java")
					.andIgnoreDirContains("src/test", "target")
				.end()
				.execute();
	}
	

	
	@Test
	public void testPrintJavaFiles() throws FileNotFoundException{
		String dir = "D:\\mydev\\java\\basedir";
		
		ProjectRefactor refactor = new ProjectRefactor(dir);
		
		BufferedWriterWrapper bw = FileUtils.createOrGetBufferedWriter("C:\\Users\\way\\Desktop\\data\\test.md");
		
		FileProccessExecutor executor = (baseDir, file) -> {
			if (bw.getLineCount()>1500) {
				return false;
			}
			bw.writeLine("## " + file.getName());
			bw.writeLine("```Java");
			FileUtils.readFile(file, (line, index) -> {
				bw.writeLine(line);
				return true;
			});
			bw.writeLine("```");
			return true;
		};
		// 生成markdown目录
//		bw.writeLine("[toc]");
		refactor.newExecutor(executor)
		.andSubDirIs("project1 project2", " ")
		.andPostfixIsAnyOf(".java")
		.andIgnoreDirContains("src/test")
		.end()
		.execute();
		bw.closeQuietly();
	}
	

}
