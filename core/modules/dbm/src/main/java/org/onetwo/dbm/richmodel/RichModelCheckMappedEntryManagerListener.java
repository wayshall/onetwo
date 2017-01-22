package org.onetwo.dbm.richmodel;

import java.util.Collection;

import org.onetwo.dbm.exception.DbmException;
import org.onetwo.dbm.mapping.MappedEntryManager;
import org.onetwo.dbm.mapping.MappedEntryManagerListener;
import org.onetwo.dbm.mapping.ScanedClassContext;

public class RichModelCheckMappedEntryManagerListener implements MappedEntryManagerListener {

	@Override
	public void beforeBuild(MappedEntryManager mappedEntryManager, Collection<ScanedClassContext> clssNameList) {
		for(ScanedClassContext cls : clssNameList){
			if(cls.isSubClassOf(RichModel.class.getName())){
				throw new DbmException("you must be add javassist to classpath if you want to use richmodel support!");
			}
		}
	}
	
	

}
