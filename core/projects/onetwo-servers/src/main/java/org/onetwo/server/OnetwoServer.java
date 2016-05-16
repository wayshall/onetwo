package org.onetwo.server;

import org.onetwo.common.web.tomcatmini.ServerConfig;
import org.onetwo.common.web.tomcatmini.TomcatServer;

//import org.onetwo.common.web.tomcatmini.ServerConfig;
//import org.onetwo.common.web.tomcatmini.TomcatServer;

public class OnetwoServer {

	public static void main(String[] args) {
		ServerConfig config = new ServerConfig();
		config.addWebapp("D:/mydev/java/yooyo-workspace/v5/emall-manager-frame/src/main/webapp", "/emall-manager-frame");
		config.addWebapp("D:/mydev/java/yooyo-workspace/v5/emall-product-manager/src/main/webapp", "/emall-product-manager");
		TomcatServer.create(config).start();
	}
}
