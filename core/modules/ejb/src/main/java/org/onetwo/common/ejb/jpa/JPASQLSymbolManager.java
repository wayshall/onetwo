package org.onetwo.common.ejb.jpa;

import java.util.Map;

import org.onetwo.common.db.ExtQuery;
import org.onetwo.common.db.sqlext.BooleanValueSQLSymbolParser;
import org.onetwo.common.db.sqlext.DefaultSQLSymbolManagerImpl;
import org.onetwo.common.db.sqlext.HasSymbolParser;
import org.onetwo.common.db.sqlext.JPQLDialetImpl;
import org.onetwo.common.db.sqlext.SQLDialet;
import org.onetwo.common.db.sqlext.SQLSymbolManager;

public class JPASQLSymbolManager extends DefaultSQLSymbolManagerImpl {
	
	public static JPASQLSymbolManager create(){
		return new JPASQLSymbolManager(new JPQLDialetImpl());
	}

	protected JPASQLSymbolManager(SQLDialet sqlDialet) {
		super(sqlDialet);
	}

	@Override
	public ExtQuery createQuery(Class<?> entityClass, String alias, Map<Object, Object> properties) {
		ExtQuery q = new JPAExtQuery(entityClass, alias, properties, this);
		q.initQuery();
		return q;
	}
	
	public SQLSymbolManager initParser() {
		super.initParser();
		register(new BooleanValueSQLSymbolParser(this, "is empty", "is empty", "is not empty"))
		.register("has", new HasSymbolParser(this));
		return this;
	}

}
