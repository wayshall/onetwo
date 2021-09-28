package org.onetwo.boot.ds;

import javax.sql.DataSource;

import org.onetwo.boot.core.config.BootJFishConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author weishao zeng
 * <br/>
 */
@Configuration
@ConditionalOnProperty(value = ConfiguratableDatasourceConfiguration.ENABLED_DSNAMES)
public class ConfiguratableDatasourceConfiguration {
	public final static String DATASOURCE_CONFIG_PREFIX = BootJFishConfig.PREFIX + ".datasources";
	public final static String ENABLED_DSNAMES = DATASOURCE_CONFIG_PREFIX + ".enabled"; // jfish.datasources.enabled: datasouceName1, datasouceName2
	

	@SuppressWarnings("unchecked")
	protected <T> T createDataSource(DataSourceProperties properties,
			Class<? extends DataSource> type) {
		return (T) properties.initializeDataSourceBuilder().type(type).build();
	}

	/****
	 * 使用 jfish.datasources.xxxx 的方式配置多个数据源后，由于存在了DataSource类型的bean，
	 * DataSourceAutoConfiguration 自动配置会失效，不注册默认的DataSource，这里复制配置过来，启用默认的DataSource
	 * @author way
	 *
	 */
	@Configuration
	@ConditionalOnClass(org.apache.tomcat.jdbc.pool.DataSource.class)
	@ConditionalOnProperty(name = "spring.datasource.type", havingValue = "org.apache.tomcat.jdbc.pool.DataSource", matchIfMissing = true)
	static class Tomcat extends ConfiguratableDatasourceConfiguration {

		@Bean
		@Primary
		@ConfigurationProperties(prefix = "spring.datasource.tomcat")
		public org.apache.tomcat.jdbc.pool.DataSource dataSource(
				DataSourceProperties properties) {
			org.apache.tomcat.jdbc.pool.DataSource dataSource = createDataSource(
					properties, org.apache.tomcat.jdbc.pool.DataSource.class);
			DatabaseDriver databaseDriver = DatabaseDriver
					.fromJdbcUrl(properties.determineUrl());
			String validationQuery = databaseDriver.getValidationQuery();
			if (validationQuery != null) {
				dataSource.setTestOnBorrow(true);
				dataSource.setValidationQuery(validationQuery);
			}
			return dataSource;
		}
		
		@Bean
		public DefaultTransactionManagementConfigurer defaultTransactionManagementConfigurer() {
			return new DefaultTransactionManagementConfigurer();
		}

	}
}
