package org.onetwo.boot.module.activemq;

import java.util.Properties;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wayshall
 * <br/>
 */
@ConfigurationProperties(value=ActivemqProperties.PREFIX_KEY)
@Data
public class ActivemqProperties {
	public static final String PREFIX_KEY = "jfish.activemq";
	public static final String ENABLE_KEY = PREFIX_KEY+".enabled";
	public static final String EMBEDDED_ENABLE_KEY = PREFIX_KEY+".embedded.enabled";
	
	Properties connectionFactory = new Properties();
	KahaDBStoreProps kahadbStore = new KahaDBStoreProps();
	JdbcStoreProps jdbcStore = new JdbcStoreProps();
	
	@Data
	static public class KahaDBStoreProps {
		public static final String ENABLE_KEY = "jfish.activemq.kahadbStore.enabled";
		String dataDir;
	}
	
	@Data
	static public class JdbcStoreProps {
		public static final String ENABLE_KEY = "jfish.activemq.jdbcStore.enabled";
		boolean createTablesOnStartup = true;
	}

}
