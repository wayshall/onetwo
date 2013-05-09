package org.onetwo.plugins.fmtagext.directive;

import org.onetwo.common.fish.plugin.JFishPlugin;
import org.onetwo.plugins.fmtag.directive.FmtagBaseDirective;
import org.onetwo.plugins.fmtagext.FmtagextPlugin;

abstract public class FmtagextDirective extends FmtagBaseDirective {

	@Override
	protected JFishPlugin getPlugin() {
		return FmtagextPlugin.getInstance();
	}


}
