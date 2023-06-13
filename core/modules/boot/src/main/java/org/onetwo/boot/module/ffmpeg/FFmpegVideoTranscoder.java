package org.onetwo.boot.module.ffmpeg;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.common.date.DateUtils;
import org.onetwo.common.date.NiceDate;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.file.SimpleFileStoredMeta;
import org.onetwo.common.file.StoredMeta;
import org.springframework.beans.factory.annotation.Autowired;

import net.bramp.ffmpeg.builder.FFmpegBuilder;

public class FFmpegVideoTranscoder {
	
	@Autowired
	private FFmpegConfig fFmpegConfig;
	
	public StoredMeta toMP4(String inputPath, String baseDir) {
		String subDir = NiceDate.New().format(DateUtils.DATEONLY);
		String outputDir = FileUtils.convertDir(baseDir) + subDir;
		String ts = NiceDate.New().format(DateUtils.DATEMILLS);
		String fileName = FileUtils.getFileNameWithoutExt(inputPath) + "_" + ts + ".mp4";
		String accessablePath = "/" + subDir + "/" + fileName;
		String storePath = toMP4(inputPath, outputDir, fileName);
		SimpleFileStoredMeta meta = new SimpleFileStoredMeta(fileName, storePath);
		meta.setAccessablePath(accessablePath);
		return meta;
	}
	
	public String toMP4(String inputPath, String outputDir, String fileName) {
		if (StringUtils.isBlank(inputPath)) {
			throw new IllegalArgumentException("inputPath can not be blank");
		}
		if (StringUtils.isBlank(outputDir)) {
			throw new IllegalArgumentException("outputDir can not be blank");
		}
		if (StringUtils.isBlank(fileName)) {
			throw new IllegalArgumentException("fileName can not be blank");
		}
		FileUtils.makeDirs(outputDir, false);
		String ouputPath = FileUtils.convertDir(outputDir) + fileName;
		FFmpegBuilder builder = new FFmpegBuilder();
		builder.setInput(inputPath)
				.overrideOutputFiles(true)
				.addOutput(ouputPath)
				   .setFormat("mp4");
		fFmpegConfig.createJob(builder).run();;
		return ouputPath;
	}

}
