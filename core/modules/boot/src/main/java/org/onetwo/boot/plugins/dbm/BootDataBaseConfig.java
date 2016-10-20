package org.onetwo.boot.plugins.dbm;

import org.onetwo.dbm.mapping.DefaultDataBaseConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="dbm")
public class BootDataBaseConfig extends DefaultDataBaseConfig {

}
