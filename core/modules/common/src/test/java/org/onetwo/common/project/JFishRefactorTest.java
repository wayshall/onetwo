package org.onetwo.common.project;

import org.junit.Test;
import org.onetwo.common.file.FileUtils;

/**
 * @author wayshall
 * <br/>
 */
public class JFishRefactorTest {
	
	@Test
	public void upgradeVersions(){
		String dir = "D:/mydev/java/workspace/bitbucket/onetwo/core/";
		String oldVersion = "4.7.1-SNAPSHOT";
		String newVersion = "4.7.2-SNAPSHOT";
		ProjectRefactor refactor = new ProjectRefactor(dir);
		refactor.newFileReplacement()
					.file(f->FileUtils.getFileName(f.getName()).equals("pom.xml") || FileUtils.getFileName(f.getName()).equalsIgnoreCase("readme.md"))
					.textReplace("<version>"+oldVersion+"</version>", "<version>"+newVersion+"</version>")
					.textReplace("<onetwo.version>"+oldVersion+"</onetwo.version>", "<onetwo.version>"+newVersion+"</onetwo.version>")
				.end()
				.execute();
		
		String dbmPath = "D:/mydev/java/workspace/bitbucket/dbm/";
		ProjectRefactor dbm = new ProjectRefactor(dbmPath);
		dbm.newFileReplacement()
					.file(f->FileUtils.getFileName(f.getName()).equals("pom.xml") || FileUtils.getFileName(f.getName()).equalsIgnoreCase("readme.md"))
					.textReplace("<version>"+oldVersion+"</version>", "<version>"+newVersion+"</version>")
					.textReplace("<onetwo.version>"+oldVersion+"</onetwo.version>", "<onetwo.version>"+newVersion+"</onetwo.version>")
				.end()
				.execute();
	}

}
