package org.onetwo.common.project;

import java.util.List;

import org.junit.Test;
import org.onetwo.common.file.FileUtils;

import com.google.common.collect.Lists;

/**
 * @author wayshall
 * <br/>
 */
public class JFishRefactorTest {
	
	@Test
	public void upgradeVersions(){
		String baseDir = "/Users/way/mydev/java/odysseus-branch";
		String dir = baseDir + "/onetwo/core/";
		String oldVersion = "5.0.0-SNAPSHOT";
		String newVersion = "5.2.7-SNAPSHOT";
		

		String dbmPath = baseDir + "/dbm";
		String wechatPath = baseDir + "/onetwo-wechat";
		String tccPath = baseDir + "/onetwo-tcc";
		String pluginPath = baseDir + "/onetwo-tcc";
		
		List<String> dirs = Lists.newArrayList();
		dirs.add(dir);
		dirs.add(dbmPath);
		dirs.add(wechatPath);
		dirs.add(tccPath);
		dirs.add(pluginPath);
		
		dirs.forEach(path -> {
			replaceVersions(path, oldVersion, newVersion);
		});
	}

	private void replaceVersions(String path, String oldVersion, String newVersion) {
		ProjectRefactor prj = new ProjectRefactor(path);
		prj.newFileReplacement()
					.file(f->FileUtils.getFileName(f.getName()).equals("pom.xml") || FileUtils.getFileName(f.getName()).equalsIgnoreCase("readme.md"))
					.textReplace("<version>"+oldVersion+"</version>", "<version>"+newVersion+"</version>")
					.textReplace("<onetwo.version>"+oldVersion+"</onetwo.version>", "<onetwo.version>"+newVersion+"</onetwo.version>")
				.end()
				.execute();
		
	}
}
