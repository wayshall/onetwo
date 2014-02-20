package org.onetwo.common.utils;

import java.io.File;
import java.io.Writer;

public class MergeFile {
	
	public static void main(String[] args){
		String mergeDir = "D:/mydev/workspace/dbproc/";
//		String mergedFileName = "proc-all-"+DateUtil.format("yyyy-MM-dd-HHmmss", new Date())+".sql";
		String mergedFileName = "proc-all.sql";
		FileUtils.mergeFiles(MergeFileConfig.build("unicode", mergeDir+mergedFileName, 
				mergeDir+"proc", ".sql", new DefaultMergeListener(){

					@Override
					public void onStart(Writer writer, File file, int fileIndex) throws Exception {
						String fileName = FileUtils.getFileName(file.getPath());
						System.out.println("merege file["+fileIndex+"] : " + fileName);
						writer.write("\n");
					}

					@Override
					public void writeLine(Writer writer, File file, int fileIndex, String line, int lineIndex) throws Exception {
						String lineUpper = line.trim().toUpperCase();
						if(lineUpper.startsWith("ALTER")){
							String[] lineUppers = StringUtils.split(lineUpper, " ");
							if(lineUppers[1].equals("PROCEDURE") || lineUppers[1].equals("FUNCTION")){
								String fileName = FileUtils.getFileName(file.getPath());
								System.out.println("add comment before line: "+line+"");
								writer.write("----------------"+fileName+"----------------\n");
							}
						}
						if(fileIndex!=0 && lineUpper.contains("USE [ICCARD]")){
							System.out.println("ignore line["+lineIndex+"]: " + line);
						}else{
							super.writeLine(writer, file, fileIndex, line, lineIndex);
						}
					}
			
		}));
	}

}
