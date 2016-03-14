package org.onetwo.common.file;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.onetwo.common.exception.BaseException;

final public class FileOps {
	

	public static List<Path> getPaths(String dirString){
		return getPaths(dirString, Comparator.comparing(p->p));
	}
	public static List<Path> getPaths(String dirString, Comparator<Path> comparator){
		Objects.requireNonNull(comparator);
		Path dir = Paths.get(dirString);
		if(!dir.toFile().exists()){
			throw new BaseException("dir not exists: "+ dirString);
		}
		try(Stream<Path> s = Files.list(dir)) {
			return s.sorted(comparator).collect(Collectors.toList());
		} catch (Exception e) {
			throw new BaseException("get paths error.", e);
		}
	}
	
	private FileOps(){
	}

}
