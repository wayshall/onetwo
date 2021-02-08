package org.onetwo.common.file;

import java.io.File;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.onetwo.common.utils.StringUtils;

import com.google.common.collect.Sets;

/**
 * @author weishao zeng
 * <br/>
 */
@FunctionalInterface
public interface FileMatcher {
	
	/****
	 * 文件是否匹配
	 * @author weishao zeng
	 * @param baseDirFile
	 * @param file
	 * @return
	 */
	boolean match(File baseDirFile, File file);
	

    default FileMatcher and(FileMatcher other) {
        Objects.requireNonNull(other);
        return (p, f) -> match(p, f) && other.match(p, f);
    }
	

    default FileMatcher or(FileMatcher other) {
        Objects.requireNonNull(other);
        return (p, f) -> match(p, f) || other.match(p, f);
    }
    
    /***
     * 文件名以指定后缀postfix中的一个结束
     * @author weishao zeng
     * @param postfix
     * @return
     */
	static public FileMatcher fileNameEndWith(String...postfix){
		return (baseDir, file)->{
			return Stream.of(postfix).anyMatch(suffix->{
				return file.getName().endsWith(suffix);
			});
		};
	}
	
	static public FileMatcher filePathContains(String...paths){
		return (baseDir, file)->{
			return Stream.of(paths).anyMatch(path->{
//				System.out.println("path: " + file.getPath().toLowerCase());;
				return file.getPath().toLowerCase().contains(path.toLowerCase());
			});
		};
	}

	static public FileMatcher subDirIs(String...dirs){
		Set<String> dirSet = Sets.newHashSet(dirs);
		return (baseDir, file) -> {
			String subdir = FileUtils.getSubdirOf(file, baseDir);
			if (StringUtils.isBlank(subdir)) {
				return false;
			}
			return dirSet.contains(subdir);
		};
	}

	static public FileMatcher relativeDirPathContains(String...dirs){
		return (baseDir, file) -> {
			return FileUtils.relativeDirPathContains(file, baseDir, dirs);
		};
	}
	
	static public FileMatcher dirNameIs(String...dirNames){
		return (baseDir, file)->{
			return Stream.of(dirNames).anyMatch(dirName->{
				boolean res = file.isDirectory() && file.getName().equals(dirName);
				return res;
			});
		};
	}
	
	static public FileMatcher fileNameIs(String...fileNames){
		return (baseDir, file)->{
			return Stream.of(fileNames).anyMatch(fileName->{
				return file.isFile() && file.getName().equals(fileName);
			});
		};
	}
	
	public class PredicateFileAdapter implements FileMatcher {
		private Predicate<File> predicate;

		public PredicateFileAdapter(Predicate<File> predicate) {
			super();
			this.predicate = predicate;
		}

		@Override
		public boolean match(File parentDirFile, File file) {
			return predicate.test(file);
		}
		
	}

}
