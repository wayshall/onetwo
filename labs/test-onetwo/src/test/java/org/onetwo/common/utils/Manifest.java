package org.onetwo.common.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class Manifest {
	
	public static void main(String[] args){
		String libDir = " ./lib/";
		String path = "D:\\mydev\\workspace\\qyscard_java_server\\lib";
		System.out.println("path: " + path);
		List<String> lib = listFilepath(path, ".jar");
		if(lib==null)
			return ;
		StringBuilder sb = new StringBuilder();
		sb.append(" .");
		for(String f : lib){
			sb.append(libDir).append(f);
		}
		System.out.println("jars :" + sb.toString());
		
	}
	
	public static List<String> listFilepath(String dirPath, final String fileType){
		List<String> list = new ArrayList<String>();
		File dir = new File(dirPath);
		File[] files = dir.listFiles(new FilenameFilter(){

			@Override
			public boolean accept(File dir, String name) {
				if(dir.isDirectory())
					return true;
				return name.endsWith(fileType);
			}
			
		});
		if(files==null)
			return null;
		for(File f : files){
			if(f.isFile())
				list.add(f.getName());
			else{
				List<File> l = listFile(f.getPath(), fileType);
				if(l==null)
					continue;
				for(File f1 : l){
					String p = f.getName()+"/"+f1.getName();
					list.add(p);
				}
			}
		}
		return list;
	}
	
	public static List listFile(String dirPath, final String fileType){
		List<File> list = new ArrayList<File>();
		File dir = new File(dirPath);
		File[] files = dir.listFiles(new FilenameFilter(){

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(fileType);
			}
			
		});
		if(files==null)
			return null;
		for(File f : files){
			if(f.isFile())
				list.add(f);
			else{
				List<File> l = listFile(f.getPath(), fileType);
				if(l!=null && !l.isEmpty())
					list.addAll(l);
			}
		}
		return list;
	}

}
