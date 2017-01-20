package org.onetwo.dbm.mapping;

import java.util.List;

public interface MappedEntryManagerListener {

	public void beforeBuild(MappedEntryManager mappedEntryManager, List<ScanedClassContext> clssNameList);
	
}
