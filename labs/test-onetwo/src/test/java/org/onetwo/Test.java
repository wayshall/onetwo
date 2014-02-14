package org.onetwo;

import java.io.File;

import org.onetwo.common.utils.FileUtils;




public class Test {
	
	public static void main(String[] args){
		FileUtils.mergeFiles("unicode", "D:/mydev/workspace/dbproc/proc/newfile.sql", "D:/mydev/workspace/dbproc/proc/", ".sql");
	}
	
}
