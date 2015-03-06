package org.onetwo.common.db;

import org.onetwo.common.utils.propconf.AbstractPropertiesManager.NamespaceProperty;

abstract public class AbstractFileNamedQueryFactory<T extends NamespaceProperty> implements FileNamedQueryFactory<T> {

	private FileNamedQueryFactoryListener fileNamedQueryFactoryListener;
	private QueryProvider createQueryable;

	public AbstractFileNamedQueryFactory(FileNamedQueryFactoryListener fileNamedQueryFactoryListener) {
		super();
		this.fileNamedQueryFactoryListener = fileNamedQueryFactoryListener;
	}


	public void initQeuryFactory(QueryProvider em){
		this.createQueryable = em;
		this.buildNamedQueryInfos();
		if(this.fileNamedQueryFactoryListener!=null)
			this.fileNamedQueryFactoryListener.onInitialized(em, this);
	}
	
	
	public QueryProvider getCreateQueryable() {
		return createQueryable;
	}


	abstract public void buildNamedQueryInfos();
	
}
