package org.onetwo.plugins.fmtagext.directive;

import org.onetwo.common.jfishdbm.mapping.JFishMappedEntry;
import org.onetwo.common.jfishdbm.mapping.JFishMappedField;
import org.onetwo.plugins.fmtag.annotation.JEntryViewMeta;
import org.onetwo.plugins.fmtag.directive.DataField;
import org.onetwo.plugins.fmtag.directive.DataGrid;
import org.onetwo.plugins.fmtagext.FmtagextUtils;

public class ExtEntityGridDirective extends EntityGridDirective {

	protected void doBeforeBuildEntryField(JFishMappedEntry entry, DataGrid dg){
		JEntryViewMeta jevm = entry.getAnnotationInfo().getAnnotation(JEntryViewMeta.class);
		if(jevm!=null){
			dg.setLabel(jevm.label());
		}
	}
	

	protected DataField createDataField(JFishMappedField field){
		DataField df =  new DataField(field.getName(), field.getLabel());
		FmtagextUtils.setJFieldViewMeta(df, field);
		return df;
	}
}
