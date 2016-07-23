package org.onetwo.boot.plugin.ftl;

import java.util.Date;

import org.onetwo.common.utils.JodatimeUtils;

@FreeMarkerViewTools
public class PluginHelperViewTools {

	public String formatDate(Date date, String pattern){
		return JodatimeUtils.format(date, pattern);
	}

}
