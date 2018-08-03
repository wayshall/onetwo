package org.onetwo.boot.plugins.swagger;

import org.onetwo.boot.plugin.core.PluginMeta;
import org.onetwo.boot.plugin.core.WebPluginAdapter;

/**
 * @author wayshall
 * <br/>
 */
public class SwaggerExtPlugin extends WebPluginAdapter {
    private final PluginMeta meta = PluginMeta.by(this.getClass());

    @Override
    public PluginMeta getPluginMeta() {
        return meta;
    }

}
