package org.onetwo.common.jfishdb.orm.version;

public interface VersionableType<T> {
	public T getVersionValule(T oldVersion);
	public boolean isEquals(T newVersion, T oldVersion);
}
