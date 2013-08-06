package org.onetwo.common.web.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.exception.WebException;
import org.onetwo.common.utils.DateUtil;
import org.onetwo.common.utils.FileUtils;
import org.onetwo.common.utils.MyUtils;
import org.onetwo.common.utils.StringUtils;

public class UploadUtil {

	protected Logger logger = Logger.getLogger(UploadUtil.class);
	
	private static UploadUtil instance = new UploadUtil();
	
	private  ResourceConfig conf = new ResourceConfig();
	
	public static UploadUtil getInstance() {
		return instance;
	}
	
	// 上传开始
	private  final Integer uploadbegin = 1;
	// 上传成功
	private  final Integer uploadsuccess = 2;
	// 上传失败
	private  final Integer uploadfail = 3;

	

	
	public String getResourceDomain() {
		return conf.getRsDomain();
	}
	
	/**
	 * 存入文件的相对路径，得到文件的绝对连接
	 * 
	 * @param filename
	 * @return
	 */
	public  String getResourceFileLink(String filename) {
		String url = conf.getRsDomain();

		if (filename == null || "".equals(filename)) {
			return url + conf.getDefaultpic();

		} else {

			String link = "";
			if (filename.startsWith("/")) {
				link = url + filename;

			} else {

				link = url + "/" + filename;
			}

			return link;
		}

	}
	
	
	public UploadInfo uploadFile(File file, String filename, String subfolder) {
		return this.saveFileToResource(file, filename, subfolder);
	}
	
	/**
	 * 编辑器自动上传图片
	 * @param url
	 * @param sourceId
	 * @param userId
	 * @param type
	 * @return
	 */
	public  UploadInfo uploadRemoteFile(String url, String subfolder) {
		String suffix = ".temp";
		String name = null;
		InputStream is = null;
		OutputStream os = null;
		File file = null;
		
		try {
			
			url = url.replaceAll("\\\\", "/");
			
			name = url.substring(url.lastIndexOf("/") + 1).replaceAll("(\\.\\w+).*$", "$1");
		
			file = File.createTempFile("temp", suffix);
			is = new URL(url).openStream();
			os = new FileOutputStream(file);
			
			int length = -1;
			for(byte[] b = new byte[1024]; (length = is.read(b)) != -1;) {
				os.write(b, 0, length);
			}
			os.flush();
			
			return saveFileToResource(file, name, subfolder);
		} catch (IOException e) {
			throw new ServiceException("上传远程资源出错:" + url, e);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (os != null) {
					os.close();
				}
				if(file != null && file.exists()) {
					file.delete();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
////////////////////////////////以下是私有的方法////////////////////////////////////////////////////
	
	public class UploadInfo {
		
		private String path;
		private String fileName;
		private long fileSize;
		private String originalName;
		private String url;
		
		private Integer status;
		
		public String getFileName() {
			return fileName;
		}
		
		public void setPath(String path) {
			this.path = path;
		}
		
		public String getPath() {
			return path;
		}
		
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		
		public long getFileSize() {
			return fileSize;
		}
		
		public void setFileSize(long fileSize) {
			this.fileSize = fileSize;
		}
		
		public String getOriginalName() {
			return originalName;
		}
		
		public void setOriginalName(String originalName) {
			this.originalName = originalName;
		}

		public Integer getStatus() {
			return status;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
	}
	
	/**
	 * 上传文件到资源库
	 * 
	 * @param tempFilePath
	 *            临时文件路径
	 * @param ownertable
	 *            所属者所在的表
	 * @param system
	 *            系统信息
	 * @param subsystem
	 *            子系统信息
	 * @return
	 */
	private  UploadInfo saveFileToResource(File file, String filename, String subfolder) {

		try {
			
			if(subfolder == null || subfolder.length() == 0) {
				throw new WebException("上传附件时，子文件夹不能为空!");
			}

			if (file.exists()) {
				// 生成文件名称
				Date nowdate = new Date();
				String realfilename = nowdate.getTime()
						+ ""
						+ MyUtils.getRadomString(6)
						+ "."
						+ org.onetwo.common.utils.FileUtils.getExtendName(filename).toLowerCase();

				UploadInfo rsvo = new UploadInfo();
				rsvo.setFileName(realfilename);
				rsvo.setFileSize(file.length());
				rsvo.setOriginalName(filename);

				// 处理相对路径
				String path = "/";
				if (StringUtils.isNotBlank(subfolder)) {
					path = path + subfolder.trim().replaceAll("\\.", "/") + "/";
				}

				path = path + DateUtil.format("yyyyMMdd", nowdate) + "/";

				if (path.indexOf("//") != -1)
					path = path.replace("//", "/");

				rsvo.setPath(path);
				rsvo.setStatus(uploadbegin);
				rsvo.setUrl(getResourceFileLink(path + realfilename)); 

				// 调用上传方法
				try {
					saveUpload(rsvo, file);
					
					rsvo.setStatus(uploadsuccess);
				} catch (Exception err) {
					rsvo.setStatus(uploadfail);
					throw new WebException(err);
				}

				return rsvo;
			} else {
				throw new WebException("临时文件不存在");
			}

		} catch (Exception allerr) {
			allerr.printStackTrace(System.out);
			throw new WebException(allerr);
		}

	}

	/**
	 * 上传文件,自动选择资源库类型
	 * 
	 * @param resinfoid
	 * @param tempFilePath
	 * @return
	 */
	private  boolean saveUpload(UploadInfo rsvo, File file) {

		String type = conf.getUploadType();

		if (type != null) {

			if ("ftp".equals(type.trim().toLowerCase())) {
				return saveFtpUpload(rsvo, file);
			} else if ("mount".equals(type.trim().toLowerCase())) {
				return saveMountUpload(rsvo, file);
			} else {
				throw new WebException("没有合适的资源库类型：" + type);
			}
		} else {
			throw new WebException("资源库类型出错：UploadType");
		}

	}

	/**
	 * @param resinfoid
	 * @param tempFilePath
	 * @return
	 */
	private  boolean saveFtpUpload(UploadInfo rsvo, File file){

		FTPClient ftp = new FTPClient();

		try {

			ftp.connect(conf.getFtpRemoteIP(), conf.getFtpRemotePort());
			if (!ftp.login(conf.getFtpUser(), conf.getFtpPassword())) {
				// FTP登录失败
				ftp.logout();
				return false;
			}

			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			ftp.enterLocalPassiveMode();

			String path = rsvo.getPath();
			
			if (conf.getRootPath() != null && !"".equals(conf.getRootPath())) {
				path = conf.getRootPath() + path;
			}

			String[] dirArray = path.split("/");
			if (dirArray.length > 1) {
				for (int i = 0; i < dirArray.length; i++) {

					if (dirArray[i] != null && !"".equals(dirArray[i])) {
						if (!ftp.changeWorkingDirectory(dirArray[i])) {
							ftp.makeDirectory(dirArray[i]);
							ftp.changeWorkingDirectory(dirArray[i]);
						}
					}

				}
			}
			// 上传文件
			InputStream input = new FileInputStream(file);
			
			ftp.storeFile(rsvo.getFileName(), input);
			input.close();
			ftp.logout();

			// 删除临时文件
			if (file.exists())
				file.delete();

			return true;

		} catch (Exception err) {
			err.printStackTrace(System.out);
			throw new WebException(err);
		} finally {

			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ferr) {
					ferr.printStackTrace(System.out);
				}
			}
		}
	}

	/**
	 * mount 硬盘路径或者同一台服务器，上传附件
	 * 
	 * @param resinfoid
	 * @param tempFilePath
	 * @return
	 */
	private  boolean saveMountUpload(UploadInfo rsvo, File file){
		try {

			if (file.exists()) {

				// 读取Mount的路径
				String mountPath = conf.getMountAddress();

				String path = mountPath + rsvo.getPath();
				
				// 创建文件夹
				File dir = new File(path);
				if (!dir.exists()) {
					mkdirs(dir);
				}

				String newfilename = path + rsvo.getFileName();

				File newfile = new File(newfilename);

				FileUtils.copyFile(file, newfile);

				// 删除临时文件
				if (file.exists())
					file.delete();

				try {
					// 20100712 oliver; 项目，因为nginx权限增加这段代码改文件权限
					Properties props = System.getProperties();
					String osname = props.getProperty("os.name");
					// logger.info("os.name="+osname);
					if (osname != null && (!"".equals(osname))) {
						osname = osname.toLowerCase();
						if (osname.indexOf("linux") != -1) {
							Runtime.getRuntime().exec(
									"chmod 0644 " + newfilename);
							logger.info("chmod 0644 " + newfilename
									+ " success");
						}
					}
				} catch (Exception ex) {
				    logger.error(ex);
				}

				return true;
			} else {
				// 临时文件不存在
				throw new WebException("临时文件不存在");
			}
		} catch (Exception err) {
			throw new WebException(err);
		}

	}

	
	/**
	 * 删除文件
	 * 
	 * @param path
	 * @return
	 */
	public  boolean deleteFile(String path)  {
		if (path == null || path.length() == 0) {
			return false;
		}

		String type = conf.getUploadType();

		if (type != null) {

			if ("ftp".equals(type.trim().toLowerCase())) {
				// ftp 删除
				return deleleFtpFile(path);
			} else if ("mount".equals(type.trim().toLowerCase())) {
				// mount 文件删除
				return deleteMountFile(path);

			} 
		}
		
		return false;
	}
	
	/**
	 * 删除FTP中的资源文件
	 * 
	 * @param resinfoid
	 */
	private boolean deleleFtpFile(String path) {
		FTPClient ftp = new FTPClient();
		try {
				ftp.connect(conf.getFtpRemoteIP(), conf.getFtpRemotePort());
				if (!ftp.login(conf.getFtpUser(), conf.getFtpPassword())) {
					// ftp登录失败
					ftp.logout();
					return false;
				}

				ftp.setFileType(FTP.BINARY_FILE_TYPE);
				ftp.enterLocalPassiveMode();

				ftp.deleteFile(path);
				ftp.logout();

				return true;
		} catch (Exception err) {
			err.printStackTrace(System.out);
		} finally {

			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ferr) {
					ferr.printStackTrace(System.out);
				}
			}
		}
		return false;
	}

	/**
	 * 删除Mount的文件
	 * 
	 * @param resinfoid
	 */
	private boolean deleteMountFile(String path) {

		// 查出资源信息
		try {
			String filename = conf.getMountAddress() + path;
			File file = new File(filename);
			if (file.exists()) {
				file.delete();
			}

			return true;
		} catch (Exception err) {
			err.printStackTrace(System.out);
		}

		return false;
	}
	
	
	/**
	 * 20101215 oliver 重写file.mkdirs的方法
	 * 
	 * @param file
	 * @return
	 */
	private  boolean mkdirs(File file) {
		if (file == null)
			return false;

		if (file.exists()) {
			return false;
		}

		if (mkdir(file)) {

		} else {

			File canonFile = null;
			try {

				canonFile = file.getCanonicalFile();
			} catch (IOException e) {
				return false;
			}

			mkdirs(canonFile.getParentFile());

			if (canonFile.getParentFile().exists()) {
				mkdir(canonFile);
			}
		}

		return true;
	}

	/**
	 * 20101215 oliver 重写了file.mkdir的方法，主要是为了修改linux文件夹属性
	 * 
	 * @param file
	 * @return
	 */
	private  boolean mkdir(File file) {
		if (file.mkdir()) {
			try {
				// 20101215 oliver; 因为nginx权限增加这段代码改文件夹权限
				Properties props = System.getProperties();
				String osname = props.getProperty("os.name");
				logger.info("mkdir path=" + file.getPath() + " ,os.name="
						+ osname);
				if (osname != null && (!"".equals(osname))) {
					osname = osname.toLowerCase();
					if (osname.indexOf("linux") != -1) {
						Runtime.getRuntime()
								.exec("chmod 755 " + file.getPath());
						logger.info("mkdir ,chmod 755 " + file.getPath()
								+ " success");
					}
				}
			} catch (Exception ex) {
				logger.error("midkr,error");
				logger.error(ex);
			}

			return true;

		} else {
			return false;
		}

	}
	
}
