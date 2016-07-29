package org.onetwo.common.jfishdbm.jdbc.type;

import java.util.List;

public interface MappedTypeHandler<T> extends TypeHandler<T> {

	public List<Class<T>> getMappedJavaTypes();
	public List<Integer> getMappedSqlTypes();
}
