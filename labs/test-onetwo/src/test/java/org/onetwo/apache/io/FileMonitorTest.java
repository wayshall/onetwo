package org.onetwo.apache.io;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class FileMonitorTest {

	@Test
	public void testMonitor(){
		File parentDir = FileUtils.getFile(PARENT_DIR);
		 
        FileAlterationObserver observer = new FileAlterationObserver(parentDir);
        observer.addListener(new FileAlterationListenerAdaptor() {
 
                @Override
                public void onFileCreate(File file) {
                    System.out.println("File created: " + file.getName());
                }
 
                @Override
                public void onFileDelete(File file) {
                    System.out.println("File deleted: " + file.getName());
                }
 
                @Override
                public void onDirectoryCreate(File dir) {
                    System.out.println("Directory created: " + dir.getName());
                }
 
                @Override
                public void onDirectoryDelete(File dir) {
                    System.out.println("Directory deleted: " + dir.getName());
                }
        });
 
        // Add a monior that will check for events every x ms,
        // and attach all the different observers that we want.
        FileAlterationMonitor monitor = new FileAlterationMonitor(500, observer);
        try {
            monitor.start();
 
            // After we attached the monitor, we can create some files and directories
            // and see what happens!
            File newDir = new File(NEW_DIR);
            File newFile = new File(NEW_FILE);
 
            newDir.mkdirs();
            newFile.createNewFile();
 
            Thread.sleep(1000);
 
            FileDeleteStrategy.NORMAL.delete(newDir);
            FileDeleteStrategy.NORMAL.delete(newFile);
 
            Thread.sleep(1000);
 
            monitor.stop();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
