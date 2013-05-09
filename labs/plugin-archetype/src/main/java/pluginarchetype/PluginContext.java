package pluginarchetype;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan(basePackageClasses=Plugin.class)
public class PluginContext {
	
	
}
