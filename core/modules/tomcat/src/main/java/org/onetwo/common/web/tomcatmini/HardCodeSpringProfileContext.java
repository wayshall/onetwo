package org.onetwo.common.web.tomcatmini;

import org.onetwo.common.web.tomcatmini.TomcatServer.TomcatServerBuilder;

public class HardCodeSpringProfileContext extends HackServletContextStandardContext {
	public HardCodeSpringProfileContext() {
		TomcatServerBuilder.getInitParametersMapper().entrySet().forEach(e->this.mapInitParameter(e.getKey(), e.getValue()));
	}
}
