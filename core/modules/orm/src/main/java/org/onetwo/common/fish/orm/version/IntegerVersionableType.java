package org.onetwo.common.fish.orm.version;

public class IntegerVersionableType implements VersionableType<Integer> {

	@Override
	public Integer getVersionValule(Integer oldVersion) {
		if(oldVersion==null)
			return 1;
		else
			return oldVersion + 1;
	}

	@Override
	public boolean isEquals(Integer newVersion, Integer oldVersion) {
		return newVersion!=null && newVersion.equals(oldVersion);
	}

}
