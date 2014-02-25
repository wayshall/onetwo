package org.onetwo.plugins.jdoc;

import java.io.File;
import java.util.List;

import org.onetwo.common.spring.utils.JFishResourcesScanner;
import org.onetwo.common.spring.utils.ScanResourcesCallback;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.plugins.jdoc.Lexer.JSourceParser;
import org.onetwo.plugins.jdoc.Lexer.JavaDefinedMapper;
import org.onetwo.plugins.jdoc.Lexer.JavaDefinedMapperImpl;
import org.onetwo.plugins.jdoc.Lexer.defined.JavaClassDefineImpl;
import org.onetwo.plugins.jdoc.Lexer.parser.JavaSourceParser;
import org.onetwo.plugins.jdoc.data.JClassDoc;
import org.springframework.core.io.Resource;
import org.springframework.core.type.classreading.MetadataReader;

public class JavaSourceScanner {

	private JFishResourcesScanner scaner = new JFishResourcesScanner();
	private String sourceDir;
	private JavaDefinedMapper<?> mapper = new JavaDefinedMapperImpl();
	
	public JavaSourceScanner(){
	}
	
	protected String getSourceFilePath(String clsName){
		String path = getSourceDir() + clsName.replace('.', '/')+".java";
		return path;
	}
	 
	public String getSourceDir() {
		return StringUtils.appendEndWith(sourceDir, "/");
	}

	public void setSourceDir(String sourceDir) {
		this.sourceDir = sourceDir;
	}
	
	public boolean isDocClass(MetadataReader metadataReader) {
		String clsName = metadataReader.getClassMetadata().getClassName().toLowerCase();
		return clsName.endsWith("controller") || clsName.endsWith("data") || clsName.endsWith("params") || clsName.endsWith("result");
	}

	public List<JClassDoc> scan(String... packagesToScan){
		List<JClassDoc> jclassDoc = this.scaner.scan(new ScanResourcesCallback<JClassDoc>() {

			/*@Override
			public boolean isCandidate(MetadataReader metadataReader) {
				return isDocClass(metadataReader);
			}*/

			@Override
			public JClassDoc doWithCandidate(final MetadataReader metadataReader, Resource resource, int count) {
				if(isDocClass(metadataReader))
					return null;
				String clsName = metadataReader.getClassMetadata().getClassName();
				
				String source = getSourceFilePath(clsName);
				File sourceFile = new File(source);
				JSourceParser parser = new JavaSourceParser(sourceFile);
				JavaClassDefineImpl classDefined = (JavaClassDefineImpl)parser.parse(null);
				JClassDoc jcdoc = mapDoc(classDefined);
				return jcdoc;
			}
			
		}, packagesToScan);
		
		return jclassDoc;
	}
	
	public JClassDoc mapDoc(JavaClassDefineImpl classDefined){
		return (JClassDoc)this.mapper.map(classDefined);
	}

	public JavaDefinedMapper<?> getMapper() {
		return mapper;
	}

	public void setMapper(JavaDefinedMapper<?> mapper) {
		this.mapper = mapper;
	}
	
}
