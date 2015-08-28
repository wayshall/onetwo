package org.onetwo.common.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.slf4j.Logger;

public class FileScanner {
	protected final Logger logger = MyLoggerFactory.getLogger(this.getClass());

//	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
//	private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();
	private String resourcePattern;
	
	public FileScanner(){}
	
	public FileScanner(String resourcePattern) {
		super();
		this.resourcePattern = resourcePattern;
	}
	
	protected List<File> scanFiles(String path){
		List<File> files = FileUtils.listFile(path, resourcePattern);
		return files;
	}

	public <T> List<T> scan(FileCallback<T> filter, String... pathsToScan) {
		Assert.notNull(filter);
		List<T> classesToBound = new ArrayList<T>();
		try {
			int count = 0;
			for (String path : pathsToScan) {
				if(!path.endsWith("/"))
					path += "/";
				List<File> resources = this.scanFiles(path);
				if (LangUtils.isEmpty(resources))
					continue;
				for (File resource : resources) {
//					System.out.println("fond file: " + resource.getPath());
					T obj = filter.doWithCandidate(resource, count++);
					if(obj!=null){
						classesToBound.add(obj);
					}
				}
			}
		} catch (Exception e) {
			throw new BaseException("scan resource in[" + LangUtils.toString(pathsToScan) + "] error : " + e.getMessage(), e);
		}
		return classesToBound;
	}

	public void setResourcePattern(String resourcePattern) {
		this.resourcePattern = resourcePattern;
	}

}
