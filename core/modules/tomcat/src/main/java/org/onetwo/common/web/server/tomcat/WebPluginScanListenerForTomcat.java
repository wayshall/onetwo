package org.onetwo.common.web.server.tomcat;

import java.util.List;
import java.util.Properties;

import org.onetwo.common.exception.BaseException;
import org.onetwo.common.log.JFishLoggerFactory;
import org.onetwo.common.utils.ReflectUtils;
import org.onetwo.common.utils.StringUtils;
import org.onetwo.common.utils.propconf.JFishProperties;
import org.onetwo.common.utils.propconf.PropUtils;
import org.onetwo.common.web.server.event.WebappAddEvent;
import org.onetwo.common.web.server.listener.EmbeddedServerListener;
import org.slf4j.Logger;
import org.springframework.core.io.Resource;

import com.google.common.eventbus.Subscribe;

public class WebPluginScanListenerForTomcat implements EmbeddedServerListener{
	public static final String PLUGIN_WEBAPP_BASE_PATH = "classpath:META-INF";
	public static final String PLUGIN_PATH = "classpath*:META-INF/jfish-plugin.properties";


	private Logger logger = JFishLoggerFactory.logger(this.getClass());
	
	@Subscribe
	public void listen(WebappAddEvent<TomcatServer> event){
		scanPluginWebapps(event);
	}
	
	public void scanPluginWebapps(final WebappAddEvent<TomcatServer> event){
		List<Resource> pluginFiles =ResourceUtils.scanResources(PLUGIN_PATH);
//		final Tomcat tomcat = event.getTomcatServer().getTomcat();

		for(Resource pluginFile: pluginFiles){
			try {
	            this.processPluginResource(event, pluginFile);
            } catch (Exception e) {
	           throw new BaseException("scan plugin error.", e);
            }
		}
		/*pluginFiles.each(new NoIndexIt<Resource>(){

			@Override
			
			
		});*/

	}
	
	public void processPluginResource(final WebappAddEvent<TomcatServer> event, Resource pluginFile) throws Exception {
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
	
	protected PluginInfo buildPluginInfo(JFishProperties prop){
		PluginInfo info = new PluginInfo();
		info.init(prop);
		return info;
	}
}
