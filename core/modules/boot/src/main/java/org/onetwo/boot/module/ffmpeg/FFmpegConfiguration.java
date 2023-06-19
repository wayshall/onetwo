package org.onetwo.boot.module.ffmpeg;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(FFmpegProperties.class)
@ConditionalOnClass(net.bramp.ffmpeg.FFmpeg.class)
@ConditionalOnProperty(name = FFmpegProperties.ENABLED_KEY, matchIfMissing = true)
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
