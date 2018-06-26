package org.onetwo.plugins.admin.controller;

import org.onetwo.boot.core.config.BootSiteConfig;
import org.onetwo.boot.core.web.service.BootCommonService;
import org.onetwo.common.exception.ServiceException;
import org.onetwo.common.file.FileStoredMeta;
import org.onetwo.common.web.utils.RequestUtils;
import org.onetwo.plugins.admin.utils.WebConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.ImmutableMap;

/**
 * @author wayshall
 * <br/>
 */
@RequestMapping(value="kindeditor", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
public class KindeditorController extends WebAdminBaseController {
	
	@Autowired
	private BootCommonService bootCommonService;
	@Autowired
	private BootSiteConfig siteConfig;
	
	@PostMapping(value="upload")
	@ResponseBody
	public Object upload(MultipartFile imgFile, String dir){
		if(imgFile==null || imgFile.isEmpty()){
			throw new ServiceException("上传文件不能为空！");
		}
		FileStoredMeta meta = bootCommonService.uploadFile(WebConstant.UPLOAD_MODULE_KINDEDITOR, imgFile);
		/*UploadOptions options = UploadOptions.builder()
					.module(WebConstant.UPLOAD_MODULE_KINDEDITOR)
					.multipartFile(imgFile)
					.compressConfig(CompressConfig.builder()
													.width(400)
													.build())
					.build();
		FileStoredMeta meta = bootCommonService.uploadFile(options);*/
		String url = null;
		if(RequestUtils.isHttpPath(meta.getAccessablePath())){
			url = meta.getAccessablePath();
		}else{
			url = siteConfig.getKindeditor().getImageBasePath() + meta.getAccessablePath();
		}
		return ImmutableMap.of("error", 0, "url", url);
	}

}
