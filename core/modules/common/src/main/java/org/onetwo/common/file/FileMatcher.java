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

	static public FileMatcher relativePathIs(String...relativePaths){
		Set<String> relativePathSet = Sets.newHashSet(relativePaths);
		return (baseDir, file) -> {
			String relativePath = FileUtils.getRelativeDirPath(file, baseDir);
			return relativePathSet.contains(relativePath);
		};
	}

	static public FileMatcher subDirsOfChildModuleIs(String...subDirsOfChildModule){
		Set<String> dirSet = Sets.newHashSet(subDirsOfChildModule);
		return (baseDir, file) -> {
			String childModuleBaseDir = FileUtils.getSubdirOf(file, baseDir);
			if (StringUtils.isBlank(childModuleBaseDir)) {
				return false;
			}
			String subDirOfChildModule = FileUtils.getSubdirOf(file, new File(baseDir, childModuleBaseDir));
			if (StringUtils.isBlank(subDirOfChildModule)) {
				return false;
			}
			return dirSet.contains(subDirOfChildModule);
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

	static public FileMatcher subDirIsNot(String...dirs){
		return (baseDir, file) -> {
			return !subDirIs(dirs).match(baseDir, file);
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
	
	/***
	 * 文件名（包括路径）
	 * @author weishao zeng
	 * @param fileNames
	 * @return
	 */
	static public FileMatcher fileNameIs(String...fileNames){
		return (baseDir, file)->{
			return Stream.of(fileNames).anyMatch(fileName->{
				return file.isFile() && file.getName().equals(fileName);
			});
		};
	}
	
	static public FileMatcher fileShortNameIs(String...shortNames){
		return (baseDir, file)->{
			return Stream.of(shortNames).anyMatch(fileName->{
				return file.isFile() && FileUtils.getFileName(file.getName()).equals(fileName);
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
