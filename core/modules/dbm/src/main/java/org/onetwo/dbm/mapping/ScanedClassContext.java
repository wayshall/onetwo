package org.onetwo.dbm.mapping;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.type.classreading.MetadataReader;

public class ScanedClassContext {
	
	private MetadataReader metadataReader;

	public ScanedClassContext(MetadataReader metadataReader) {
		super();
		this.metadataReader = metadataReader;
	}
	
	public String getClassName(){
		return metadataReader.getClassMetadata().getClassName();
	}
	
	public InputStream getInputStream() throws IOException{
		return this.metadataReader.getResource().getInputStream();
	}
	
	public String getSuperClassName(){
		return this.metadataReader.getClassMetadata().getSuperClassName();
	}
	
	public boolean isSubClassOf(String superClassName){
		return superClassName.equals(getSuperClassName());
	}
}
