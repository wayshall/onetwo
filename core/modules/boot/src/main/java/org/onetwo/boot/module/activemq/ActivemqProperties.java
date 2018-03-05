package org.onetwo.boot.module.activemq;

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
	public static final String ENABLE_KEY = "jfish.activemq.enabled";
	
	KahaDBStoreProps kahadbStore = new KahaDBStoreProps();
	JdbcStoreProps jdbcStore = new JdbcStoreProps();
	
	@Data
	static public class KahaDBStoreProps {
		public static final String STORE_KEY = "jfish.activemq.kahadbStore.enabled";
		String dataDir;
	}
	
	@Data
	static public class JdbcStoreProps {
		public static final String STORE_KEY = "jfish.activemq.jdbcStore.enabled";
		boolean createTablesOnStartup = true;
	}

}
