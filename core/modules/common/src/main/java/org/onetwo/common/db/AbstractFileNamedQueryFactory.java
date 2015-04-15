package org.onetwo.common.db;

import java.util.List;

import org.onetwo.common.utils.LangUtils;
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

	@Override
	public <E> E findOne(String queryName, Object... params) {
		DataQuery jq = this.createQuery(queryName, params);
		E entity = null;
		List<E> list = jq.getResultList();
		if(LangUtils.hasElement(list))
			entity = list.get(0);
		return entity;
	}
	
	abstract public void buildNamedQueryInfos();
	
}
