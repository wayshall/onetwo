package org.onetwo.boot.core.web.service.impl;

import java.io.InputStream;

import org.onetwo.boot.core.web.utils.UploadOptions.ResizeConfig;
import org.onetwo.boot.core.web.utils.UploadOptions.SnapshotConfig;
import org.onetwo.boot.core.web.utils.UploadOptions.WaterMaskConfig;
import org.onetwo.common.file.StoringFileContext;

import lombok.Getter;
import lombok.Setter;

/**
 * @author weishao zeng
 * <br/>
 */
public class BootStoringFileContext extends StoringFileContext {
	
	@Setter
	@Getter
	private ResizeConfig resizeConfig;
	@Setter
	@Getter
	private WaterMaskConfig waterMaskConfig;
	@Setter
	@Getter
	private SnapshotConfig snapshotConfig;

	public BootStoringFileContext(String module, InputStream inputStream, String fileName) {
		super(module, inputStream, fileName);
	}

}

