package org.onetwo.common.project;

import java.io.FileNotFoundException;

import org.junit.Test;
import org.onetwo.common.file.BufferedWriterWrapper;
import org.onetwo.common.file.FileUtils;

/**
 * @author wayshall
 * <br/>
 */
public class JFishSourceCode2Print {
	
	@Test
	public void testPrintJavaFiles() throws FileNotFoundException{
		String projectBaseDir = "/projectBaseDir";
		
		String outfile = projectBaseDir + "/subproject/doc/源码.md";
		printJavaSources(outfile, projectBaseDir, "module-cient module-service", ".java");
		
		outfile = projectBaseDir + "/subproject/doc/源码.md";
		printJavaSources(outfile, projectBaseDir, "module-cient module-service", ".java");
		
		String outfile2 = projectBaseDir + "/subproject/doc/前端源码.md";
		printJavaSources(outfile2, "/projectBaseDir/vue", "src", ".js", ".vue");
		
	}
	

	private void printJavaSources(String outFile, String projectBaseDir, String subProjects, String...postfix) throws FileNotFoundException{
		ProjectRefactor refactor = new ProjectRefactor(projectBaseDir);
		
		BufferedWriterWrapper bw = FileUtils.createOrGetBufferedWriter(outFile);
		
		FileProccessExecutor executor = (baseDir, file) -> {
			if (bw.getLineCount()>6000) {
				System.out.println("行数已够，停止输出: " + outFile);
				return false;
			}
			bw.writeLine("## " + file.getName());
			bw.writeLine("```java");
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
		.andSubDirIs(subProjects, " ")
//		.andFilePathContains("service", "spi", "util")
		.andPostfixIsAnyOf(postfix)
		.andIgnoreDirContains("src/test")
		.end()
		.execute();
		bw.closeQuietly();
	}
	


	

}
