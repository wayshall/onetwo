package org.onetwo.common.appbase;

import java.util.Collection;

import org.onetwo.common.propconf.AppConfig;
import org.onetwo.common.utils.AppConstant;

@Deprecated
public class AppBaseConfig extends AppConfig {

//	public static final String EJB_MAPPED_NAME = "yoyob2b_ejb";
	
//	public static final String PERSISTENCE_UNIT = "yoyob2b_unit";

//	public static final String EJB_MAPPED_NAME_KEY = "ejb.mapped.name";
//	public static final String PERSISTENCE_UNIT_KEY = "persistence.unit";
	
//	public static final String APP_EJB_MAPPED_NAME = "yoyob2b_ejb";
	
	public static final String JPQL_SYMBOL = "jpql.symbol";
	public static final String JDBC_DIALET = "jdbc.dialet";
	public static final String CONTAINER_MODULE = "container.module";

	public AppBaseConfig(String config) {
		super(config);
	}
	
	public Class getJPQLSymbol(){
		return this.getClass(JPQL_SYMBOL, null);
	}
	
	public String getJdbcDialet(){
		return this.getProperty(JDBC_DIALET, AppConstant.DIALET_ORACLE);
	}
	
	public Collection<Class> getContainerModules(){
		return getClasses(CONTAINER_MODULE);
		
	}
}
