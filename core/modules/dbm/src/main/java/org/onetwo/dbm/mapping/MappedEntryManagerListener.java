package org.onetwo.dbm.mapping;

import java.util.Collection;

public interface MappedEntryManagerListener {

	public void beforeBuild(MappedEntryManager mappedEntryManager, Collection<ScanedClassContext> clssNameList);
	
}
