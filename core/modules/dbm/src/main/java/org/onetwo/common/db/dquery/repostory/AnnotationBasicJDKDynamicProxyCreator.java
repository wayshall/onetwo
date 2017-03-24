package org.onetwo.common.db.dquery.repostory;

import java.lang.reflect.Method;

import javax.sql.DataSource;

import org.onetwo.common.db.DataBase;
import org.onetwo.common.db.dquery.DynamicMethod;
import org.onetwo.common.db.dquery.JDKDynamicProxyCreator;
import org.onetwo.common.db.filequery.SpringBasedSqlFileScanner;
import org.onetwo.common.db.filequery.spi.SqlFileScanner;
import org.onetwo.common.propconf.ResourceAdapter;
import org.onetwo.common.spring.utils.SpringResourceAdapterImpl;
import org.onetwo.dbm.exception.DbmException;
import org.onetwo.dbm.jdbc.JdbcUtils;
import org.springframework.util.ClassUtils;

import com.google.common.cache.LoadingCache;

public class AnnotationBasicJDKDynamicProxyCreator extends JDKDynamicProxyCreator {

	private SqlFileScanner sqlFileScanner = new SpringBasedSqlFileScanner(ClassUtils.getDefaultClassLoader());
	
	public AnnotationBasicJDKDynamicProxyCreator(Class<?> interfaceClass, LoadingCache<Method, DynamicMethod> methodCache) {
		super(interfaceClass, methodCache);
	}

	@Override
	protected ResourceAdapter<?> getSqlFile(DataSource dataSource) {
		Class<?> repostoryClass = this.interfaceClass;
		DataBase database = JdbcUtils.getDataBase(dataSource);
		String filePath = repostoryClass.getName();
		SpringResourceAdapterImpl sqlRes = getClassPathResource(database, filePath, false);
		if(sqlRes==null){
			filePath = ClassUtils.convertClassNameToResourcePath(repostoryClass.getName());
			sqlRes = getClassPathResource(database, filePath, true);
		}
		
		return sqlRes;
	}
	
	/***
	 * 先根据数据库查找，如果没找到，则默认查找
	 * @param database
	 * @param filePath
	 * @param throwIfNotfound
	 * @return
	 */
	private SpringResourceAdapterImpl getClassPathResource(DataBase database, String filePath, boolean throwIfNotfound){
		SpringResourceAdapterImpl sqlRes = null;
		if(database!=null){
			sqlRes = sqlFileScanner.getClassPathResource(database.getName(), filePath);
		}
		if(!sqlRes.exists()){
			sqlRes = sqlFileScanner.getClassPathResource(null, filePath);
			if(!sqlRes.exists() && throwIfNotfound){
				throw new DbmException("no sql file found for repostory: " + filePath);
			}
		}
		return sqlRes;
	}

}
