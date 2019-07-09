package org.onetwo.boot.compress;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.boot.utils.ImageCompressor;
import org.onetwo.boot.utils.ImageCompressor.ImageCompressorConfig;
import org.onetwo.common.file.FileUtils;
import org.onetwo.common.utils.LangOps;
import org.onetwo.common.utils.LangUtils;
import org.springframework.util.AntPathMatcher;

import lombok.Setter;

/**
 * @author weishao zeng
 * <br/>
 */
public class ImageBatchCompressor {
	
	private ImageCompressor imageCompressor;
	private String baseDir;
	private AntPathMatcher antMatcher;
	@Setter
	private ImageCompressorConfig compressorConfig;
	private String[] antPatterns;
	private String compressedFilePostfix = "_min";
	@Setter
	private String compressedDir;
	@Setter
	private String compressThresholdSize;
	private int compressThresholdSizeInBytes;
	
	public void initialize() {
		if (LangUtils.isEmpty(antPatterns)) {
			throw new IllegalArgumentException("antPatterns can not be empty!");
		}
		if (compressorConfig==null) {
			compressorConfig = ImageCompressorConfig.builder().scale(0.3).quality(0.3).build(); 
		}
		ImageCompressor compressor = new ImageCompressor();
		compressor.setConfig(compressorConfig);
		this.imageCompressor = compressor;
		this.antMatcher = new AntPathMatcher();
		this.compressThresholdSizeInBytes = LangOps.parseSize(compressThresholdSize, -1);
	}
	
	public List<String> compress() {
		Stream<File> stream = findMatchFiles().stream().filter(file -> {
			if (compressThresholdSizeInBytes==-1) {
				return true;
			}
			return FileUtils.size(file) > this.compressThresholdSizeInBytes;
		});
		if (StringUtils.isNotBlank(compressedDir)) {
			String dir = FileUtils.convertDir(compressedDir);
			FileUtils.makeDirs(dir, false);
			return stream.map(file -> {
				String fullpath = FileUtils.replaceBackSlashToSlash(file.getPath());
				String fileExt = FileUtils.getExtendName(fullpath, true);
				String filename = dir + FileUtils.getFileNameWithoutExt(fullpath);
				String compressed = imageCompressor.compressTo(fullpath, filename + compressedFilePostfix + fileExt);
				return compressed;
			}).collect(Collectors.toList());
		} else {
			return stream.map(file -> {
				String fullpath = FileUtils.replaceBackSlashToSlash(file.getPath());
				String fileExt = FileUtils.getExtendName(fullpath, true);
				String filename = StringUtils.substringBefore(fullpath, fileExt);
				String compressed = imageCompressor.compressTo(fullpath, filename + compressedFilePostfix + fileExt);
				return compressed;
			}).collect(Collectors.toList());
		}
		
	}
	
	protected List<File> findMatchFiles() {
		String dir = FileUtils.convertDir(baseDir);
		File dirFile = new File(dir);
		return FileUtils.list(dirFile, file -> {
			String fullpath = FileUtils.replaceBackSlashToSlash(file.getPath());
			String subpath = StringUtils.substringAfter(fullpath, dir); 
			return Stream.of(antPatterns).anyMatch(pattern -> antMatcher.match(pattern, subpath));
		}, true);
	}
	
	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}
	public void setAntPattern(String...antPatterns) {
		this.antPatterns = antPatterns;
	}

}

