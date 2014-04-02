package org.onetwo.plugins.email;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.FileUtils;

public class ZipUtils {
//	private static final Logger logger = MyLoggerFactory.getLogger(ZipUtils.class);

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
		Assert.notEmpty(files);
		File zipfile = new File(targetZipFilePath);
		FileUtils.makeDirs(zipfile, true);
		ZipOutputStream zipout = null;
		try {
			zipout = new ZipOutputStream(new FileOutputStream(zipfile));
			zipout.setEncoding(encoding);
			for(File f : files){
				String entryName = f.getName();
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
