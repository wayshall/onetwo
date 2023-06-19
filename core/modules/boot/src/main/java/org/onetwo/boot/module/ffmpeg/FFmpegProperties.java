package org.onetwo.boot.module.ffmpeg;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.boot.core.config.BootJFishConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(FFmpegProperties.PREFIX)
@Data
public class FFmpegProperties {
	/***
	 * jfish.ffmpeg
	 */
	public static final String PREFIX = BootJFishConfig.ZIFISH_CONFIG_PREFIX + ".ffmpeg";
	
	/***
	 * jfish.ffmpeg.ffmpegPath
	 */
	String ffmpegPath;
	String ffprobePath;
	boolean checkFFmpegPath = true;
	
	@PostConstruct
	public void check() {
		if (checkFFmpegPath) {
			if (StringUtils.isBlank(ffmpegPath)) {
				throw new IllegalArgumentException(PREFIX + ".ffmpegPath not found on properties");
			}
			if (StringUtils.isBlank(ffprobePath)) {
				throw new IllegalArgumentException(PREFIX + ".ffprobePath not found on properties");
			}
		}
	}
	
}
