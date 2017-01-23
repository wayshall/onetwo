package org.onetwo.dbm.richmodel;

import java.util.Collection;

import org.onetwo.dbm.mapping.ScanedClassContext;

public interface PackageScanedProcessor {
	public void processClasses(Collection<ScanedClassContext> clssNameList);
}