package org.onetwo.common.utils;


public class MergeHpTxtFile {
	
	public static void main(String[] args){
		String mergeDir = "E:/mydev/xianda/data/productCardData/B卡返回数据/清远市民卡第三批发货22345枚返回数据/";
//		String mergedFileName = "proc-all-"+DateUtil.format("yyyy-MM-dd-HHmmss", new Date())+".sql";
		String mergedFileName = "a.txt";
		FileUtils.mergeFiles(MergeFileConfig.build("gbk", mergeDir+mergedFileName, 
				mergeDir+"返回数据", ".txt", new DefaultMergeListener(){

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
