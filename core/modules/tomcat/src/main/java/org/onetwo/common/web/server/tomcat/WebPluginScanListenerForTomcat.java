package org.onetwo.common.web.server.tomcat;

import java.util.Properties;

import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.spring.plugin.PluginInfo;
import org.onetwo.common.spring.plugin.SpringContextPluginManager;
import org.onetwo.common.spring.utils.ResourceUtils;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.list.JFishList;
import org.onetwo.common.utils.list.NoIndexIt;
import org.onetwo.common.utils.propconf.JFishProperties;
import org.onetwo.common.utils.propconf.PropUtils;
import org.onetwo.common.web.server.event.WebappAddEvent;
import org.onetwo.common.web.server.listener.EmbeddedServerListener;
import org.slf4j.Logger;
import org.springframework.core.io.Resource;

import com.google.common.eventbus.Subscribe;

public class WebPluginScanListenerForTomcat implements EmbeddedServerListener{
	public static final String PLUGIN_WEBAPP_BASE_PATH = "classpath:META-INF";

	private Logger logger = JFishLoggerFactory.logger(this.getClass());
	
	@Subscribe
	public void listen(WebappAddEvent<TomcatServer> event){
		scanPluginWebapps(event);
	}
	
	public void scanPluginWebapps(final WebappAddEvent<TomcatServer> event){
		JFishList<Resource> pluginFiles =ResourceUtils.scanResources(SpringContextPluginManager.PLUGIN_PATH);
//		final Tomcat tomcat = event.getTomcatServer().getTomcat();

		pluginFiles.each(new NoIndexIt<Resource>(){

			@Override
			public void doIt(Resource pluginFile) throws Exception {
				Properties vconfig = new Properties();
				PropUtils.loadProperties(pluginFile.getInputStream(), vconfig);
				JFishProperties prop = new JFishProperties(true, vconfig);
				PluginInfo plugin = buildPluginInfo(prop);
				if(!plugin.isWebappPlugin()){
					return;
				}
				//plugin-monitor
				String webappPath = PLUGIN_WEBAPP_BASE_PATH + plugin.getContextPath();
				Resource res = ResourceUtils.getResource(webappPath);
				logger.info("found web plugin["+plugin+"] : {}", res.getURI().getPath() );
				event.addWebapp(plugin.getContextPath(), res.getURI().getPath());
				logger.info("load web plugin : {} ", res.getURI().getPath());
				
				String listener = plugin.getWebappPluginServerListener();
				if(StringUtils.isNotBlank(listener)){
					EmbeddedServerListener lisnter = ReflectUtils.newInstance(listener);
					event.getEventSource().registerListener(lisnter);
				}
			}
			
		});

	}
	protected PluginInfo buildPluginInfo(JFishProperties prop){
		PluginInfo info = new PluginInfo();
		info.init(prop);
		return info;
	}
}
