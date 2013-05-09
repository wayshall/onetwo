package org.onetwo.common.db.wheel.oracle;

import org.onetwo.common.db.wheel.DefaultEntityBuilder;
import org.onetwo.common.db.wheel.DefaultEntityBuilderFactory;
import org.onetwo.common.db.wheel.EntityBuilder;
import org.onetwo.common.db.wheel.SQLBuilder;
import org.onetwo.common.db.wheel.SQLBuilderFactory;
import org.onetwo.common.db.wheel.TableInfo;
import org.onetwo.common.db.wheel.TableInfoBuilder;

public class OracleEntityBuilderFactory extends DefaultEntityBuilderFactory {
	
	public static class OralceEntityBuilder extends DefaultEntityBuilder {

		protected OralceEntityBuilder(TableInfo tableInfo) {
			super(tableInfo);
		}
		
		protected SQLBuilder makeFetchPKSQLBuilder(){
			SQLBuilder sqlBuilder = createSQLBuilder();
			sqlBuilder.setSeqName(getTableInfo().getSeqName());
			return sqlBuilder;
		}
		
	}

	public OracleEntityBuilderFactory(TableInfoBuilder tableInfoBuilder, SQLBuilderFactory sqlBuilderFactory) {
		super(tableInfoBuilder, sqlBuilderFactory);
	}
	
	protected EntityBuilder newEntityBuilder(TableInfo tableInfo){
		return new OralceEntityBuilder(tableInfo);
	}

}
