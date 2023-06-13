package org.onetwo.boot.module.ffmpeg;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(FFmpegProperties.class)
public class FFmpegConfiguration {
	
	@Bean
	public FFmpegConfig ffmpegConfig() {
		return new FFmpegConfig();
	}
	
	@Bean
	public FFmpegVideoTranscoder ffmpegVideoTranscoder() {
		return new FFmpegVideoTranscoder();
	}
}
