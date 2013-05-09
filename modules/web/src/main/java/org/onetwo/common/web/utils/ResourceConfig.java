package org.onetwo.common.web.utils;

import org.onetwo.common.web.config.BaseSiteConfig;


/**
 * 资源库，上传文件配置获取类
 * @author oliver
 *
 */
public class ResourceConfig {

	
	/**
	 * 获取上传类型
	 * ftp or mount
	 * @return
	 */
	public String getUploadType() {
		BaseSiteConfig.getInstance();
		return BaseSiteConfig.getConfig("rs.uploadtype");
	}
	
	/**
	 * 获取资源域名URL
	 * @return
	 */
	public String getRsDomain() {
		return BaseSiteConfig.getConfig("rs.domain");
	}
	
	/**
	 * 获取FTP地址
	 * @return
	 */
	public String getFtpRemoteIP() {
		return BaseSiteConfig.getConfig("rs.ftpremoteip");
	}
	
	/**
	 * 获取FTP端口
	 * @return
	 */
	public int getFtpRemotePort() {
		String prostr = BaseSiteConfig.getConfig("rs.ftpremoteport");
		if(prostr!=null&&!"".equals(prostr))
		{
		  return Integer.parseInt(prostr);
		}else{
			return 21 ;
		}
		 
	}
	
	
	/**
	 * 获取FTP相对目录
	 * @return
	 */
	public String getRootPath() {
		return BaseSiteConfig.getConfig("rs.ftprootpath");
	}
	
	/**
	 * 获取FTP用户
	 * @return
	 */
	public String getFtpUser() {
		return BaseSiteConfig.getConfig("rs.ftpuser");
	}
	
	/**
	 * 获取FTP密码
	 * @return
	 */
	public String getFtpPassword() {
		return BaseSiteConfig.getConfig("rs.ftppassword");
	}
	
	/**
	 * 获取共享的目录
	 * @return
	 */
	public String getMountAddress() {
		return BaseSiteConfig.getConfig("rs.mountaddress");
	}
	
	/**
	 * 获取默认图片
	 * @return
	 */
	public String getDefaultpic(){
		return BaseSiteConfig.getConfig("rs.defaultpic");
	}
	
}
