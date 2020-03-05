package org.onetwo.common.spring.utils;

import java.io.File;
import java.util.function.Predicate;

import org.onetwo.common.file.FileUtils;
import org.springframework.util.AntPathMatcher;

/**
 * @author weishao zeng
 * <br/>
 */

public class FileMatchers {
	
	private static final AntPathMatcher MATCHER = new AntPathMatcher();
	
	public static Predicate<File> nameIs(String fileName) {
		return f-> FileUtils.getFileName(f.getName()).equalsIgnoreCase(fileName);
	}
	
	public static Predicate<File> suffixIs(String suffix) {
		return f-> {
			String fileName = FileUtils.getFileName(f.getName());
			return fileName.endsWith(suffix);
		};
	}
	
	public static Predicate<File> pathContains(String search) {
		return f-> {
			String path = FileUtils.fixPath(f.getPath());
			return path.contains(search);
		};
	}
	
	public static Predicate<File> pathMatch(String antPattern) {
		return f-> {
			String path = FileUtils.fixPath(f.getPath());
			return MATCHER.match(antPattern, path);
		};
	}
	
	private FileMatchers() {
	}

}
