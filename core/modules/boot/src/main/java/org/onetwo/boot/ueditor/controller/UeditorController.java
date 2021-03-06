package org.onetwo.boot.ueditor.controller;

import javax.servlet.http.HttpServletResponse;

import org.onetwo.boot.core.web.controller.AbstractBaseController;
import org.onetwo.boot.core.web.mvc.interceptor.UploadFileValidator;
import org.onetwo.boot.core.web.service.BootCommonService;
import org.onetwo.boot.core.web.utils.PathTagResolver;
import org.onetwo.boot.core.web.utils.UploadOptions;
import org.onetwo.boot.ueditor.UeditorProperties;
import org.onetwo.boot.ueditor.UeditorProperties.UeditorConfig;
import org.onetwo.boot.ueditor.vo.UeditorUploadResponse;
import org.onetwo.common.file.FileStoredMeta;
import org.onetwo.common.jackson.JsonMapper;
import org.onetwo.common.web.utils.ResponseUtils;
import org.onetwo.ext.permission.api.annotation.ByPermissionClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author weishao zeng
 * <br/>
 */
//@RestController
@RequestMapping("${"+UeditorProperties.UEDITOR_UPLOAD_PATH + ":/web-admin/ueditor}")
public class UeditorController extends AbstractBaseController {
	@Autowired
	private UeditorProperties ueditorProperties;
	@Autowired
	private BootCommonService bootCommonService;
//	@Autowired
//	private BootSiteConfig bootSiteConfig;
	@Autowired
	private PathTagResolver pathTagResolver;
	
	@GetMapping(params="action=config")
	@ResponseBody
	@ByPermissionClass
	public UeditorConfig config() {
		UeditorConfig config = ueditorProperties.getConfig();
//		config.setImageUrlPrefix(bootSiteConfig.getImageServer().getBasePath()); 通过pathTagResolver解释地址，不再需要配置这个
		return config;
	}
	
	@PostMapping(params="action="+UeditorConfig.IMAGE_ACTION_NAME)
    @UploadFileValidator(allowedPostfix = {"jpg", "jpeg", "gif", "png", "bmp"}, maxUploadSize = 1024 * 1024 * 10)
	@ByPermissionClass
	public void uploadimage(MultipartFile upfile, HttpServletResponse response) {
//		WebHolder.getResponse().get().setHeader("Access-Control-Allow-Origin", "*");
		String key = null;
		UeditorConfig config = ueditorProperties.getConfig();
		if (config.isFormatPath()) {
			key = config.formatImagePath(upfile.getOriginalFilename());
		}
		FileStoredMeta meta = this.bootCommonService.uploadFile(UploadOptions.builder()
                													.compressConfig(ueditorProperties.isCompressEnabled()?ueditorProperties.getImageCompress():null)
													                .module(ueditorProperties.getBizModule())
													                .multipartFile(upfile)
													                .key(key)
													                .build());

		// 解释后的地址已经包含前缀，此时不需要配置 imageUrlPrefix 属性
		String path = pathTagResolver.parsePathTag(meta.getAccessablePath());
		
		UeditorUploadResponse res = UeditorUploadResponse.builder()
						.state("SUCCESS")
						.url(path)
						.original(meta.getOriginalFilename())
						.title(meta.getOriginalFilename())
						.build();
		String json = JsonMapper.IGNORE_EMPTY.toJson(res);
//		json = string2Unicode(json);
		ResponseUtils.renderHtml(response, json);
	}
	
}
