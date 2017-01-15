package org.onetwo.common.web.tomcatmini;

import org.onetwo.common.web.tomcatmini.TomcatServer.TomcatServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TomcatTestServer {
    private static final Logger logger = LoggerFactory.getLogger(TomcatTestServer.class);

	public static void main(String[] args) {
		logger.info("start server...");
		TomcatServerBuilder.create(8080)
							.addWebapp("F:/resources/activiti-5.22.0/wars/activiti-explorer.war", "/activiti-explorer")
							.build()
							.start();
	}
}
