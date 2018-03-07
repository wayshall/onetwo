package org.onetwo.common.project;

import org.junit.Test;
import org.onetwo.common.file.FileUtils;

/**
 * @author wayshall
 * <br/>
 */
public class ProjectRefactorTest {
	
	@Test
	public void testRenamePoms(){
		String dir = "dir path";
		ProjectRefactor refactor = new ProjectRefactor(dir);
		refactor.newFileReplacement()
					.fileMatcher(f->FileUtils.getFileName(f.getName()).equals("pom.xml"))
					.textReplace("source", "target")
				.end()
				.newFileDelete()
					.dirNameEqual(".settings", "target")
					.fileNameEqual(".classpath", ".project")
				.end()
				.execute();
	}

}
