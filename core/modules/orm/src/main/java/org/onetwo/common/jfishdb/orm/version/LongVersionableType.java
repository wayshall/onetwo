package org.onetwo.common.jfishdb.orm.version;

public class LongVersionableType implements VersionableType<Long> {

	@Override
	public Long getVersionValule(Long oldVersion) {
		if(oldVersion==null)
			return 1L;
		else
			return oldVersion + 1;
	}

	@Override
	public boolean isEquals(Long newVersion, Long oldVersion) {
		return newVersion!=null && newVersion.equals(oldVersion);
	}

}
