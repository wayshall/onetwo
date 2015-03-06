package org.onetwo.bcard;

import java.util.Date;

import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.DefaultMergeListener;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.MergeFileConfig;
import org.onetwo.common.utils.MergeFileContext;


public class MergeSqlFile {


	public static void main(String[] args){
		mergeAttendWeb();
	}
	
	public static void mergeAttendWeb(){
		String mergeDir = "D:/mydev/workspace/xianda/attend-web/trunk/attend-web/doc/dbscript";
		String mergedFileName = mergeDir+"/create-tables-"+DateUtil.format("yyyy-MM-dd", new Date())+".sql";
		mergeSqlFiles(mergeDir, mergedFileName, "utf-8");
	}
	
	public static void mergeSqlFiles(String mergeDir, String mergedFileNameWithPath, String charset){
//		String mergeDir = "D:\\mydev\\workspace\\xianda\\dbproc\\";
//		String mergedFileName = "proc-all-"+DateUtil.format("yyyy-MM-dd-HHmmss", new Date())+".sql";
//		String mergedFileName = "proc-all.sql";
		FileUtils.mergeFiles(MergeFileConfig.build(charset, mergedFileNameWithPath, 
				mergeDir, ".sql", new DefaultMergeListener(){

					@Override
					public void onFileStart(MergeFileContext context) throws Exception {
						String fileName = FileUtils.getFileName(context.getFile().getPath());
						System.out.println("merege file["+context.getFileIndex()+"] : " + fileName);
						context.getWriter().write("\n");
					}
				}
		));
	}

}
