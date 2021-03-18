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
		String dir = "D:\\mydev\\java\\cloudsoft-workspace\\papabooking";
		String projectName = "papabooking";
		
		ProjectRefactor refactor = new ProjectRefactor(dir);
		refactor.newFileReplacement()
					.file(f->FileUtils.getFileName(f.getName()).equals("pom.xml"))
					.textReplace("project-template-springboot", projectName)
				.end()
				.newFileReplacement()
					.file(f->FileUtils.getFileName(f.getName()).endsWith(".yaml"))
					.textReplace("[project-template-springboot]", projectName)
					.textReplace("[jdbcUrl]", "host:port/dbName")
					.textReplace("[jdbcUser]", "dbUser")
					.textReplace("[jdbcPassword]", "dbPassword")
				.end()
				.newFileDelete()
					.orDirNameIs(".settings", "target")
					.orFileNameIs(".classpath", ".project")
				.end()
				.execute();
	}

}
