package org.onetwo.boot.module.activemq;

import java.io.File;
import java.io.IOException;

import javax.sql.DataSource;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.openwire.OpenWireFormat;
import org.apache.activemq.store.PersistenceAdapter;
import org.apache.activemq.store.jdbc.JDBCPersistenceAdapter;
import org.apache.activemq.store.kahadb.KahaDBPersistenceAdapter;
import org.apache.commons.lang3.StringUtils;
import org.onetwo.boot.module.activemq.ActivemqProperties.JdbcStoreProps;
import org.onetwo.boot.module.activemq.ActivemqProperties.KahaDBStoreProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

/**
 * @author wayshall
 * <br/>
 */
@ConditionalOnClass(EnableJms.class)
@ConditionalOnProperty(value=ActivemqProperties.ENABLE_KEY)
@EnableJms
@EnableConfigurationProperties(ActivemqProperties.class)
public class ActivemqConfiguration {
	
	@Bean
	public ActivemqProducerService activemqProducerService(){
		ActivemqProducerService producer = new ActivemqProducerService();
		return producer;
	}
	

	@Configuration
	@ConditionalOnClass(BrokerService.class)
	@ConditionalOnProperty(value=ActivemqProperties.EMBEDDED_ENABLE_KEY)
	protected static class BrokerServiceConfiguration {
		@ConditionalOnMissingBean
		@Bean(initMethod="start", destroyMethod="stop")
		public BrokerService brokerService(PersistenceAdapter persistenceAdapter) throws IOException{
			BrokerService broker = new BrokerService();
			broker.setPersistenceAdapter(persistenceAdapter);
			return broker;
		}
	}
	
	@Configuration
	@ConditionalOnProperty(KahaDBStoreProps.ENABLE_KEY)
	protected static class KahaDBPersistenceStoreConfiguration {
		@Autowired
		ActivemqProperties activemqProperties;
		
		@Bean
		public PersistenceAdapter kahaDBPersistenceAdapter(){
			KahaDBStoreProps kahaProps = activemqProperties.getKahadbStore();
			// default messages store is under AMQ_HOME/data/KahaDB/
			KahaDBPersistenceAdapter kahadb = new KahaDBPersistenceAdapter();
			if(StringUtils.isNotBlank(kahaProps.getDataDir())){
				File dataDir = new File(kahaProps.getDataDir());
				if(!dataDir.exists()){
					dataDir.mkdirs();
				}
				kahadb.setDirectory(dataDir);
			}
			return kahadb;
		}
	}
	
	@Configuration
	@ConditionalOnProperty(JdbcStoreProps.ENABLE_KEY)
	protected static class JdbcDBPersistenceStoreConfiguration {
		@Autowired
		ActivemqProperties activemqProperties;
		@Bean
		public PersistenceAdapter jdbcPersistenceAdapter(DataSource dataSource){
			JdbcStoreProps jdbcProps = activemqProperties.getJdbcStore();
			JDBCPersistenceAdapter jdbc = new JDBCPersistenceAdapter(dataSource, new OpenWireFormat());
			jdbc.setCreateTablesOnStartup(jdbcProps.isCreateTablesOnStartup());
			return jdbc;
		}
	}

}
