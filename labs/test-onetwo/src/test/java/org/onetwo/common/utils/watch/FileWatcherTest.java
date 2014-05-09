package org.onetwo.common.utils.watch;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.LangUtils;
import org.onetwo.common.utils.propconf.ResourceAdapterImpl;

public class FileWatcherTest {
	
	private FileWatcher watcher;
	
	@Before
	public void setup(){
		watcher = FileWatcher.newWatcher(1);
	}
	
	@Test
	public void testWatch(){
		File file1 = new File(FileUtils.getResourcePath("test1.properties"));
		System.out.println(file1.getPath());
		File file2 = new File(FileUtils.getResourcePath("test2.properties"));
		File file3 = new File(FileUtils.getResourcePath("test3.properties"));
		
		watcher.watchFile(new FileChangeListener() {
			
			@Override
			public void fileChanged(File file) {
				System.out.println("file change: " + file.getPath());
				LangUtils.await(10);
			}
		}, new ResourceAdapterImpl<File>(file1), new ResourceAdapterImpl<File>(file2), new ResourceAdapterImpl<File>(file3));
		
		LangUtils.CONSOLE.exitIf("exit");
	}

}
