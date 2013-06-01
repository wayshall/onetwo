package org.onetwo.plugins.richmodel;

import org.onetwo.common.fish.orm.TableInfo;
import org.onetwo.common.fish.relation.JFishRelatedEntryImpl;
import org.onetwo.common.utils.AnnotationInfo;

public class RichModelEntryImpl extends JFishRelatedEntryImpl {

	public RichModelEntryImpl(AnnotationInfo annotationInfo, TableInfo tableInfo) {
		super(annotationInfo, tableInfo);
	}

}
