package org.onetwo.boot.core.web.upload;

import lombok.Data;

import org.onetwo.boot.core.web.upload.HeaderBaseDirStrategyConfiguration.HeaderStrategyProperties;
import org.onetwo.boot.core.web.utils.BootWebUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.file.FileStoredMeta;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.file.SimpleFileStorer.SimpleStoreFilePathStrategy;
import org.onetwo.common.file.StoringFileContext;
import org.onetwo.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

/**
 * @author wayshall
 * <br/>
 */
@Configuration
@ConditionalOnProperty(HeaderStrategyProperties.ENABLED_KEY)
@EnableConfigurationProperties(HeaderStrategyProperties.class)
public class HeaderBaseDirStrategyConfiguration {
	
	@Autowired
	private HeaderStrategyProperties headerStrategyProperties;
	
	@Bean
	public HeaderBaseDirStrategy headerBaseDirStrategy(){
		Assert.hasText(headerStrategyProperties.getHeaderName());
		HeaderBaseDirStrategy strategy = new HeaderBaseDirStrategy();
		strategy.headerName = headerStrategyProperties.getHeaderName();
		return strategy;
	}

	static public class HeaderBaseDirStrategy extends SimpleStoreFilePathStrategy {
		String headerName;

		@Override
		public FileStoredMeta getStoreFilePath(final String storeBaseDir, String appContextDir, StoringFileContext ctx) {
			String subPath = getSubPathFromHeader();
			String newStoreBaseDir = storeBaseDir + FileUtils.convertDir(subPath);
			return super.getStoreFilePath(newStoreBaseDir, appContextDir, ctx);
		}
		
		public String getSubPathFromHeader(){
			String subPath =  BootWebUtils.getHeader(headerName, "");
			if(StringUtils.isBlank(subPath)){
				throw new BaseException("no subPath found!");
			}
			return subPath;
		}
	}
	
	@ConfigurationProperties(HeaderStrategyProperties.PREFIX)
	@Data
	public static class HeaderStrategyProperties {
		public static final String PREFIX = "site.upload.headerStrategy";
		public static final String ENABLED_KEY = PREFIX + ".enabled";
		String headerName;
	}
}
