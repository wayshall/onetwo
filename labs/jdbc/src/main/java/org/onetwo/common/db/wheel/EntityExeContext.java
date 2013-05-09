package org.onetwo.common.db.wheel;

import java.io.Serializable;

import org.onetwo.common.db.wheel.EnhanceQueryImpl.EntityValue;
import org.onetwo.common.db.wheel.EnhanceQueryImpl.SelfValueAdaptor;
import org.onetwo.common.utils.Assert;
import org.onetwo.common.utils.LangUtils;


public class EntityExeContext extends SqlExeContext {
	protected EntityBuilder entityBuilder;
	
	public EntityExeContext(EntityBuilder entityBuilder, EnhanceQuery q){
		super(q);
		this.entityBuilder = entityBuilder;
	}

	public EntityExeContext setBy(Object entity){
		if(LangUtils.isBaseType(entity.getClass())){
			getSqlParser().setParameters(new SelfValueAdaptor(entity));
		}else{
			getSqlParser().setParameters(getEntityValue(entity));
		}
		return this;
	}
	
	protected ValueAdaptor getEntityValue(Object entity){
		return new EntityValue(entity, entityBuilder.getTableInfo().getAlias());
	}
	
	public EntityExeContext setId(Serializable id){
		Assert.notNull(id);
		this.getSqlParser().setParameter(entityBuilder.getTableInfo().getPrimaryKey().getJavaNameWithAlias(), id);
		return this;
	}
}
