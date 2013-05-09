package org.onetwo.plugins.jdoc;

import java.util.List;

import org.onetwo.common.log.MyLoggerFactory;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.file.FileScanner;
import org.onetwo.common.utils.xml.XmlUtils;
import org.onetwo.plugins.jdoc.Lexer.JavadocManager;
import org.onetwo.plugins.jdoc.data.JClassDoc;
import org.onetwo.plugins.jdoc.utils.JDocPluginUtils;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;


@Configuration
@ComponentScan(basePackageClasses=JDocPlugin.class)
public class JDocContext {

	protected static final String DEFAULT_RESOURCE_PATTERN = ".+(Controller|Params|Data|Result)\\.java";
	
	private static final Logger logger = MyLoggerFactory.getLogger(JDocContext.class);
	
	@Bean
	public FileScanner docScanner(){
		FileScanner scaner = new FileScanner(DEFAULT_RESOURCE_PATTERN);
		return scaner;
	}

	/*@Bean
	public JavaDocReader javaDocReader(){
		JavaDocReader reader = new JavaDocReader();
		return reader;
	}*/
	
	@Bean
	public JavadocManager javadocManager(){
		JavadocManager jdoc = new JavadocManager();
		try {
			Resource jdocXml = SpringUtils.classpath("jdoc.xml");
			if(jdocXml.exists()){
				List<JClassDoc> jclassDocs = XmlUtils.toBeanFrom(jdocXml.getFile());
				jdoc.setJClassDoc(jclassDocs);
			}else{
				String pathToScan = JDocPluginUtils.getProejctSrcDir();
				jdoc.setDocScaner(docScanner());
				jdoc.setPathToScan(pathToScan);
				jdoc.startScanDoc();
			}
		} catch (Exception e) {
			logger.error("create JavadocManager error : " + e.getMessage(), e);
		}
		return jdoc;
	}
	
}
