package org.onetwo.common.fish.spring;

import org.onetwo.common.db.ParamValues.PlaceHolder;
import org.onetwo.common.db.sql.DynamicQuery;
import org.onetwo.common.fish.orm.DBDialect;
import org.onetwo.common.fish.orm.InnerDBDialet;
import org.onetwo.common.fish.orm.MappedEntryManager;
import org.onetwo.common.fish.orm.AbstractDBDialect.DBMeta;
import org.onetwo.common.spring.SpringUtils;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;
import org.springframework.context.ApplicationContext;

final public class JFishSpringUtils {

	private JFishSpringUtils(){
	}
	
	public static DBDialect getMatchDBDiaclet(ApplicationContext applicationContext, DBMeta dbmeta){
		DBDialect dialect = applicationContext.getBean(dbmeta.getDialetName(), DBDialect.class);
		if(dialect instanceof InnerDBDialet){
			InnerDBDialet innerDialect = (InnerDBDialet) dialect;
			if(dbmeta!=null && innerDialect.getDbmeta()==null)
				innerDialect.setDbmeta(dbmeta);
		}
		return dialect;
	}
	
	public static DBDialect getJFishDBDiaclet(ApplicationContext applicationContext){
		return getJFishDao(applicationContext).getDialect();
	}
	
	public static JFishDaoImplementor getJFishDao(ApplicationContext applicationContext){
		JFishDaoImplementor dao = SpringUtils.getHighestOrder(applicationContext, JFishDaoImplementor.class);
		return dao;
	}
	
	public static MappedEntryManager getJFishMappedEntryManager(ApplicationContext applicationContext){
		MappedEntryManager mappedEntryManager = SpringUtils.getHighestOrder(applicationContext, MappedEntryManager.class);
		return mappedEntryManager;
	}
	
	public static JFishFileQueryImpl castToFileQuery(DynamicQuery aq){
		JFishFileQueryImpl jfq = null;
		if(aq instanceof JFishFileQueryImpl){
			jfq = (JFishFileQueryImpl) aq;
		}else{
			LangUtils.throwBaseException("it's not a file query : " + aq.getClass());
		}
		return jfq;
	}
	
	@SuppressWarnings("unchecked")
	public static void setAnotherQueryValue(DynamicQuery aq, PlaceHolder type, Object...args){
		if(LangUtils.isEmpty(args))
			return ;
		Assert.notNull(type);
		if(type==PlaceHolder.POSITION){
			aq.setParameters(LangUtils.asList(args));
		}else{
			aq.setParameters(LangUtils.asMap(args));
		}
	}
}
