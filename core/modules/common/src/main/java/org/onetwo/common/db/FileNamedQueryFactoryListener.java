package org.onetwo.common.db;

import org.onetwo.common.db.filequery.NamespaceProperty;



public interface FileNamedQueryFactoryListener {

	public void onInitialized(QueryProvideManager em, FileNamedQueryFactory<? extends NamespaceProperty> fq);
}
