package org.onetwo.boot.webmgr;

import org.onetwo.boot.plugin.core.PluginMeta;
import org.onetwo.boot.plugin.core.WebPluginAdapter;

/**
 * @author wayshall
 * <br/>
 */
public class ZifishWebPlugin extends WebPluginAdapter {
    private final PluginMeta meta = PluginMeta.by(this.getClass());
    
    public ZifishWebPlugin(){
    }

    @Override
    public PluginMeta getPluginMeta() {
        return meta;
    }

}
