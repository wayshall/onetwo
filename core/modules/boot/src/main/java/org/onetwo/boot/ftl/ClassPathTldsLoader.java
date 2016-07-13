package org.onetwo.boot.ftl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.ArrayUtils;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.utils.ResourceUtils;
import org.onetwo.common.utils.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

public class ClassPathTldsLoader  {

//	private static final String SECURITY_TLD = "/META-INF/security.tld";
	private static final String META_INF_PATH = "/META-INF/";
	private static final String TLDS_PATH = "classpath*:/META-INF/**/*.tld";
	
	private final Logger logger = JFishLoggerFactory.getLogger(this.getClass());
	
	final private List<String> classPathTlds;

	public ClassPathTldsLoader(String... classPathTlds) {
	    super();
	    if(ArrayUtils.isEmpty(classPathTlds)){
	    	this.classPathTlds = new ArrayList<String>();
	    	List<Resource> resources = ResourceUtils.scanResources(TLDS_PATH);
	    	resources.stream().forEach(res->{
	    		getPath(res).ifPresent(path->this.classPathTlds.add(path));
	    	});
	    }else{
	    	this.classPathTlds = Arrays.asList(classPathTlds);
	    }
    }

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@PostConstruct
    public void loadClassPathTlds() {
		freeMarkerConfigurer.getTaglibFactory().setClasspathTlds(classPathTlds);
    }
	
	protected Optional<String> getPath(Resource res){
		try {
			String path = res.getURL().getPath();
			if(StringUtils.isBlank(path))
				return Optional.empty();
			int startIndex = path.lastIndexOf(META_INF_PATH);
			if(startIndex!=-1){
				path = path.substring(startIndex);
			}
			logger.info("found tld : {}", path);
			return Optional.of(path);
		} catch (IOException e) {
			throw new BaseException("get resource path error");
		}
	}


}
