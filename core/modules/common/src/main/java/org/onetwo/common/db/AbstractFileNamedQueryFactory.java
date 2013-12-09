package org.onetwo.common.db;

import org.onetwo.common.utils.propconf.AbstractPropertiesManager.NamespaceProperty;

abstract public class AbstractFileNamedQueryFactory<T extends NamespaceProperty> implements FileNamedQueryFactory<T> {

	private FileNamedQueryFactoryListener fileNamedQueryFactoryListener;
	private CreateQueryable createQueryable;

	public AbstractFileNamedQueryFactory(FileNamedQueryFactoryListener fileNamedQueryFactoryListener) {
		super();
		this.fileNamedQueryFactoryListener = fileNamedQueryFactoryListener;
	}
	

	public void initQeuryFactory(CreateQueryable em){
		this.createQueryable = em;
		this.buildNamedQueryInfos();
		if(this.fileNamedQueryFactoryListener!=null)
			this.fileNamedQueryFactoryListener.onInitialized(em, this);
	}
	
	
	public CreateQueryable getCreateQueryable() {
		return createQueryable;
	}


	abstract public void buildNamedQueryInfos();
	
}
