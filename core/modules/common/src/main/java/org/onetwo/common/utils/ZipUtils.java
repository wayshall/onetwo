package org.onetwo.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.file.FileUtils;

public class ZipUtils {
//	private static final Logger logger = MyLoggerFactory.getLogger(ZipUtils.class);

	public static interface ZipFileFilter {
		boolean isFileWillZip(File file, String subFileName);
	}
	
	public static interface ZipEntryName {
		String entryName(File file);
	}
	
	/***
	 * 
	 * @author weishao zeng
	 * @param targetZipFilePath 目标文件，若目标文件不包含父目录，则以baseDir为父目录
	 * @param baseDir
	 * @param filter
	 * @return
	 */
	public static File zipfiles(String targetZipFilePath, String baseDir, ZipFileFilter filter){
		File baseDirFile = new File(baseDir);
		String baseDirPath = baseDirFile.getPath() + File.separator;
		List<File> files = FileUtils.listFile(baseDirFile, file -> {
			String subFileName = StringUtils.substringAfter(file.getPath(), baseDirPath);
			return filter.isFileWillZip(file, subFileName);
		});
		if (files.isEmpty()) {
			throw new BaseException("no file will be zip!");
		}
		File targetZipFile = new File(targetZipFilePath);
		if (targetZipFile.getParentFile()==null) {
			targetZipFilePath = baseDirPath + targetZipFilePath;
		}
		return zipfileList(targetZipFilePath, FileUtils.UTF8, files, f -> {
			String subFileName = StringUtils.substringAfter(f.getPath(), baseDirPath);
			return subFileName;
		});
	}
	
	public static File zipfile(String targetZipFilePath, File file){
		return zipfile(targetZipFilePath, file, file.isFile());
	}
	
	public static File zipfile(String targetZipFilePath, File file, boolean isfile){
		if(isfile){
			return zipfiles(targetZipFilePath, file);
		}else{
			List<File> files = FileUtils.listFile(file);
			return zipfiles(targetZipFilePath, files.toArray(new File[0]));
		}
	}

	public static File zipfile(File file, String encoding){
		String targetZipFilePath = FileUtils.getNewFilenameBy(file, ".zip");
		return zipfile(targetZipFilePath, file);
	}

	public static File zipfiles(String targetZipFilePath, File...files){
		return zipfiles(targetZipFilePath, FileUtils.UTF8, files);
	}
	
	public static File zipfiles(String targetZipFilePath, String encoding, File...files){
		return zipfileList(targetZipFilePath, encoding, Arrays.asList(files), (ZipEntryName)null);
	}
	
	/***
	 * 
	 * @author weishao zeng
	 * @param targetZipFilePath
	 * @param encoding
	 * @param files
	 * @param baseDirPath 压缩的基准目录，为空，则不创建目录，所有文件压缩在同一个目录下
	 * @return
	 */
	public static File zipfileList(String targetZipFilePath, List<File> files, String baseDirPath){
		return zipfileList(targetZipFilePath, FileUtils.UTF8, files, f -> {
			if (StringUtils.isBlank(baseDirPath)) {
				return f.getName();
			}
			String subFileName = StringUtils.substringAfter(f.getPath(), baseDirPath);
			return subFileName;
		});
	}
	
	public static File zipfileList(String targetZipFilePath, String encoding, List<File> files, ZipEntryName zipEntryName){
		Assert.notEmpty(files);
		File zipfile = new File(targetZipFilePath);
		FileUtils.makeDirs(zipfile, true);
		ZipOutputStream zipout = null;
		try {
			zipout = new ZipOutputStream(new FileOutputStream(zipfile));
			zipout.setEncoding(encoding);
			for(File f : files){
				String entryName = f.getName();
				if (zipEntryName!=null) {
					entryName = zipEntryName.entryName(f);
				}
				ZipEntry zipentry = new ZipEntry(entryName);
//				logger.info("put entry: " + entryName);
				zipout.putNextEntry(zipentry);
				FileUtils.copyFileToOutputStream(zipout, f);
			}
			zipout.finish();
		} catch (Exception e) {
			throw new BaseException("zip file error: " + e.getMessage(), e);
		} finally{
			FileUtils.close(zipout);
		}
		return zipfile;
	}
	
	private ZipUtils() {
	}

}
