package org.onetwo.boot.module.ffmpeg;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;

public class FFmpegConfig implements InitializingBean {
	@Autowired
	private FFmpegProperties properties;

	private FFmpeg ffmpeg;
	private FFprobe ffprobe;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		String ffmpegPath = properties.getFfmpegPath();
		String ffprobePath = properties.getFfprobePath();
		ffmpeg = new FFmpeg(ffmpegPath);
		ffprobe = new FFprobe(ffprobePath);
	}
	
	public FFmpegJob createJob(FFmpegBuilder builder) {
		FFmpegJob job = createExecutor().createJob(builder);
		return job;
	}

	
	public FFmpegJob createTwoPassJob(FFmpegBuilder builder) {
		FFmpegJob job = createExecutor().createTwoPassJob(builder);
		return job;
	}
	
	public FFmpegExecutor createExecutor() {
		FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
		return executor;
	}

}
