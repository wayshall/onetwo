package org.onetwo.dbm.mapping.version;

public interface VersionableType<T> {
	public T getVersionValule(T oldVersion);
	public boolean isEquals(T newVersion, T oldVersion);
}
