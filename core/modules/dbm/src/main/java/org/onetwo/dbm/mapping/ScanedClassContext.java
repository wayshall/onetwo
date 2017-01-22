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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((metadataReader == null) ? 0 : metadataReader.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScanedClassContext other = (ScanedClassContext) obj;
		if (metadataReader == null) {
			if (other.metadataReader != null)
				return false;
		} else if (!metadataReader.equals(other.metadataReader))
			return false;
		return true;
	}
}
