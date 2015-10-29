package org.onetwo.common.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

public class FileOpsTest {
	
	@Test
	public void testListDir() throws Exception{
		Path dir = Paths.get("D:/mydev/java/workspace/bitbucket/onetwo/core/modules/common", "src/test", "resources");
		//list不会递归子目录
		Files.list(dir).forEach(System.out::println);
		Files.list(dir).filter(path->{
			System.out.println("path:"+path.getFileName());
			System.out.println("path:"+path.toFile().getName());
			return path.getFileName().toString().equals("ansi.txt");
		})
		.findFirst()
		.get();

		//倒序
		List<Path> paths = Files.list(dir).sorted(Comparator.comparingLong((Path path)->path.toFile().lastModified()))
										.collect(Collectors.collectingAndThen(Collectors.toList(), lists->{
//											System.out.println("path1:"+path.getClass());
											Collections.reverse(lists);
											return lists;
										}));
		System.out.println("lastModify:"+paths.get(0));
	}

}
