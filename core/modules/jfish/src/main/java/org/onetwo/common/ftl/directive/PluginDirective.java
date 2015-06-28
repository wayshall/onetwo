package org.onetwo.common.ftl.directive;

import java.io.IOException;
import java.util.Map;

import org.onetwo.common.fish.plugin.JFishPlugin;
import org.onetwo.common.fish.plugin.PluginConfig;
import org.onetwo.common.web.view.ftl.AbstractDirective;
import org.onetwo.common.web.view.ftl.DirectivesUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@SuppressWarnings("rawtypes")
abstract public class PluginDirective extends AbstractDirective {

	private PluginConfig selfConfig;
	
	public PluginDirective(){
		selfConfig = getPlugin().getPluginMeta().getPluginConfig();
	}
	
	abstract protected JFishPlugin getPlugin();
	
	@Override
	public void render(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		DirectivesUtils.setVariable(env, "selfConfig", selfConfig);
		this.doInternalRender(env, params, loopVars, body);
	}
	
	public PluginConfig getSelfConfig() {
		return selfConfig;
	}
	

	public String getPluginTemplatePath(String template) {
		return getPlugin().getPluginMeta().getPluginConfig().getTemplatePath(template);
	}
	
	abstract public void doInternalRender(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException;

}
