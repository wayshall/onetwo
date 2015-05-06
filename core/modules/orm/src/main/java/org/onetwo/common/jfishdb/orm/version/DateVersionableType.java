package org.onetwo.common.jfishdb.orm.version;

import java.util.Date;

public class DateVersionableType implements VersionableType<Date> {

	@Override
	public Date getVersionValule(Date oldVersion) {
		return new Date();
	}

	@Override
	public boolean isEquals(Date newVersion, Date oldVersion) {
		return newVersion!=null && newVersion.equals(oldVersion);
	}

}
