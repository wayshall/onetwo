package org.onetwo.boot.plugin.core;

import org.onetwo.boot.core.condition.EnabledKeyCondition;
import org.onetwo.common.reflect.ReflectUtils;
import org.onetwo.common.spring.SpringUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author wayshall
 * <br/>
 */
public class ByPluginNameEanbledCondition extends EnabledKeyCondition {

	private WebPlugin webPlugin;
	final private String prefix = "";// "jfish.";
	
	public ByPluginNameEanbledCondition() {
		super();
		this.setDefaultEnabledValue(false);
	}


	@Override
	protected String getEnabledKey(Environment environment, AnnotationAttributes attrubutes) {
		PluginMeta meta = parsePlugin(attrubutes).getPluginMeta();
		String property = attrubutes.getString("property");
		// prefix+jfish.swagger.pluginName.enabled
		String key = prefix+property+"."+meta.getName()+".enabled";
		return key;
	}
	
	protected AnnotationAttributes getAnnotationAttributes(AnnotatedTypeMetadata metadata){
		//support @AliasFor
		AnnotationAttributes attributes = SpringUtils.getAnnotationAttributes(metadata, EnabledByPluginNameProperty.class);
		return attributes;
	}


	private WebPlugin parsePlugin(AnnotationAttributes attributes){
		WebPlugin webPlugin = this.webPlugin;
		if(webPlugin==null){
			Class<? extends WebPlugin> pluginClass = attributes.getClass("pluginClass");
			/*if(pluginClass==WebPlugin.class){
				throw new BaseException("you must be set your plugin class to annotation @"+EnabledByPluginNameProperty.class.getSimpleName());
			}*/
			webPlugin = ReflectUtils.newInstance(pluginClass);
			this.webPlugin = webPlugin;
		}
		return webPlugin;
	}

}
