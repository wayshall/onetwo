package org.onetwo.bcard;

import org.onetwo.common.utils.DefaultMergeListener;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.MergeFileConfig;
import org.onetwo.common.utils.MergeFileContext;
import org.onetwo.common.utils.StringUtils;


public class CompileDbprocMergeFile {
	
	public static void main(String[] args){
		String mergeDir = "D:/mydev/workspace/dbproc/";
//		String mergedFileName = "proc-all-"+DateUtil.format("yyyy-MM-dd-HHmmss", new Date())+".sql";
		String mergedFileName = "proc-all.sql";
		FileUtils.mergeFiles(MergeFileConfig.build("unicode", mergeDir+mergedFileName, 
				mergeDir+"proc", ".sql", new DefaultMergeListener(){

					@Override
					public void onFileStart(MergeFileContext context) throws Exception {
						String fileName = FileUtils.getFileName(context.getFile().getPath());
						System.out.println("merege file["+context.getFileIndex()+"] : " + fileName);
						context.getWriter().write("\n");
					}

					@Override
					public void writeLine(MergeFileContext context, String line, int lineIndex) throws Exception {
						String lineUpper = line.trim().toUpperCase();
						if(lineUpper.startsWith("ALTER") || lineUpper.startsWith("CREATE")){
							String[] lineUppers = StringUtils.split(lineUpper, " ");
							if(lineUppers[1].equals("PROCEDURE") || lineUppers[1].equals("FUNCTION")){
								String fileName = FileUtils.getFileName(context.getFile().getPath());
								System.out.println("add comment before line: "+line+"");
								context.getWriter().write("----------------"+fileName+"----------------\n");
							}
						}
						if(context.getFileIndex()!=0 && lineUpper.contains("USE [ICCARD]")){
							System.out.println("ignore line["+lineIndex+"]: " + line);
						}else{
							super.writeLine(context, line, lineIndex);
						}
					}
			
		}));
	}

}
