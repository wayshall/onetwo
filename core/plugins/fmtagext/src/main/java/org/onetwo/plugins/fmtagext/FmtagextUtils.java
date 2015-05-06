package org.onetwo.plugins.fmtagext;

import org.onetwo.common.jfishdb.orm.JFishMappedField;
import org.onetwo.plugins.fmtag.directive.AbstractJFieldView;
import org.onetwo.plugins.fmtagext.annotation.JFieldView;

final public class FmtagextUtils {

	public static void setJFieldViewMeta(AbstractJFieldView jf, JFishMappedField field){
		jf.setLabel(field.getLabel());
		JFieldView jfm = field.getPropertyInfo().getAnnotation(JFieldView.class);
		if(jfm!=null){
			jf.setLabel(jfm.label());
			jf.setShowOrder(jfm.order());
			jf.setFormat(jfm.format());
			jf.setShowable(jfm.showable());
		}
	}
	
	private FmtagextUtils(){};
}
