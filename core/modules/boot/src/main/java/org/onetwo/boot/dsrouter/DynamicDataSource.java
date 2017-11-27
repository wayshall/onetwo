package org.onetwo.boot.dsrouter;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.onetwo.boot.dsrouter.DatasourceRouterProperties.DsProperties;
import org.onetwo.common.exception.BaseException;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.spring.utils.BeanPropertiesMapper;
import org.onetwo.common.utils.ValueHolder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.google.common.collect.Maps;

/**
 * @author wayshall
 * <br/>
 */
public class DynamicDataSource extends AbstractRoutingDataSource implements InitializingBean {

	@Autowired
	private DatasourceRouterProperties datasourceRouterProperties;
	@Autowired
	private ApplicationContext applicationContext;
	private String lookupStrategy;
	
	private Map<String, LookupKeyStrategy> strategyMap = Maps.newConcurrentMap();
	
	
	@Override
	protected Object determineCurrentLookupKey() {
		if(!strategyMap.containsKey(lookupStrategy)){
			return null;
		}
		return strategyMap.get(lookupStrategy).lookup();
	}

	@Override
	public void afterPropertiesSet() {
		if(StringUtils.isBlank(lookupStrategy)){
			this.lookupStrategy = datasourceRouterProperties.getLookupStrategy();
		}
		this.scanDatasources();
		List<LookupKeyStrategy> strategys = SpringUtils.getBeans(applicationContext, LookupKeyStrategy.class);
		strategys.forEach(strategy->{
			this.registerStrategy(strategy);
		});
		super.afterPropertiesSet();
	}
	
	public void registerStrategy(LookupKeyStrategy strategy){
		this.strategyMap.put(strategy.getName(), strategy);
	}
	
	protected void scanDatasources(){
		Map<String, DsProperties> dsConfig = this.datasourceRouterProperties.getTargets();
		ValueHolder<DataSource> dsHouder = new ValueHolder<>(null);
		Map<Object, Object> targetDataSources = Maps.newLinkedHashMap();
		dsConfig.forEach((key, props)->{
			props.putAll(datasourceRouterProperties.getCommon());
			
			DataSource ds = DataSourceBuilder.create()
								.driverClassName(props.getDriverClassName())
								.url(props.getUrl())
								.username(props.getUsername())
								.password(props.getPassword())
								.build();
			BeanPropertiesMapper mapper = new BeanPropertiesMapper(props, null);
			mapper.mapToObject(ds);
			targetDataSources.put(key, ds);
			if(props.getBoolean("default", false)){//是否作为默认数据源
				dsHouder.setValue(ds);
			}
		});
		if(dsHouder.hasValue()){
			this.setDefaultTargetDataSource(dsHouder.getValue());
		}else if(!targetDataSources.isEmpty()){
			this.setDefaultTargetDataSource(targetDataSources.values().iterator().next());
		}else{
			throw new BaseException("default datasource not found!");
		}
		this.setTargetDataSources(targetDataSources);
	}
	
	

}
