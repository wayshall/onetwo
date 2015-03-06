package org.onetwo.bcard;

import org.onetwo.common.utils.DefaultMergeListener;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.MergeFileConfig;
import org.onetwo.common.utils.MergeFileContext;


public class MergeHpTxtFile {
	
	public static void main(String[] args){
		String mergeDir = "D:\\mydev\\mydoc\\50hp\\";
//		String mergedFileName = "proc-all-"+DateUtil.format("yyyy-MM-dd-HHmmss", new Date())+".sql";
		String mergedFileName = "50.txt";
		FileUtils.mergeFiles(MergeFileConfig.build("gbk", mergeDir+mergedFileName, 
				mergeDir, ".txt", new DefaultMergeListener(){

					@Override
					public void onFileStart(MergeFileContext context) throws Exception {
						String fileName = FileUtils.getFileName(context.getFile().getPath());
						System.out.println("start merege file["+context.getFileIndex()+"] : " + fileName);
					}

					@Override
					public void onFileEnd(MergeFileContext context) throws Exception {
						String fileName = FileUtils.getFileName(context.getFile().getPath());
						System.out.println("mereged file["+context.getFileIndex()+"] : " + fileName );
					}

					@Override
					public void onEnd(MergeFileContext context) throws Exception {
						System.out.println("mereged line number: "+(context.getTotalLineIndex()+1)+"" );
					}
			
		}));
	}

}
