package org.onetwo.boot.module.qrcode;

import org.onetwo.boot.core.web.service.BootCommonService;
import org.onetwo.boot.core.web.utils.SimpleMultipartFile;
import org.onetwo.boot.core.web.utils.UploadOptions;
import org.onetwo.common.file.FileStoredMeta;
import org.onetwo.common.utils.LangUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

public class QRCodeStorer {
	
	@Autowired
	private BootCommonService  bootCommonService;
	
	public FileStoredMeta upload(String module, String qrcodeContent, int qrcodeSize) {
		QRCodeCreator qrcodeCreator = new QRCodeCreator(qrcodeContent, qrcodeSize);
		return upload(module, qrcodeCreator);
	}
	
	public FileStoredMeta upload(String module, QRCodeCreator qrcodeCreator) {
		byte[] data = qrcodeCreator.toByteArray();
		String fileName = "qrc-"+LangUtils.randomUUID()+"."+qrcodeCreator.getFormat();
		MultipartFile multipartFile = new SimpleMultipartFile(fileName, data);
		FileStoredMeta meta = bootCommonService.uploadFile(UploadOptions.builder()
												.module(module)
												.multipartFile(multipartFile)
												.build());
		return meta;
	}

}
